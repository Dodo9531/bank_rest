package com.example.bankcards.security;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ExpiredTokenException;
import com.example.bankcards.exception.InvalidTokenException;
import com.example.bankcards.util.ExceptionMessages;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * Провайдер JWT токенов
 */


@Component
public class JwtTokenProvider {

    private final Key key;
    private final long expiration;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // создаём ключ HMAC
        this.expiration = expiration;
    }

    /**
     * Генерация JWT токена для пользователя с ролями
     */

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * Извлечение userId из токена
     *
     * @param token токен
     * @return идентфикатор пользователя
     */
    public String getUserIdFromToken(String token) {
        return parse(token).getSubject();
    }

    /**
     * Получение ролей из JWT
     */

    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        return parse(token).get("roles", List.class);
    }

    /**
     * Валидация токена
     *
     * @param token токен
     */

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException(ExceptionMessages.JWT_TOKEN_EXPIRED.getDescription());
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(ExceptionMessages.JWT_TOKEN_NOT_VALID.getDescription());
        }
    }

    /**
     * Парсинг токена и извлечение Claims
     *
     * @param token токен
     * @return Claims пользователя с логином, ролями, времени создания токена и времени истечения действия токена
     */

    private Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
