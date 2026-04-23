package com.emm.msregistrations.dtos;

import com.emm.msregistrations.enums.RegistrationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRegistrationDTO {
    
    @NotNull(message = "El estado es obligatorio")
    private RegistrationStatus status;
    
    @Size(max = 255, message = "El código QR no puede exceder 255 caracteres")
    private String qrCode;
    
    private Boolean requiresPayment;
    
    private Long paymentOrderId;
}