package com.lucasdt.desafio_picpay.config;

import com.lucasdt.desafio_picpay.dtos.ExceptionDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDTO> treatDuplicateEntry(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body(new ExceptionDTO("User already exists!", 500));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ExceptionDTO> treatAuthorizationDenial(HttpClientErrorException e) {
        return ResponseEntity.badRequest().body(new ExceptionDTO("Authorization denied!", 500));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> treatGeneralExceptions(Exception e) {
        return ResponseEntity.internalServerError().body(new ExceptionDTO(e.getMessage(), 400));
    }
}
