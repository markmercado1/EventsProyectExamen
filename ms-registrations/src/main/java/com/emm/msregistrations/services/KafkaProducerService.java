package com.emm.msregistrations.services;


import com.emm.msregistrations.events.RegistrationCreatedEvent;

public interface KafkaProducerService {
    void enviarRegistroCreado(RegistrationCreatedEvent event);
}
