package com.emm.msregistrations.models;


import com.emm.msregistrations.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "registration")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @Column(nullable = false)
    private Long eventId;


    @Column(nullable = false)
    private Long participantId;
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private RegistrationStatus status = RegistrationStatus.PENDING;

    @Column(length = 255)
    private String qrCode;

    private Long paymentOrderId;
    @Column(nullable = false)
    private BigDecimal eventPrice = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType; // FREE o PAID
    public enum EventType {
        FREE,
        PAID
    }
}
