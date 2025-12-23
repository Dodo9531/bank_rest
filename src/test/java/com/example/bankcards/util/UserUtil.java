package com.example.bankcards.util;

import com.example.bankcards.dto.request.UserCreateRequestDto;
import com.example.bankcards.dto.request.UserUpdateRequestDto;
import com.example.bankcards.dto.response.UserInfoResponseDto;
import com.example.bankcards.entity.User;
import lombok.experimental.UtilityClass;

import java.util.Set;

/**
 * Класс для быстрого получения объектов пользователя и DTO связанных с ним, используется в тестовых классах
 */

@UtilityClass
public class UserUtil {

    public User getValidUser = User.builder()
            .id(ConstantUtil.VALID_USER_ID_1)
            .username(ConstantUtil.VALID_USERNAME)
            .password(ConstantUtil.VALID_PASSWORD)
            .firstName(ConstantUtil.VALID_USER_FIRSTNAME)
            .lastName(ConstantUtil.VALID_USER_LASTNAME)
            .roles(Set.of(RoleUtil.getRoleUser()))
            .build();

    public UserCreateRequestDto getValidUserCreateDto() {
        return UserCreateRequestDto.builder()
                .username(ConstantUtil.VALID_USERNAME)
                .firstName(ConstantUtil.VALID_USER_FIRSTNAME)
                .lastName(ConstantUtil.VALID_USER_LASTNAME)
                .password(ConstantUtil.VALID_PASSWORD)
                .roles(RoleUtil.getRoleAdminSetWithString()).build();
    }

    public UserCreateRequestDto getInvalidUserCreateRequestDto() {
        return UserCreateRequestDto.builder()
                .username("")
                .firstName("")
                .lastName("")
                .password("")
                .roles(RoleUtil.getRoleUserSetWithString()).build();
    }

    public UserUpdateRequestDto getInvalidUserUpdateRequestDto() {
        return UserUpdateRequestDto.builder()
                .firstName("")
                .username("")
                .lastName("")
                .password(ConstantUtil.VALID_PASSWORD)
                .roles(RoleUtil.getRoleUserSetWithString()).build();
    }

    public UserUpdateRequestDto getValidUserUpdateRequestDto() {
        return UserUpdateRequestDto.builder()
                .firstName(ConstantUtil.VALID_USER_FIRSTNAME)
                .username(ConstantUtil.VALID_USERNAME)
                .lastName(ConstantUtil.VALID_USER_LASTNAME)
                .password(ConstantUtil.VALID_PASSWORD)
                .roles(RoleUtil.getRoleUserSetWithString()).build();
    }

    public UserInfoResponseDto getValidAdminRoleUserInfoResponseDto() {
        return UserInfoResponseDto.builder()
                .username(ConstantUtil.VALID_USERNAME_2)
                .firstName(ConstantUtil.VALID_USER_FIRSTNAME)
                .lastName(ConstantUtil.VALID_USER_LASTNAME)
                .roles(RoleUtil.getRoleAdminSetWithString()).build();
    }

    public UserInfoResponseDto getValidUserRoleUserInfoResponseDto() {
        return UserInfoResponseDto.builder()
                .username(ConstantUtil.VALID_USERNAME)
                .firstName(ConstantUtil.VALID_USER_FIRSTNAME)
                .lastName(ConstantUtil.VALID_USER_LASTNAME)
                .roles(RoleUtil.getRoleUserSetWithString()).build();
    }

}
