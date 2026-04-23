package com.emm.authservice.controller;


import com.emm.authservice.dtos.AuthUserDto;
import com.emm.authservice.models.AuthUser;
import com.emm.authservice.dtos.TokenDto;
import com.emm.authservice.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthUserController {
    @Autowired
    AuthUserService authUserService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody AuthUserDto authUserDto) {
        TokenDto tokenDto = authUserService.login(authUserDto);
        if (tokenDto == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenDto> validate(@RequestParam String token) {
        TokenDto tokenDto = authUserService.validate(token);
        if (tokenDto == null)
            return ResponseEntity
                    .badRequest()
                    .body(new TokenDto("Token inválido o expirado"));
        return ResponseEntity.ok(tokenDto);
    }
    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth service activo");
    }

    @PostMapping("/create")
    public ResponseEntity<AuthUser> create(@RequestBody AuthUserDto authUserDto) {
        AuthUser authUser = authUserService.save(authUserDto);
        if (authUser == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(authUser);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        boolean exists = authUserService.existsById(id);
        return ResponseEntity.ok(exists);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AuthUserDto> getUserById(@PathVariable Long id) {
        AuthUserDto user = authUserService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

}