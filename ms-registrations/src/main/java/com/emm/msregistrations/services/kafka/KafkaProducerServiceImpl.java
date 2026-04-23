package com.emm.msregistrations.services.kafka;

import com.emm.msregistrations.events.RegistrationCreatedEvent;
import com.emm.msregistrations.services.KafkaProducerService;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "registros-topic";

    @Override
    public void enviarRegistroCreado(RegistrationCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event);
        System.out.println("Evento registro.creado event: " + event);
    }

}
