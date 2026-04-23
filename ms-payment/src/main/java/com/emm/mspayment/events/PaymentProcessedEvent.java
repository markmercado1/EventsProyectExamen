package com.emm.mspayment.events;

import com.emm.mspayment.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentProcessedEvent(
    Long registrationId,
    Long paymentOrderId,
    PaymentStatus paymentStatus,
    BigDecimal amount,
    LocalDateTime paymentDate
) {}