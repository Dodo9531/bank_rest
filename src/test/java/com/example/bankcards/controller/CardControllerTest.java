package com.example.bankcards.controller;

import com.example.bankcards.dto.request.CardCreateRequestDto;
import com.example.bankcards.dto.request.CardTransferRequestDto;
import com.example.bankcards.dto.response.CardInfoResponseDto;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.security.JwtAuthenticationFilter;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardUtil;
import com.example.bankcards.util.ConstantUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.security.Principal;
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

@WebMvcTest(CardController.class)
@AutoConfigureMockMvc(addFilters = false)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CardService cardService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /* ======================================================
                              ADMIN
         ====================================================== */
    @Test
    void createCard_ShouldReturn201() throws Exception {
        CardCreateRequestDto requestDto = CardUtil.getValidCardCreateRequestDto();
        CardInfoResponseDto responseDto = CardUtil.getValidCardInfoResponseDto();

        Mockito.when(cardService.createCard(any(CardCreateRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/admin/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ConstantUtil.VALID_CARD_ID_1.toString()))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.balance").value(300));
    }

    @Test
    void getAllCards_ShouldReturn200AndPage() throws Exception {
        CardInfoResponseDto card1 = CardUtil.getValidCardInfoResponseDto();
        CardInfoResponseDto card2 = CardUtil.getSecondValidCardInfoResponseDto();

        Mockito.when(cardService.getAllCards(
                ArgumentMatchers.any(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of(card1, card2)));

        mockMvc.perform(get("/api/admin/cards")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(ConstantUtil.VALID_CARD_ID_1.toString()))
                .andExpect(jsonPath("$.content[1].id").value(ConstantUtil.VALID_CARD_ID_2.toString()));
    }

    @Test
    void changeCardStatus_ShouldReturn200() throws Exception {
        UUID cardId = ConstantUtil.VALID_CARD_ID_1;

        Mockito.when(cardService.updateCardStatus(cardId, CardStatus.BLOCKED))
                .thenReturn("Статус карты изменён");

        mockMvc.perform(patch("/api/admin/cards/{cardId}/status", cardId)
                        .param("status", "BLOCKED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Статус карты изменён"));
    }

    @Test
    void deleteCard_ShouldReturn200() throws Exception {
        UUID cardId = ConstantUtil.VALID_CARD_ID_1;

        Mockito.when(cardService.deleteCard(cardId))
                .thenReturn("Карта была удалена");

        mockMvc.perform(delete("/api/admin/cards/{cardId}", cardId))
                .andExpect(status().isOk())
                .andExpect(content().string("Карта была удалена"));
    }

    /* ======================================================
                            USER
       ====================================================== */

    @Test
    void getUserCards_ShouldReturn200AndPage() throws Exception {
        CardInfoResponseDto card = CardUtil.getValidCardInfoResponseDto();

        Mockito.when(cardService.getUserCards(eq(ConstantUtil.VALID_USER_ID_1), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(card)));

        Principal mockPrincipal = () -> ConstantUtil.VALID_USER_ID_1.toString();

        mockMvc.perform(get("/api/user/cards")
                        .param("page", "0")
                        .param("size", "1")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id")
                        .value(ConstantUtil.VALID_CARD_ID_1.toString()));
    }

    @Test
    void blockOwnCard_ShouldReturn200() throws Exception {
        Mockito.when(cardService.blockOwnedCard(
                        ConstantUtil.VALID_CARD_ID_1,
                        ConstantUtil.VALID_USER_ID_1))
                .thenReturn("Карта была заблокирована");

        Principal mockPrincipal = () -> ConstantUtil.VALID_USER_ID_1.toString();

        mockMvc.perform(patch("/api/user/cards/{cardId}/block",
                        ConstantUtil.VALID_CARD_ID_1)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(content().string("Карта была заблокирована"));
    }

    @Test
    void transfer_ShouldReturn200() throws Exception {
        CardTransferRequestDto requestDto = CardUtil.getValidTransferRequestDto();

        Mockito.when(cardService.transfer(any(CardTransferRequestDto.class)))
                .thenReturn("Перевод прошёл успешно");

        mockMvc.perform(post("/api/user/cards/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Перевод прошёл успешно"));
    }

    @Test
    void getBalance_ShouldReturn200() throws Exception {
        Mockito.when(cardService.getCardBalance(
                        ConstantUtil.VALID_CARD_ID_1,
                        ConstantUtil.VALID_USER_ID_1))
                .thenReturn(BigDecimal.valueOf(300));

        Principal mockPrincipal = () -> ConstantUtil.VALID_USER_ID_1.toString();

        mockMvc.perform(get("/api/user/cards/{cardId}/balance",
                        ConstantUtil.VALID_CARD_ID_1)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(content().string("300"));
    }
}
