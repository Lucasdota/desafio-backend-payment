package com.lucasdt.desafio_picpay.dtos;

import com.lucasdt.desafio_picpay.entities.UserType;

import java.math.BigDecimal;

public record UserDTO(String name, Long document, String email, String password, UserType userType, BigDecimal balance) {
}
