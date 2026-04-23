
package com.emm.authservice.security;

import com.emm.authservice.models.AuthUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    private final Key key;

    public JwtProvider(@Value("${jwt.secret:}") String secret) {
        Key tempKey;
        try {
            if (secret == null || secret.isBlank()) {
                // Genera una clave segura si no hay ninguna configurada
                tempKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                System.out.println("⚠️ No se encontró jwt.secret, se generó una nueva clave temporal:");
                System.out.println(Base64.getEncoder().encodeToString(tempKey.getEncoded()));
            } else {
                byte[] decoded = Decoders.BASE64.decode(secret);
                if (decoded.length < 32) {
                    System.out.println("⚠️ Clave muy corta (" + decoded.length + " bytes). Generando una nueva válida...");
                    tempKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                    System.out.println("🔑 Nueva clave válida (cópiala en tu YML):");
                    System.out.println(Base64.getEncoder().encodeToString(tempKey.getEncoded()));
                } else {
                    tempKey = Keys.hmacShaKeyFor(decoded);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error construyendo JwtProvider: " + e.getMessage());
            throw e;
        }

        this.key = tempKey;
    }

    public String createToken(AuthUser authUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", authUser.getId());

        Date now = new Date();
        Date exp = new Date(now.getTime() + 3600000); // 1 hora

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authUser.getUserName())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserNameFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return "bad token";
        }
    }
}