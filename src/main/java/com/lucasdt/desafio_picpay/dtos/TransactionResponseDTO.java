package com.lucasdt.desafio_picpay.dtos;

import java.math.BigDecimal;

public record TransactionResponseDTO(Long id, Long senderId, Long receiverId, BigDecimal amount) {
}
