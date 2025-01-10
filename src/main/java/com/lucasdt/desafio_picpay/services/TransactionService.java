package com.lucasdt.desafio_picpay.services;

import com.lucasdt.desafio_picpay.dtos.AuthorizationDTO;
import com.lucasdt.desafio_picpay.entities.Transaction;
import com.lucasdt.desafio_picpay.entities.User;
import com.lucasdt.desafio_picpay.entities.UserType;
import com.lucasdt.desafio_picpay.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;

    public Transaction create(BigDecimal amount, Long senderId, Long receiverId) throws Exception {
        Optional<User> sender = Optional.ofNullable(userService.getUserById(senderId));
        Optional<User> receiver = Optional.ofNullable(userService.getUserById(receiverId));
        if (sender.isEmpty() || receiver.isEmpty()) {
            throw new Exception("User not found.");
        }

        validateTransaction(amount, sender.get(), receiver.get());

        Transaction newTransaction = new Transaction(sender.get(), receiver.get(), amount);
        sender.get().setBalance(sender.get().getBalance().subtract(amount));
        receiver.get().setBalance(receiver.get().getBalance().add(amount));
        return transactionRepository.save(newTransaction);
    }

    public void validateTransaction(BigDecimal amount, User sender, User receiver) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Merchants cannot transfer money.");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Not enough funds.");
        }

        AuthorizationDTO authorizationResponse = restTemplate.getForObject("https://util.devi.tools/api/v2/authorize", AuthorizationDTO.class);
        if (authorizationResponse == null) {
            throw new Exception("Authorization service offline.");
        }
        if (authorizationResponse.status().equalsIgnoreCase("fail")) {
            throw new Exception("Authorization denied.");
        }
    }

    public List<Transaction> list() {
        return transactionRepository.findAll();
    }
}
