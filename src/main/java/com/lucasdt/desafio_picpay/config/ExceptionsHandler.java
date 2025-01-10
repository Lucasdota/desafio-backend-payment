package com.lucasdt.desafio_picpay.config;

import com.lucasdt.desafio_picpay.dtos.ExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDTO> treatDuplicateEntry() {
        return ResponseEntity.badRequest().body(new ExceptionDTO("User already exists", 400));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ExceptionDTO> treatAuthorizationError() {
        return ResponseEntity.internalServerError().body(new ExceptionDTO("Authorization service offline", 403));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> treatGeneralException(Exception exception) {
        return ResponseEntity.internalServerError().body(new ExceptionDTO(exception.getMessage(), 500));
    }
}
