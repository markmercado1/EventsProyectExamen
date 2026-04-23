package com.emm.mspayment.events;

import java.math.BigDecimal;

public record RegistrationCreatedEvent(
        Long registrationId,
        Long participantId,
        Long eventId,
        Boolean requiresPayment,
        BigDecimal amount

) {}