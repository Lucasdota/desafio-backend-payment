package com.lucasdt.desafio_picpay.dtos;

import com.lucasdt.desafio_picpay.entities.User;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal amount, Long senderId, Long receiverId) {
}
