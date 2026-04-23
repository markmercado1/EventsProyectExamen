package com.emm.mspayment.repository;

import com.emm.mspayment.enums.PaymentStatus;
import com.emm.mspayment.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    
    List<PaymentOrder> findByRegistrationId(Long registrationId);
    
    List<PaymentOrder> findByStatus(PaymentStatus status);
    
    List<PaymentOrder> findByRegistrationIdAndStatus(Long registrationId, PaymentStatus status);
}