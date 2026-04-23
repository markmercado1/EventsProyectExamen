package com.emm.msregistrations.events;

import java.math.BigDecimal;

public record RegistrationCreatedEvent(
        Long registrationId,
        Long participantId,
        Long eventId,
        BigDecimal amount
) {}