package com.example.bankcards.controller;

import com.example.bankcards.dto.request.UserCreateRequestDto;
import com.example.bankcards.dto.request.UserUpdateRequestDto;
import com.example.bankcards.dto.response.UserInfoResponseDto;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.security.JwtAuthenticationFilter;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.ConstantUtil;
import com.example.bankcards.util.ExceptionMessages;
import com.example.bankcards.util.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    private final UUID userId = ConstantUtil.VALID_USER_ID_1;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void createUser_ShouldReturn201AndFullUser() throws Exception {
        UserCreateRequestDto dto = UserUtil.getValidUserCreateDto();
        UserInfoResponseDto responseDto = UserUtil.getValidUserRoleUserInfoResponseDto();

        Mockito.when(userService.createUser(any(UserCreateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post(ConstantUtil.ADMIN_USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(ConstantUtil.VALID_USERNAME))
                .andExpect(jsonPath("$.firstName").value(ConstantUtil.VALID_USER_FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(ConstantUtil.VALID_USER_LASTNAME))
                .andExpect(jsonPath("$.roles[0]").value(ConstantUtil.ROLE_USER));
    }

    @Test
    void createUser_InvalidInput_ShouldReturn400() throws Exception {
        UserCreateRequestDto dto = UserUtil.getInvalidUserCreateRequestDto();

        mockMvc.perform(post(ConstantUtil.ADMIN_USERS_CONTROLLER_URL)

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserById_ShouldReturn200AndFullUser() throws Exception {
        UserInfoResponseDto responseDto = UserUtil.getValidUserRoleUserInfoResponseDto();

        Mockito.when(userService.getUserById(eq(ConstantUtil.VALID_USER_ID_1))).thenReturn(responseDto);

        mockMvc.perform(get(ConstantUtil.ADMIN_USERS_CONTROLLER_URL + "/{userId}", ConstantUtil.VALID_USER_ID_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(ConstantUtil.VALID_USERNAME))
                .andExpect(jsonPath("$.firstName").value(ConstantUtil.VALID_USER_FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(ConstantUtil.VALID_USER_LASTNAME))
                .andExpect(jsonPath("$.roles[0]").value(ConstantUtil.ROLE_USER));
    }

    @Test
    void getUserById_NotFound_ShouldReturn404() throws Exception {
        Mockito.when(userService.getUserById(eq(userId))).thenThrow(
                new NotFoundException(ExceptionMessages.USER_NOT_FOUND_BY_ID.getDescription()));

        mockMvc.perform(get(ConstantUtil.ADMIN_USERS_CONTROLLER_URL + "/{userId}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsers_ShouldReturn200AndFullPage() throws Exception {
        UserInfoResponseDto user1 = UserUtil.getValidUserRoleUserInfoResponseDto();
        UserInfoResponseDto user2 = UserUtil.getValidAdminRoleUserInfoResponseDto();

        Mockito.when(userService.getAllUsers(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(user1, user2)));

        mockMvc.perform(get(ConstantUtil.ADMIN_USERS_CONTROLLER_URL)
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].username").value(ConstantUtil.VALID_USERNAME))
                .andExpect(jsonPath("$.content[0].roles[0]").value(ConstantUtil.ROLE_USER))
                .andExpect(jsonPath("$.content[1].username").value(ConstantUtil.VALID_USERNAME_2))
                .andExpect(jsonPath("$.content[1].roles[0]").value(ConstantUtil.ROLE_ADMIN));
    }

    @Test
    void updateUser_ShouldReturn200AndUpdatedUser() throws Exception {
        UUID userId = UUID.randomUUID();
        UserUpdateRequestDto requestDto = UserUtil.getValidUserUpdateRequestDto();
        UserInfoResponseDto responseDto = UserUtil.getValidAdminRoleUserInfoResponseDto();

        Mockito.when(userService.updateUser(eq(userId), any(UserUpdateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(patch(ConstantUtil.ADMIN_USERS_CONTROLLER_URL + "/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(ConstantUtil.VALID_USERNAME_2))
                .andExpect(jsonPath("$.firstName").value(ConstantUtil.VALID_USER_FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(ConstantUtil.VALID_USER_LASTNAME))
                .andExpect(jsonPath("$.roles[0]").value(ConstantUtil.ROLE_ADMIN));
    }

    @Test
    void deleteUser_success() throws Exception {
        UUID userId = ConstantUtil.VALID_USER_ID_1;
        Mockito.when(userService.deleteUser(userId))
                .thenReturn("Пользователь был успешно удалён");

        mockMvc.perform(delete("/api/admin/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь был успешно удалён"));
    }

    @Test
    void deleteUser_failure() throws Exception {
        UUID userId = ConstantUtil.VALID_USER_ID_1;
        Mockito.when(userService.deleteUser(userId))
                .thenReturn("Не удалось удалить пользователя");

        mockMvc.perform(delete("/api/admin/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("Не удалось удалить пользователя"));
    }
}
