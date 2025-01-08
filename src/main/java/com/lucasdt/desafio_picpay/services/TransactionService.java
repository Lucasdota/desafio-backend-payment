package com.lucasdt.desafio_picpay.services;

import com.lucasdt.desafio_picpay.dtos.StatusDTO;
import com.lucasdt.desafio_picpay.dtos.TransactionDTO;
import com.lucasdt.desafio_picpay.entities.Transaction;
import com.lucasdt.desafio_picpay.entities.User;
import com.lucasdt.desafio_picpay.entities.UserType;
import com.lucasdt.desafio_picpay.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;

    public Transaction create(TransactionDTO transaction) throws Exception {
        User sender = userService.getUserById(transaction.senderId());
        User receiver = userService.getUserById(transaction.receiverId());

        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Merchants cannot transfer money.");
        }

        if (sender.getBalance().compareTo(transaction.amount()) < 0) {
            throw new Exception("Insufficient balance.");
        }

        StatusDTO response = restTemplate.getForObject("https://util.devi.tools/api/v2/authorize", StatusDTO.class);

        if (response == null) throw new Exception("API offline.");
        if (response.status().equalsIgnoreCase("fail")) {
            throw new Exception("Authorization denied.");
        }

        Transaction newTransaction = new Transaction(transaction.amount(), sender, receiver);
        sender.setBalance(sender.getBalance().subtract(transaction.amount()));
        receiver.setBalance(receiver.getBalance().add(transaction.amount()));
        return newTransaction;
    }
}
