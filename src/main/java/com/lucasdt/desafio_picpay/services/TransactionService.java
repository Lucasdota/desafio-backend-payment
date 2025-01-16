package com.lucasdt.desafio_picpay.services;

import com.lucasdt.desafio_picpay.dtos.AuthorizationDTO;
import com.lucasdt.desafio_picpay.dtos.ResponseTransactionDTO;
import com.lucasdt.desafio_picpay.dtos.TransactionDTO;
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

    public Transaction create(TransactionDTO transaction) throws Exception {
        User sender = userService.getUserById(transaction.senderId());
        User receiver = userService.getUserById(transaction.receiverId());
        if (sender ==  null || receiver == null) {
            throw new Exception("User not found");
        }
        validateTransaction(transaction.amount(), sender);
        sender.setBalance(sender.getBalance().subtract(transaction.amount()));
        receiver.setBalance(receiver.getBalance().add(transaction.amount()));
        Transaction newTransaction = new Transaction(transaction.amount(), sender, receiver);
        return transactionRepository.save(newTransaction);
    }

    public List<ResponseTransactionDTO> list() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<ResponseTransactionDTO> response = new ArrayList<>();
        for (Transaction transaction : transactions) {
            Long id = transaction.getId();
            BigDecimal amount = transaction.getAmount();
            Long senderId = transaction.getSender().getId();
            Long receiverId = transaction.getReceiver().getId();
            ResponseTransactionDTO responseObj = new ResponseTransactionDTO(id, amount, senderId, receiverId);
            response.add(responseObj);
        }
        return response;
    }

    public void validateTransaction(BigDecimal amount, User sender) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Merchants cannot transfer money");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Not enough balance.");
        }
        AuthorizationDTO authorizationResponse = restTemplate.getForObject("https://util.devi.tools/api/v2/authorize", AuthorizationDTO.class);
        if (authorizationResponse != null && authorizationResponse.status().equalsIgnoreCase("fail")) {
            throw new Exception("Authorization denied.");
        }
    }
}
