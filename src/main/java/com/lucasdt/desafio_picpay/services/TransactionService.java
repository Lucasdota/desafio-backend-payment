package com.lucasdt.desafio_picpay.services;

import com.lucasdt.desafio_picpay.dtos.AuthorizationDTO;
import com.lucasdt.desafio_picpay.dtos.TransactionDTO;
import com.lucasdt.desafio_picpay.dtos.TransactionResponseDTO;
import com.lucasdt.desafio_picpay.entities.Transaction;
import com.lucasdt.desafio_picpay.entities.User;
import com.lucasdt.desafio_picpay.entities.UserType;
import com.lucasdt.desafio_picpay.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;

    public Transaction create(TransactionDTO data) throws Exception {
        User sender = userService.getUserById(data.senderId());
        User receiver = userService.getUserById(data.receiverId());
        if (sender == null || receiver == null) {
            throw new Exception("User already exists!");
        }

        validateTransaction(data.amount(), sender);
        sender.setBalance(sender.getBalance().subtract(data.amount()));
        receiver.setBalance(receiver.getBalance().add(data.amount()));
        Transaction transaction = new Transaction(data.amount(), sender, receiver);
        return transactionRepository.save(transaction);
    }

    public List<TransactionResponseDTO> list() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionResponseDTO> response = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionResponseDTO data = new TransactionResponseDTO(transaction.getId(), transaction.getSender().getId(), transaction.getReceiver().getId(), transaction.getAmount());
            response.add(data);
        }
        return response;
    }

    private void validateTransaction(BigDecimal amount, User sender) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Merchants cannot transfer money.");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Not enough balance.");
        }
        AuthorizationDTO authorizationResponse = restTemplate.getForObject("https://util.devi.tools/api/v2/authorize", AuthorizationDTO.class);
        if (authorizationResponse == null || authorizationResponse.status().equalsIgnoreCase("fail")) {
            throw new Exception("Authorization denied!");
        }
    }
}
