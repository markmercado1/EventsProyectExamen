package com.emm.mspayment.controller;

import com.emm.mspayment.dto.PaymentOrderRequestDto;
import com.emm.mspayment.dto.PaymentOrderResponseDto;
import com.emm.mspayment.dto.PaymentOrderUpdateDto;
import com.emm.mspayment.enums.PaymentStatus;
import com.emm.mspayment.service.PaymentOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-orders")
@RequiredArgsConstructor
public class PaymentOrderController {

    private final PaymentOrderService paymentOrderService;

    @PostMapping
    public ResponseEntity<PaymentOrderResponseDto> createPaymentOrder(
            @Valid @RequestBody PaymentOrderRequestDto requestDto) {
        PaymentOrderResponseDto response = paymentOrderService.createPaymentOrder(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentOrderResponseDto> getPaymentOrderById(@PathVariable Long id) {
        PaymentOrderResponseDto response = paymentOrderService.getPaymentOrderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentOrderResponseDto>> getAllPaymentOrders() {
        List<PaymentOrderResponseDto> response = paymentOrderService.getAllPaymentOrders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/registration/{registrationId}")
    public ResponseEntity<List<PaymentOrderResponseDto>> getPaymentOrdersByRegistrationId(
            @PathVariable Long registrationId) {
        List<PaymentOrderResponseDto> response = 
                paymentOrderService.getPaymentOrdersByRegistrationId(registrationId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentOrderResponseDto>> getPaymentOrdersByStatus(
            @PathVariable PaymentStatus status) {
        List<PaymentOrderResponseDto> response = paymentOrderService.getPaymentOrdersByStatus(status);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentOrderResponseDto> updatePaymentOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody PaymentOrderUpdateDto updateDto) {
        PaymentOrderResponseDto response = 
                paymentOrderService.updatePaymentOrderStatus(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<PaymentOrderResponseDto> processPayment(@PathVariable Long id) {
        PaymentOrderResponseDto response = paymentOrderService.processPayment(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentOrder(@PathVariable Long id) {
        paymentOrderService.deletePaymentOrder(id);
        return ResponseEntity.noContent().build();
    }
}
