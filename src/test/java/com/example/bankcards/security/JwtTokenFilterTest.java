package com.example.bankcards.security;

import com.example.bankcards.config.SecurityConfig;
import com.example.bankcards.controller.UserController;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.ConstantUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
)
public class JwtTokenFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private TextEncryptor textEncryptor;

    @Test
    @WithMockUser(roles = {"USER"})
    void getAdminEndpoint_WhenUser_ShouldReturn403() throws Exception {
        mockMvc.perform(get(ConstantUtil.ADMIN_USERS_CONTROLLER_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAdminEndpoint_WhenAdmin_ShouldReturn200() throws Exception {
        mockMvc.perform(get(ConstantUtil.ADMIN_USERS_CONTROLLER_URL))
                .andExpect(status().isOk());
    }
}
