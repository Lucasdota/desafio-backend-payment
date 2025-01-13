package com.lucasdt.desafio_picpay.services;

import com.lucasdt.desafio_picpay.dtos.AuthorizationDTO;
import com.lucasdt.desafio_picpay.dtos.TransactionDTO;
import com.lucasdt.desafio_picpay.entities.Transaction;
import com.lucasdt.desafio_picpay.entities.User;
import com.lucasdt.desafio_picpay.entities.UserType;
import com.lucasdt.desafio_picpay.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public Transaction create(TransactionDTO data) throws Exception {
        User sender = userService.getUserById(data.senderId());
        User receiver = userService.getUserById(data.receiverId());
        if (sender == null || receiver == null) {
            throw new Exception("Entity not found!");
        }

        validateTransaction(data.amount(), sender, receiver);

        sender.setBalance(sender.getBalance().subtract(data.amount()));
        receiver.setBalance(receiver.getBalance().add(data.amount()));
        Transaction transaction = new Transaction(data.amount(), sender, receiver);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> list() {
        return transactionRepository.findAll();
    }

    public void validateTransaction(BigDecimal amount, User sender, User receiver) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Merchants cannot transfer money.");
        }
        if (sender.getBalance().compareTo(amount) > 0) {
            throw new Exception("Not enough balance.");
        }

        AuthorizationDTO authorizationResponse = restTemplate.getForObject("https://util.devi.tools/api/v2/authorize", AuthorizationDTO.class);
        if (authorizationResponse != null && authorizationResponse.status().equalsIgnoreCase("fail")) {
            throw new Exception("Authorization denied.");
        }
    }
}
