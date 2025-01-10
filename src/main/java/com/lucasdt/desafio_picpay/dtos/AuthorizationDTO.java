package com.lucasdt.desafio_picpay.dtos;

import java.util.Map;

public record AuthorizationDTO(String status, Map<String, Object> data) {
}
