package com.lucasdt.desafio_picpay.controllers;

import com.lucasdt.desafio_picpay.dtos.ResponseTransactionDTO;
import com.lucasdt.desafio_picpay.dtos.TransactionDTO;
import com.lucasdt.desafio_picpay.entities.Transaction;
import com.lucasdt.desafio_picpay.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    ResponseEntity<Transaction> create(@RequestBody TransactionDTO transaction) throws Exception {
        return ResponseEntity.ok(transactionService.create(transaction));
    }

    @GetMapping
    ResponseEntity<List<ResponseTransactionDTO>> list() {
        return ResponseEntity.ok(transactionService.list());
    }
}
