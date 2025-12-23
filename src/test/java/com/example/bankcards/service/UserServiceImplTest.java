package com.example.bankcards.service;

import com.example.bankcards.dto.request.UserCreateRequestDto;
import com.example.bankcards.dto.request.UserUpdateRequestDto;
import com.example.bankcards.dto.response.UserInfoResponseDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.UserServiceImpl;
import com.example.bankcards.util.ConstantUtil;
import com.example.bankcards.util.RoleUtil;
import com.example.bankcards.util.UserUtil;
import com.example.bankcards.util.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final UUID userId = ConstantUtil.VALID_USER_ID_1;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TextEncryptor textEncryptor;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_success() {
        UserCreateRequestDto request = UserUtil.getValidUserCreateDto();
        User user = UserUtil.getValidUser;
        UserInfoResponseDto dto = UserUtil.getValidUserRoleUserInfoResponseDto();

        when(roleRepository.findByNameIn(request.roles())).thenReturn(Set.of(RoleUtil.getRoleUser()));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserInfoResponseDto result = userService.createUser(request);

        assertEquals(dto, result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_userNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void getUserById_success() {
        User user = UserUtil.getValidUser;
        UserInfoResponseDto dto = UserUtil.getValidUserRoleUserInfoResponseDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        UserInfoResponseDto result = userService.getUserById(userId);

        assertEquals(dto, result);
    }

    @Test
    void getAllUsers_success() {
        Pageable pageable = Pageable.unpaged();
        User user = UserUtil.getValidUser;
        UserInfoResponseDto dto = UserUtil.getValidUserRoleUserInfoResponseDto();

        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toDto(user)).thenReturn(dto);

        Page<UserInfoResponseDto> result = userService.getAllUsers(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().get(0));
    }

    @Test
    void updateUser_userNotFound() {

        UserUpdateRequestDto request = UserUtil.getInvalidUserUpdateRequestDto();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(userId, request));
    }

    @Test
    void updateUser_success() {

        User user = UserUtil.getValidUser;
        UserUpdateRequestDto request = UserUtil.getValidUserUpdateRequestDto();
        UserInfoResponseDto dto = UserUtil.getValidUserRoleUserInfoResponseDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByNameIn(request.roles())).thenReturn(Set.of(RoleUtil.getRoleAdmin()));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserInfoResponseDto result = userService.updateUser(userId, request);

        assertEquals(dto, result);
        assertEquals(ConstantUtil.VALID_USERNAME, user.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser_success() {
        UUID userId = ConstantUtil.VALID_USER_ID_1;

        String result = userService.deleteUser(userId);

        assertEquals("Пользователь был успешно удалён", result);
        verify(userRepository).deleteById(userId);
    }
}
