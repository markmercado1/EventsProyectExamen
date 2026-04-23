package com.emm.authservice.dtos;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TokenDto {
    private String token;

}
