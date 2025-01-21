package com.lucasdt.desafio_picpay.dtos;

import java.math.BigDecimal;

public record TransactionResponse(Long id, Long userId, Long receiverId, BigDecimal amount) {
}
