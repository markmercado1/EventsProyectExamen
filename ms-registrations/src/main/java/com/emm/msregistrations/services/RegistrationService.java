package com.emm.msregistrations.services;



import com.emm.msregistrations.dtos.CreateRegistrationDTO;
import com.emm.msregistrations.dtos.RegistrationResponseDTO;
import com.emm.msregistrations.dtos.UpdateRegistrationDTO;
import com.emm.msregistrations.enums.RegistrationStatus;

import java.util.List;

public interface RegistrationService {

    RegistrationResponseDTO createRegistration(CreateRegistrationDTO dto);

    RegistrationResponseDTO updateRegistration(Long id, UpdateRegistrationDTO dto);

    RegistrationResponseDTO getRegistrationById(Long id);

    List<RegistrationResponseDTO> getAllRegistrations();

    List<RegistrationResponseDTO> getRegistrationsByEventId(Long eventId);

    List<RegistrationResponseDTO> getRegistrationsByParticipantId(Long participantId);

    List<RegistrationResponseDTO> getRegistrationsByStatus(RegistrationStatus status);

    void deleteRegistration(Long id);

    String generateQRCode(Long registrationId);
}