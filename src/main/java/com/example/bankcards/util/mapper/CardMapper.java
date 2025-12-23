package com.example.bankcards.util.mapper;

import com.example.bankcards.dto.request.CardCreateRequestDto;
import com.example.bankcards.dto.response.CardInfoResponseDto;
import com.example.bankcards.entity.Card;
import org.mapstruct.Mapper;

/**
 * Mapper для преобразования сущности Card в DTO CardInfoResponseDto
 */
@Mapper(componentModel = "spring")
public interface CardMapper {

    CardInfoResponseDto toDto(Card card);

    Card toCard(CardCreateRequestDto cardCreateRequestDto);
}