package com.emm.authservice.service;

import com.emm.authservice.dtos.AuthUserDto;
import com.emm.authservice.models.AuthUser;
import com.emm.authservice.dtos.TokenDto;

public interface AuthUserService {
    AuthUser save(AuthUserDto authUserDto);


    TokenDto login(AuthUserDto authUserDto);

    AuthUserDto findById(Long id);
    boolean existsById(Long id);
    TokenDto validate(String token);
}
