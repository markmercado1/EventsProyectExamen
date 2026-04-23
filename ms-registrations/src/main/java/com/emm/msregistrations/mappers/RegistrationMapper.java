package com.emm.msregistrations.mappers;
import com.emm.msregistrations.dtos.CreateRegistrationDTO;
import com.emm.msregistrations.dtos.RegistrationResponseDTO;
import com.emm.msregistrations.dtos.UpdateRegistrationDTO;
import com.emm.msregistrations.enums.RegistrationStatus;
import com.emm.msregistrations.models.Registration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RegistrationMapper {

    public Registration toEntity(CreateRegistrationDTO dto) {
        if (dto == null) {
            return null;
        }

        return Registration.builder()
                .eventId(dto.getEventId())
                .participantId(dto.getParticipantId())
                .registrationDate(LocalDateTime.now())
                .status(RegistrationStatus.PENDING)

                .build();
    }

    public void updateEntity(UpdateRegistrationDTO dto, Registration entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }

        if (dto.getQrCode() != null) {
            entity.setQrCode(dto.getQrCode());
        }


    }

    public RegistrationResponseDTO toResponseDTO(Registration entity) {
        if (entity == null) {
            return null;
        }

        return RegistrationResponseDTO.builder()
                .registrationId(entity.getRegistrationId())
                .eventId(entity.getEventId())
                .participantId(entity.getParticipantId())
                .registrationDate(entity.getRegistrationDate())
                .status(entity.getStatus())
                .qrCode(entity.getQrCode())
                .build();
    }
}