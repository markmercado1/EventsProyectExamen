package com.emm.mspayment.dto;
import com.emm.mspayment.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderResponseDto {
    private Long paymentOrderId;
    private Long registrationId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private LocalDateTime creationDate;
    private LocalDateTime paymentDate;
    private RegistrationResponseDTO registration; // Datos del microservicio externo
}