package com.emm.msregistrations.services.impl;


import com.emm.msregistrations.dtos.*;
import com.emm.msregistrations.enums.RegistrationStatus;
import com.emm.msregistrations.events.RegistrationCreatedEvent;
import com.emm.msregistrations.exceptions.DuplicateRegistrationException;
import com.emm.msregistrations.exceptions.ResourceNotFoundException;
import com.emm.msregistrations.feign.EventFeign;
import com.emm.msregistrations.feign.ParticipantFeign;
import com.emm.msregistrations.feign.PaymentFeign;
import com.emm.msregistrations.mappers.RegistrationMapper;
import com.emm.msregistrations.models.Registration;
import com.emm.msregistrations.repositorys.RegistrationRepository;
import com.emm.msregistrations.services.KafkaProducerService;
import com.emm.msregistrations.services.RegistrationService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private final EventFeign eventFeign;
    private final ParticipantFeign participantFeign;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public RegistrationResponseDTO createRegistration(CreateRegistrationDTO dto) {
        log.info("Creando registro para evento {} y participante {}", dto.getEventId(), dto.getParticipantId());

        // 1. Validar y OBTENER el evento (no solo validar)
        EventDTO event = validateAndGetEvent(dto.getEventId());
        validateParticipantExists(dto.getParticipantId());

        // 2. Verificar duplicados
        if (registrationRepository.existsByEventIdAndParticipantId(dto.getEventId(), dto.getParticipantId())) {
            throw new DuplicateRegistrationException("Ya existe un registro para este participante");
        }

        // 3. Crear el registro CON los datos del evento
        Registration registration = registrationMapper.toEntity(dto);
        registration.setStatus(RegistrationStatus.PENDING);

        // ✅ AUTOMÁTICO: convertir String a Enum y setear datos del evento
        registration.setEventType(
                Registration.EventType.valueOf(event.getEventType()) // String "PAID" → Enum PAID
        );
        registration.setEventPrice(event.getPrice() != null ? event.getPrice() : BigDecimal.ZERO);

        Registration savedRegistration = registrationRepository.save(registration);
        log.info("Registro creado exitosamente con ID: {}", savedRegistration.getRegistrationId());

        // 4. Decidir flujo según el tipo de evento
        if ("PAID".equalsIgnoreCase(event.getEventType())) {  // ← Comparar String
            // Evento de pago: enviar a Kafka para que ms-payment genere la orden
            RegistrationCreatedEvent kafkaEvent = new RegistrationCreatedEvent(
                    savedRegistration.getRegistrationId(),
                    savedRegistration.getParticipantId(),
                    savedRegistration.getEventId(),
                    event.getPrice()  // ← precio real del evento
            );
            kafkaProducerService.enviarRegistroCreado(kafkaEvent);
            log.info("Evento PAID: mensaje enviado a Kafka para pago de registrationId: {}",
                    savedRegistration.getRegistrationId());
        } else {
            // Evento gratuito: confirmar automáticamente
            savedRegistration.setStatus(RegistrationStatus.CONFIRMED);
            registrationRepository.save(savedRegistration);
            log.info("Evento FREE: registro confirmado automáticamente para registrationId: {}",
                    savedRegistration.getRegistrationId());
        }

        return enrichResponseDTO(registrationMapper.toResponseDTO(savedRegistration));
    }

    @Override
    public RegistrationResponseDTO updateRegistration(Long id, UpdateRegistrationDTO dto) {
        log.info("Actualizando registro con ID: {}", id);

        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro no encontrado con ID: " + id));


        registrationMapper.updateEntity(dto, registration);
        Registration updatedRegistration = registrationRepository.save(registration);

        log.info("Registro actualizado exitosamente");

        return enrichResponseDTO(registrationMapper.toResponseDTO(updatedRegistration));
    }

    @Override
    public RegistrationResponseDTO getRegistrationById(Long id) {
        log.info("Buscando registro con ID: {}", id);

        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro no encontrado con ID: " + id));

        return enrichResponseDTO(registrationMapper.toResponseDTO(registration));
    }

    @Override
    public List<RegistrationResponseDTO> getAllRegistrations() {
        log.info("Obteniendo todos los registros");

        return registrationRepository.findAll().stream()
                .map(registrationMapper::toResponseDTO)
                .map(this::enrichResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistrationResponseDTO> getRegistrationsByEventId(Long eventId) {
        log.info("Buscando registros para el evento: {}", eventId);

        validateEventExists(eventId);

        return registrationRepository.findByEventId(eventId).stream()
                .map(registrationMapper::toResponseDTO)
                .map(this::enrichResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistrationResponseDTO> getRegistrationsByParticipantId(Long participantId) {
        log.info("Buscando registros para el participante: {}", participantId);

        validateParticipantExists(participantId);

        return registrationRepository.findByParticipantId(participantId).stream()
                .map(registrationMapper::toResponseDTO)
                .map(this::enrichResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistrationResponseDTO> getRegistrationsByStatus(RegistrationStatus status) {
        log.info("Buscando registros con estado: {}", status);

        return registrationRepository.findByStatus(status).stream()
                .map(registrationMapper::toResponseDTO)
                .map(this::enrichResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRegistration(Long id) {
        log.info("Eliminando registro con ID: {}", id);

        if (!registrationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Registro no encontrado con ID: " + id);
        }

        registrationRepository.deleteById(id);
        log.info("Registro eliminado exitosamente");
    }

    @Override
    public String generateQRCode(Long registrationId) {
        log.info("Generando código QR para registro: {}", registrationId);

        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registro no encontrado con ID: " + registrationId));

        String qrCode = UUID.randomUUID().toString();
        registration.setQrCode(qrCode);
        registrationRepository.save(registration);

        log.info("Código QR generado exitosamente");
        return qrCode;
    }

    // ========== Métodos auxiliares ==========

    private void validateEventExists(Long eventId) {
        try {
            eventFeign.getEventById(eventId);
            log.debug("Evento validado: {}", eventId);
        } catch (FeignException.NotFound e) {
            log.error("Evento no encontrado: {}", eventId);
            throw new ResourceNotFoundException("Evento no encontrado con ID: " + eventId);
        } catch (FeignException e) {
            log.error("Error al validar evento: {}", e.getMessage());
            throw new RuntimeException("Error al comunicarse con el servicio de eventos", e);
        }
    }

    private void validateParticipantExists(Long participantId) {
        try {
            participantFeign.getParticipantById(participantId);
            log.debug("Participante validado: {}", participantId);
        } catch (FeignException.NotFound e) {
            log.error("Participante no encontrado: {}", participantId);
            throw new ResourceNotFoundException("Participante no encontrado con ID: " + participantId);
        } catch (FeignException e) {
            log.error("Error al validar participante: {}", e.getMessage());
            throw new RuntimeException("Error al comunicarse con el servicio de participantes", e);
        }
    }

    private EventDTO validateAndGetEvent(Long eventId) {
        try {
            EventDTO event = eventFeign.getEventById(eventId);
            log.debug("Evento obtenido: {} - Tipo: {} - Precio: {}",
                    eventId, event.getEventType(), event.getPrice());
            return event;
        } catch (FeignException.NotFound e) {
            log.error("Evento no encontrado: {}", eventId);
            throw new ResourceNotFoundException("Evento no encontrado con ID: " + eventId);
        } catch (FeignException e) {
            log.error("Error al validar evento: {}", e.getMessage());
            throw new RuntimeException("Error al comunicarse con el servicio de eventos", e);
        }
    }

    private RegistrationResponseDTO enrichResponseDTO(RegistrationResponseDTO dto) {
        try {
            // Obtener información del evento
            EventDTO event = eventFeign.getEventById(dto.getEventId());
            dto.setEvent(event);
        } catch (FeignException e) {
            log.warn("No se pudo obtener información del evento: {}", e.getMessage());
        }

        try {
            // Obtener información del participante
            ParticipantDTO participant = participantFeign.getParticipantById(dto.getParticipantId());
            dto.setParticipant(participant);
        } catch (FeignException e) {
            log.warn("No se pudo obtener información del participante: {}", e.getMessage());
        }



        return dto;
    }
}