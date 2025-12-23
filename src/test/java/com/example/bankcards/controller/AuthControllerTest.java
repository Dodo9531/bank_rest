package com.example.bankcards.controller;

import com.example.bankcards.exception.LoginAlreadyExistsException;
import com.example.bankcards.security.JwtAuthenticationFilter;
import com.example.bankcards.service.AuthService;
import com.example.bankcards.util.AuthUtil;
import com.example.bankcards.util.ConstantUtil;
import com.example.bankcards.util.ExceptionMessages;
import com.example.bankcards.util.RegisterUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void login_success() throws Exception {
        Mockito.when(authService.login(any()))
                .thenReturn(AuthUtil.getValidAuthResponseDto());

        mockMvc.perform(post(ConstantUtil.LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AuthUtil.getValidAuthRequestDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(ConstantUtil.VALID_JWT_TOKEN));
    }

    @Test
    void register_success() throws Exception {
        Mockito.when(authService.register(any()))
                .thenReturn("Пользователь с логином = " + ConstantUtil.VALID_USERNAME + " успешно зарегистрирован");

        mockMvc.perform(post(ConstantUtil.REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(RegisterUtil.getValidRegisterRequestDto())))
                .andExpect(status().isCreated())
                .andExpect(content().string("Пользователь с логином = " + ConstantUtil.VALID_USERNAME + " успешно зарегистрирован"));
    }

    @Test
    void register_loginAlreadyExists() throws Exception {
        Mockito.when(authService.register(any()))
                .thenThrow(new LoginAlreadyExistsException(ExceptionMessages.LOGIN_IS_TAKEN.getDescription()));

        mockMvc.perform(post(ConstantUtil.REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(RegisterUtil.getValidRegisterRequestDto())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(ExceptionMessages.LOGIN_IS_TAKEN.getDescription()));
    }
}
