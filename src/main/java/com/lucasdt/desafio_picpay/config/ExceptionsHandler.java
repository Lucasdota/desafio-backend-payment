package com.lucasdt.desafio_picpay.config;

import com.lucasdt.desafio_picpay.dtos.ExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDTO> treatDuplicateEntry(DataIntegrityViolationException exception) {
        return ResponseEntity.badRequest().body(new ExceptionDTO("User already exists with this document/email.", 400));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity treatNotFound(EntityNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ExceptionDTO> treatAuthorizationForbidden(HttpClientErrorException exception) {
        return ResponseEntity.internalServerError().body(new ExceptionDTO("Authorization denied.", 403));
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ExceptionDTO> treatAuthorizationForbidden(HttpServerErrorException exception) {
        return ResponseEntity.internalServerError().body(new ExceptionDTO("Notification timeout.", 504));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> treatGeneralException(Exception exception) {
        return ResponseEntity.internalServerError().body(new ExceptionDTO(exception.getMessage(), 500));
    }
}
