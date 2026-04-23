package com.emm.msregistrations.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {

    private Long eventId;
    private String name;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String modality;        // ← Como String, no enum
    private String eventType;       // ← Como String, no enum (FREE/PAID)
    private Integer maxCapacity;
    private Long organizerId;
    private String organizer;
    private String address;
    private String status;          // ← Como String, no enum
    private BigDecimal price;       // ← NUEVO campo

    // Métodos helper para trabajar más fácil
    public boolean isPaidEvent() {
        return "PAID".equalsIgnoreCase(this.eventType);
    }

    public boolean isFreeEvent() {
        return "FREE".equalsIgnoreCase(this.eventType);
    }
}