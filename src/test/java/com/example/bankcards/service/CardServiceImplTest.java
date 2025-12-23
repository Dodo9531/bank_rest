package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardCreateRequestDto;
import com.example.bankcards.dto.response.CardInfoResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.CardNotOwnedException;
import com.example.bankcards.exception.InsufficientBalanceException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.exception.SameCardTransferException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.impl.CardServiceImpl;
import com.example.bankcards.util.CardUtil;
import com.example.bankcards.util.ConstantUtil;
import com.example.bankcards.util.mapper.CardMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    @Mock
    private TextEncryptor textEncryptor;

    @InjectMocks
    private CardServiceImpl cardService;


    @Test
    void createCard_success() {
        CardCreateRequestDto requestDto = mock(CardCreateRequestDto.class);
        CardInfoResponseDto responseDto = mock(CardInfoResponseDto.class);
        Card card = Card.builder().id(ConstantUtil.VALID_CARD_ID_1).build();

        when(cardMapper.toCard(requestDto)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(card);
        when(cardMapper.toDto(card)).thenReturn(responseDto);

        CardInfoResponseDto result = cardService.createCard(requestDto);

        assertEquals(responseDto, result);
        verify(cardRepository).save(card);
    }

    @Test
    void blockCard_cardNotFound() {
        when(cardRepository.findById(ConstantUtil.VALID_CARD_ID_1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cardService.updateCardStatus(ConstantUtil.VALID_CARD_ID_1, CardStatus.BLOCKED));
    }

    @Test
    void blockCard_success() {
        Card card = Card.builder().status(CardStatus.ACTIVE).build();
        when(cardRepository.findById(ConstantUtil.VALID_CARD_ID_1)).thenReturn(Optional.of(card));

        String result = cardService.updateCardStatus(ConstantUtil.VALID_CARD_ID_1, CardStatus.BLOCKED);

        assertEquals(CardStatus.BLOCKED, card.getStatus());
        verify(cardRepository).save(card);
    }

    @Test
    void activateCard_success() {
        Card card = Card.builder().status(CardStatus.BLOCKED).build();
        when(cardRepository.findById(ConstantUtil.VALID_CARD_ID_1)).thenReturn(Optional.of(card));

        String result = cardService.updateCardStatus(ConstantUtil.VALID_CARD_ID_1, CardStatus.ACTIVE);

        assertEquals(CardStatus.ACTIVE, card.getStatus());
        verify(cardRepository).save(card);
    }

    @Test
    void deleteCard_success() {
        String result = cardService.deleteCard(ConstantUtil.VALID_CARD_ID_1);

        assertEquals("Карта была удалена", result);
        verify(cardRepository).deleteById(ConstantUtil.VALID_CARD_ID_1);
    }

    @Test
    void getAllCards_withOwnerIdAndStatus_returnsFilteredCards() {
        UUID ownerId = ConstantUtil.VALID_USER_ID_1;
        CardStatus status = CardStatus.ACTIVE;
        PageRequest pageable = PageRequest.of(0, 10);

        Card card1 = CardUtil.getValidCard1();
        Card card2 = CardUtil.getValidCard2();
        card1.setOwnerId(ownerId);
        card1.setStatus(status);
        card2.setOwnerId(ownerId);
        card2.setStatus(status);

        List<Card> cards = List.of(card1, card2);
        Page<Card> page = new PageImpl<>(cards, pageable, cards.size());

        when(cardRepository.findAll((Specification<Card>) any(), eq(pageable)))
                .thenReturn(page);

        Page<CardInfoResponseDto> result = cardService.getAllCards(ownerId, "ACTIVE", pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(ownerId, result.getContent().get(0).ownerId());
        assertEquals(status, result.getContent().get(0).status());

        verify(cardRepository, times(1)).findAll((Specification<Card>) any(), eq(pageable));
    }


    @Test
    void getUserCards_success() {
        Pageable pageable = Pageable.unpaged();
        Card card = Card.builder().ownerId(ConstantUtil.VALID_USER_ID_1).build();

        when(cardRepository.findAllByOwnerId(ConstantUtil.VALID_USER_ID_1, pageable))
                .thenReturn(new PageImpl<>(List.of(card)));

        Page<CardInfoResponseDto> result = cardService.getUserCards(ConstantUtil.VALID_USER_ID_1, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void blockOwnedCard_notOwned() {
        Card card = CardUtil.getValidCard1WithRandomUser();

        when(cardRepository.findById(ConstantUtil.VALID_CARD_ID_1)).thenReturn(Optional.of(card));

        assertThrows(
                CardNotOwnedException.class,
                () -> cardService.blockOwnedCard(ConstantUtil.VALID_CARD_ID_1, ConstantUtil.VALID_USER_ID_1)
        );
    }

    @Test
    void blockOwnedCard_success() {
        Card card = CardUtil.getValidCard1();
        when(cardRepository.findById(ConstantUtil.VALID_CARD_ID_1)).thenReturn(Optional.of(card));

        String result = cardService.blockOwnedCard(ConstantUtil.VALID_CARD_ID_1, ConstantUtil.VALID_USER_ID_1);

        assertEquals(CardStatus.BLOCKED, card.getStatus());
        assertEquals("Карта была заблокирована по вашему запросу", result);
    }

    @Test
    void transfer_SameCardTransfer() {
        Card from = CardUtil.getValidCard1();

        when(cardRepository.findById(ConstantUtil.VALID_CARD_ID_1)).thenReturn(Optional.of(from));

        assertThrows(
                SameCardTransferException.class,
                () -> cardService.transfer(
                        CardUtil.getTransferRequestDtoWithSameCardOnToAndFromFields()
                )
        );

        verify(cardRepository, never()).save(any());
    }

    @Test
    void transfer_InsufficientBalance() {
        Card from = CardUtil.getValidCard1();

        Card to = CardUtil.getValidCard2();

        when(cardRepository.findById(ConstantUtil.VALID_CARD_ID_1)).thenReturn(Optional.of(from));
        when(cardRepository.findById(ConstantUtil.VALID_CARD_ID_2)).thenReturn(Optional.of(to));

        assertThrows(
                InsufficientBalanceException.class,
                () -> cardService.transfer(
                        CardUtil.getTransferRequestDtoWith99999Balance()
                )
        );

        verify(cardRepository, never()).save(any());
    }

    @Test
    void transfer_success() {
        Card from = CardUtil.getValidCard1();
        Card to = CardUtil.getValidCard2();
        when(cardRepository.findById(ConstantUtil.VALID_CARD_ID_1)).thenReturn(Optional.of(from));
        when(cardRepository.findById(ConstantUtil.VALID_CARD_ID_2)).thenReturn(Optional.of(to));

        String result = cardService.transfer(CardUtil.getValidTransferRequestDto());

        assertEquals(BigDecimal.valueOf(100), from.getBalance());
        assertEquals(BigDecimal.valueOf(1200), to.getBalance());
        assertEquals("Перевод прошёл успешно", result);

        verify(cardRepository).save(from);
        verify(cardRepository).save(to);
    }

}