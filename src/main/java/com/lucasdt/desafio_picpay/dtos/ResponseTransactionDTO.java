package com.lucasdt.desafio_picpay.dtos;

import java.math.BigDecimal;

public record ResponseTransactionDTO(Long id, BigDecimal amount, Long senderId, Long receiverId) {
}
