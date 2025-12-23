package com.example.bankcards.service;

import com.example.bankcards.dto.request.AuthRequestDto;
import com.example.bankcards.dto.request.RegisterRequestDto;
import com.example.bankcards.dto.response.AuthResponseDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.LoginAlreadyExistsException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtTokenProvider;
import com.example.bankcards.service.impl.AuthServiceImpl;
import com.example.bankcards.util.AuthUtil;
import com.example.bankcards.util.ConstantUtil;
import com.example.bankcards.util.RegisterUtil;
import com.example.bankcards.util.RoleUtil;
import com.example.bankcards.util.UserUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_success() {
        AuthRequestDto request = AuthUtil.getValidAuthRequestDto();

        User user = UserUtil.getValidUser;
        String token = ConstantUtil.VALID_JWT_TOKEN;

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtTokenProvider.generateToken(user)).thenReturn(token);

        AuthResponseDto result = authService.login(request);

        assertEquals(token, result.token());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(user);
    }

    @Test
    void register_success() {
        RegisterRequestDto request = RegisterUtil.getValidRegisterRequestDto();

        when(userRepository.findByUsername(request.username()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password()))
                .thenReturn("encoded-password");
        when(roleRepository.findByNameIn(Set.of("USER")))
                .thenReturn(Set.of(RoleUtil.getRoleUser()));

        String result = authService.register(request);

        assertEquals(
                "Пользователь с логином = " + ConstantUtil.VALID_USERNAME + " успешно зарегистрирован",
                result
        );
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_loginAlreadyExists() {
        RegisterRequestDto request = RegisterUtil.getValidRegisterRequestDto();

        when(userRepository.findByUsername(request.username()))
                .thenReturn(Optional.of(UserUtil.getValidUser));

        assertThrows(LoginAlreadyExistsException.class,
                () -> authService.register(request));

        verify(userRepository, never()).save(any());
    }
}
