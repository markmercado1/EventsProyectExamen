package com.emm.mspayment.dto;

import com.emm.mspayment.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderUpdateDto {
    private PaymentStatus status;
}