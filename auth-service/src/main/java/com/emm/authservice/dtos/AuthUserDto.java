package com.emm.authservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDto {
    private Long id;
    private String userName;
    private String password;
}