package com.emm.mspayment.service.kafka;

import com.emm.mspayment.events.PaymentProcessedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String PAYMENT_PROCESSED_TOPIC = "payment-processed-topic";

    public void sendPaymentProcessedEvent(PaymentProcessedEvent event) {
        log.info("Enviando PaymentProcessedEvent a Kafka: {}", event);
        kafkaTemplate.send(PAYMENT_PROCESSED_TOPIC, event);
        log.info("PaymentProcessedEvent enviado exitosamente");
    }
}