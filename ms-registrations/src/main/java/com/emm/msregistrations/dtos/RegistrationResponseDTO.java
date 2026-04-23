package com.emm.msregistrations.dtos;

import com.emm.msregistrations.enums.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationResponseDTO {
    
    private Long registrationId;
    private Long eventId;
    private Long participantId;
    private LocalDateTime registrationDate;
    private RegistrationStatus status;
    private String qrCode;
    private Boolean requiresPayment;
    private Long paymentOrderId;
    
    // Información adicional de otros microservicios
    private EventDTO event;
    private ParticipantDTO participant;
    private PaymentOrderDTO paymentOrder;
}