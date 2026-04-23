package com.emm.msregistrations.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentProcessedEvent(
    Long registrationId,
    Long paymentOrderId,
    PaymentStatus paymentStatus,
    BigDecimal amount,
    LocalDateTime paymentDate
) {
    public enum PaymentStatus {
        COMPLETED,
        FAILED
    }
}