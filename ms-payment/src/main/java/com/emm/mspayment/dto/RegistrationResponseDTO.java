package com.emm.mspayment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponseDTO {
    private Long registrationId;
    private String studentName;
    private String email;
    private String courseCode;
}