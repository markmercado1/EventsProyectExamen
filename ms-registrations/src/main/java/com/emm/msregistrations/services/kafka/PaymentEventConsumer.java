package com.emm.msregistrations.services.kafka;


import com.emm.msregistrations.enums.RegistrationStatus;
import com.emm.msregistrations.events.PaymentProcessedEvent;
import com.emm.msregistrations.exceptions.ResourceNotFoundException;
import com.emm.msregistrations.models.Registration;
import com.emm.msregistrations.repositorys.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final RegistrationRepository registrationRepository;

    @KafkaListener(
        topics = "payment-processed-topic",
        groupId = "registration-service-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handlePaymentProcessed(PaymentProcessedEvent event) {
        log.info("PaymentProcessedEvent recibido en ms-registration: {}", event);

        try {
            Registration registration = registrationRepository.findById(event.registrationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Registro no encontrado con ID: " + event.registrationId()));

            // Actualizar el paymentOrderId
            registration.setPaymentOrderId(event.paymentOrderId());

            // Actualizar el status según el resultado del pago
            if ("COMPLETED".equals(event.paymentStatus().name())) {
                registration.setStatus(RegistrationStatus.CONFIRMED);
                log.info("Registro {} confirmado exitosamente tras pago completado", event.registrationId());
            } else if ("FAILED".equals(event.paymentStatus().name())) {
                registration.setStatus(RegistrationStatus.CANCELLED);
                log.warn("Registro {} cancelado debido a fallo en el pago", event.registrationId());
            }

            registrationRepository.save(registration);
            log.info("Registro actualizado exitosamente con status: {}", registration.getStatus());

        } catch (Exception e) {
            log.error("Error al procesar PaymentProcessedEvent: {}", e.getMessage(), e);
            throw e;
        }
    }
}