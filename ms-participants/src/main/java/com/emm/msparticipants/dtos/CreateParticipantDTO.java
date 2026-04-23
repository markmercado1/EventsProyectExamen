package com.emm.msparticipants.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateParticipantDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}