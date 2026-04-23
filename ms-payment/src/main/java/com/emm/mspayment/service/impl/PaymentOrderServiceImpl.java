package com.emm.mspayment.service.impl;

import com.emm.mspayment.dto.PaymentOrderRequestDto;
import com.emm.mspayment.dto.PaymentOrderResponseDto;
import com.emm.mspayment.dto.PaymentOrderUpdateDto;
import com.emm.mspayment.dto.RegistrationResponseDTO;
import com.emm.mspayment.enums.PaymentStatus;
import com.emm.mspayment.events.PaymentProcessedEvent;
import com.emm.mspayment.exceptions.ResourceNotFoundException;
import com.emm.mspayment.feign.RegistrationFeignClient;
import com.emm.mspayment.mapper.PaymentOrderMapper;
import com.emm.mspayment.model.PaymentOrder;
import com.emm.mspayment.repository.PaymentOrderRepository;
import com.emm.mspayment.service.PaymentOrderService;
import com.emm.mspayment.service.kafka.KafkaProducerService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final RegistrationFeignClient registrationFeignClient;
    private final PaymentOrderMapper paymentOrderMapper;
    private final KafkaProducerService kafkaProducerService;
    @Override
    @Transactional
    public PaymentOrderResponseDto createPaymentOrder(PaymentOrderRequestDto requestDto) {
        log.info("Creating payment order for registrationId: {}", requestDto.getRegistrationId());

        RegistrationResponseDTO registration = getRegistrationFromService(requestDto.getRegistrationId());

        PaymentOrder paymentOrder = paymentOrderMapper.toEntity(requestDto);
        PaymentOrder savedOrder = paymentOrderRepository.save(paymentOrder);

        log.info("Payment order created with ID: {}", savedOrder.getPaymentOrderId());

        return paymentOrderMapper.toResponseDto(savedOrder, registration);
    }

    @Override
    @Transactional
    public PaymentOrderResponseDto getPaymentOrderById(Long id) {
        log.info("Fetching payment order with ID: {}", id);

        PaymentOrder paymentOrder = findPaymentOrderById(id);
        RegistrationResponseDTO registration = getRegistrationFromService(paymentOrder.getRegistrationId());

        return paymentOrderMapper.toResponseDto(paymentOrder, registration);
    }

    @Override
    public List<PaymentOrderResponseDto> getAllPaymentOrders() {
        log.info("Fetching all payment orders");

        return paymentOrderRepository.findAll().stream()
                .map(this::mapToResponseDtoWithRegistration)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PaymentOrderResponseDto> getPaymentOrdersByRegistrationId(Long registrationId) {
        log.info("Fetching payment orders for registrationId: {}", registrationId);

        RegistrationResponseDTO registration = getRegistrationFromService(registrationId);

        return paymentOrderRepository.findByRegistrationId(registrationId).stream()
                .map(order -> paymentOrderMapper.toResponseDto(order, registration))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PaymentOrderResponseDto> getPaymentOrdersByStatus(PaymentStatus status) {
        log.info("Fetching payment orders with status: {}", status);

        return paymentOrderRepository.findByStatus(status).stream()
                .map(this::mapToResponseDtoWithRegistration)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentOrderResponseDto updatePaymentOrderStatus(Long id, PaymentOrderUpdateDto updateDto) {
        log.info("Updating payment order status for ID: {} to {}", id, updateDto.getStatus());

        PaymentOrder paymentOrder = findPaymentOrderById(id);
        PaymentStatus oldStatus = paymentOrder.getStatus();
        paymentOrder.setStatus(updateDto.getStatus());

        if (updateDto.getStatus() == PaymentStatus.COMPLETED) {
            paymentOrder.setPaymentDate(LocalDateTime.now());
        }

        PaymentOrder updatedOrder = paymentOrderRepository.save(paymentOrder);

        // ← NUEVO: Enviar evento si cambió a COMPLETED o FAILED
        if (shouldSendPaymentEvent(oldStatus, updateDto.getStatus())) {
            sendPaymentProcessedEvent(updatedOrder);
        }

        RegistrationResponseDTO registration = getRegistrationFromService(updatedOrder.getRegistrationId());
        return paymentOrderMapper.toResponseDto(updatedOrder, registration);
    }

    @Override
    @Transactional
    public PaymentOrderResponseDto processPayment(Long id) {
        log.info("Processing payment for order ID: {}", id);

        PaymentOrder paymentOrder = findPaymentOrderById(id);

        if (paymentOrder.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Payment order is not in PENDING status");
        }

        paymentOrder.setStatus(PaymentStatus.PROCESSING);
        paymentOrderRepository.save(paymentOrder);

        paymentOrder.setStatus(PaymentStatus.COMPLETED);
        paymentOrder.setPaymentDate(LocalDateTime.now());

        PaymentOrder processedOrder = paymentOrderRepository.save(paymentOrder);

        sendPaymentProcessedEvent(processedOrder);

        RegistrationResponseDTO registration = getRegistrationFromService(processedOrder.getRegistrationId());

        log.info("Payment processed successfully for order ID: {}", id);
        return paymentOrderMapper.toResponseDto(processedOrder, registration);
    }
    @Override
    @Transactional
    public void deletePaymentOrder(Long id) {
        log.info("Deleting payment order with ID: {}", id);

        PaymentOrder paymentOrder = findPaymentOrderById(id);

        if (paymentOrder.getStatus() == PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot delete a completed payment order");
        }

        paymentOrderRepository.delete(paymentOrder);
        log.info("Payment order deleted successfully");
    }

    private PaymentOrder findPaymentOrderById(Long id) {
        return paymentOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment order not found with ID: " + id));
    }

    private RegistrationResponseDTO getRegistrationFromService(Long registrationId) {
        try {
            return registrationFeignClient.getRegistrationById(registrationId);
        } catch (FeignException.NotFound e) {
            log.error("Registration not found with ID: {}", registrationId);
            throw new ResourceNotFoundException("Registration not found with ID: " + registrationId);
        } catch (FeignException e) {
            log.error("Error calling registration service: {}", e.getMessage());
            throw new RuntimeException("Error communicating with registration service");
        }
    }

    private PaymentOrderResponseDto mapToResponseDtoWithRegistration(PaymentOrder order) {
        try {
            RegistrationResponseDTO registration = getRegistrationFromService(order.getRegistrationId());
            return paymentOrderMapper.toResponseDto(order, registration);
        } catch (Exception e) {
            log.warn("Could not fetch registration data for ID: {}", order.getRegistrationId());
            return paymentOrderMapper.toResponseDto(order, null);
        }
    }
    private boolean shouldSendPaymentEvent(PaymentStatus oldStatus, PaymentStatus newStatus) {
        return (newStatus == PaymentStatus.COMPLETED || newStatus == PaymentStatus.FAILED)
                && oldStatus != newStatus;
    }

    private void sendPaymentProcessedEvent(PaymentOrder paymentOrder) {
        PaymentProcessedEvent event = new PaymentProcessedEvent(
                paymentOrder.getRegistrationId(),
                paymentOrder.getPaymentOrderId(),
                paymentOrder.getStatus(),
                paymentOrder.getAmount(),
                paymentOrder.getPaymentDate()
        );

        kafkaProducerService.sendPaymentProcessedEvent(event);
        log.info("PaymentProcessedEvent enviado para registrationId: {}", paymentOrder.getRegistrationId());
    }
}