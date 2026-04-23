package com.emm.mspayment.mapper;



import com.emm.mspayment.dto.PaymentOrderRequestDto;
import com.emm.mspayment.dto.PaymentOrderResponseDto;
import com.emm.mspayment.dto.PaymentOrderUpdateDto;
import com.emm.mspayment.dto.RegistrationResponseDTO;
import com.emm.mspayment.enums.PaymentStatus;
import com.emm.mspayment.feign.RegistrationFeignClient;
import com.emm.mspayment.model.PaymentOrder;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentOrderMapper {

    public PaymentOrder toEntity(PaymentOrderRequestDto dto) {
        return PaymentOrder.builder()
                .registrationId(dto.getRegistrationId())
                .amount(dto.getAmount())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "PEN")
                .status(PaymentStatus.PENDING)
                .creationDate(LocalDateTime.now())
                .build();
    }

    public PaymentOrderResponseDto toResponseDto(PaymentOrder entity) {
        return toResponseDto(entity, null);
    }

    public PaymentOrderResponseDto toResponseDto(PaymentOrder entity, RegistrationResponseDTO registration) {
        return PaymentOrderResponseDto.builder()
                .paymentOrderId(entity.getPaymentOrderId())
                .registrationId(entity.getRegistrationId())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .status(entity.getStatus())
                .creationDate(entity.getCreationDate())
                .paymentDate(entity.getPaymentDate())
                .build();
    }
}
