package com.emm.mspayment.service.kafka;

import com.emm.mspayment.enums.PaymentStatus;
import com.emm.mspayment.events.RegistrationCreatedEvent;
import com.emm.mspayment.model.PaymentOrder;
import com.emm.mspayment.repository.PaymentOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationEventConsumer {

    private final PaymentOrderRepository paymentOrderRepository;

    @KafkaListener(
        topics = "registros-topic",
        groupId = "pago-consumer-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleRegistrationCreated(RegistrationCreatedEvent event) {
        log.info("Evento recibido en ms-payment: {}", event);

        try {
            // Crear la orden de pago
            PaymentOrder paymentOrder = new PaymentOrder();
            paymentOrder.setRegistrationId(event.registrationId());
            paymentOrder.setAmount(event.amount());
            paymentOrder.setCurrency("PEN");
            paymentOrder.setStatus(PaymentStatus.PENDING);

            PaymentOrder savedOrder = paymentOrderRepository.save(paymentOrder);
            
            log.info("PaymentOrder creada exitosamente con ID: {} para registrationId: {}", 
                     savedOrder.getPaymentOrderId(), event.registrationId());

        } catch (Exception e) {
            log.error("Error al procesar RegistrationCreatedEvent: {}", e.getMessage(), e);
            throw e;
        }
    }
}