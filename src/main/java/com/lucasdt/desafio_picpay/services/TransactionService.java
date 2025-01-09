package com.lucasdt.desafio_picpay.services;

import com.lucasdt.desafio_picpay.dtos.AuthorizeDTO;
import com.lucasdt.desafio_picpay.dtos.NotificationDTO;
import com.lucasdt.desafio_picpay.dtos.TransactionDTO;
import com.lucasdt.desafio_picpay.entities.Transaction;
import com.lucasdt.desafio_picpay.entities.User;
import com.lucasdt.desafio_picpay.entities.UserType;
import com.lucasdt.desafio_picpay.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    public Transaction create(TransactionDTO transaction) throws Exception {
        User sender = userService.getUserById(transaction.senderId());
        User receiver = userService.getUserById(transaction.receiverId());

        validateTransaction(sender, receiver, transaction);

        Transaction newTransaction = new Transaction(sender, receiver, transaction.value());
        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));
        transactionRepository.save(newTransaction);
        return newTransaction;
    }

    public void validateTransaction(User sender, User receiver, TransactionDTO transaction) throws Exception {
        if (sender == null || receiver == null) {
            throw new Exception("User not found.");
        }

        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Merchants cannot send money.");
        }
        if (sender.getBalance().compareTo(transaction.value()) < 0) {
            throw new Exception("Insufficient funds.");
        }

        AuthorizeDTO authorizeResponse = restTemplate.getForObject("https://util.devi.tools/api/v2/authorize", AuthorizeDTO.class);
        if (authorizeResponse == null) {
            throw new Exception("Authorization service offline.");
        }
        if (authorizeResponse.status().equalsIgnoreCase("fail")) {
            throw new Exception("Authorization denied.");
        }

        //NotificationDTO notificationResponse = restTemplate.postForObject("https://util.devi.tools/api/v1/notify", sender, NotificationDTO.class);
        //if (notificationResponse == null) {
        //    throw new Exception("Notification service offline.");
        //}
        //if (notificationResponse.status().equalsIgnoreCase("fail")) {
        //    throw new Exception("notification denied.");
        //}
    }

    public List<Transaction> list() {
        return transactionRepository.findAll();
    }
}
