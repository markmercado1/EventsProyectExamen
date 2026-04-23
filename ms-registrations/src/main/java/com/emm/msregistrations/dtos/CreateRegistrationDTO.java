package com.emm.msregistrations.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRegistrationDTO {
    
    @NotNull(message = "El ID del evento es obligatorio")
    private Long eventId;
    
    @NotNull(message = "El ID del participante es obligatorio")
    private Long participantId;
    

}