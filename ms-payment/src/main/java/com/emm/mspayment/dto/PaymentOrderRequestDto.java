package com.emm.mspayment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderRequestDto {
    private Long registrationId;
    private BigDecimal amount;
    private String currency;
}