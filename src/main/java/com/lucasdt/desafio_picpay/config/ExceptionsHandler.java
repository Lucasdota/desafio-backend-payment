package com.lucasdt.desafio_picpay.config;

import com.lucasdt.desafio_picpay.dtos.ExceptionDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ExceptionDTO> treatDuplicateEntry(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body(new ExceptionDTO("User already exists.", 400));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ExceptionDTO> treatGeneralExceptions(Exception e) {
        return ResponseEntity.internalServerError().body(new ExceptionDTO(e.getMessage(), 404));
    }
}
