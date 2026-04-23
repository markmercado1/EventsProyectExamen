package com.emm.mspayment.model;

import com.emm.mspayment.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "payment_order")
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentOrderId;

    private Long registrationId;

    @Column(nullable = true, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(length = 10, nullable = false)
    private String currency = "PEN";

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
}