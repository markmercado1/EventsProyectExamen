package com.emm.mspayment.service;

import com.emm.mspayment.dto.PaymentOrderRequestDto;
import com.emm.mspayment.dto.PaymentOrderResponseDto;
import com.emm.mspayment.dto.PaymentOrderUpdateDto;
import com.emm.mspayment.enums.PaymentStatus;

import java.util.List;

public interface PaymentOrderService {
    
    PaymentOrderResponseDto createPaymentOrder(PaymentOrderRequestDto requestDto);
    
    PaymentOrderResponseDto getPaymentOrderById(Long id);
    
    List<PaymentOrderResponseDto> getAllPaymentOrders();
    
    List<PaymentOrderResponseDto> getPaymentOrdersByRegistrationId(Long registrationId);
    
    List<PaymentOrderResponseDto> getPaymentOrdersByStatus(PaymentStatus status);
    
    PaymentOrderResponseDto updatePaymentOrderStatus(Long id, PaymentOrderUpdateDto updateDto);
    
    PaymentOrderResponseDto processPayment(Long id);
    
    void deletePaymentOrder(Long id);
}