package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.AuthRequestDto;
import com.example.bankcards.dto.request.RegisterRequestDto;
import com.example.bankcards.dto.response.AuthResponseDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.LoginAlreadyExistsException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtTokenProvider;
import com.example.bankcards.service.AuthService;
import com.example.bankcards.util.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Реализация сервиса отвечающего за регистрацию и авторизацию пользователей
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /**
     * Попытка авторизации
     *
     * @param request запрос на авторизацию с логином и паролем
     * @return JWT токен
     */
    @Override
    public AuthResponseDto login(AuthRequestDto request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User userDetails = (User) auth.getPrincipal();

        String token = jwtTokenProvider.generateToken(userDetails);

        return new AuthResponseDto(token);
    }

    /**
     * Регистрация нового пользователя
     *
     * @param request запрос на регистрацию с именем, фамилией, логином и паролем
     * @return ре
     */
    @Override
    public String register(RegisterRequestDto request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new LoginAlreadyExistsException(ExceptionMessages.LOGIN_IS_TAKEN.getDescription());
        }

        Set<Role> userRoles = roleRepository.findByNameIn(Set.of("USER"));

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .roles(userRoles)
                .build();

        userRepository.save(user);

        return "Пользователь с логином = " + user.getUsername() + " успешно зарегистрирован";
    }
}
