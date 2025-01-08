package com.lucasdt.desafio_picpay.services;

import com.lucasdt.desafio_picpay.dtos.NotificationStatusDTO;
import com.lucasdt.desafio_picpay.dtos.StatusDTO;
import com.lucasdt.desafio_picpay.dtos.TransactionDTO;
import com.lucasdt.desafio_picpay.entities.Transaction;
import com.lucasdt.desafio_picpay.entities.User;
import com.lucasdt.desafio_picpay.entities.UserType;
import com.lucasdt.desafio_picpay.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Transactional
    public Transaction create(TransactionDTO transaction) throws Exception {
        User sender = userService.getUserById(transaction.senderId());
        User receiver = userService.getUserById(transaction.receiverId());

        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Merchants cannot transfer money.");
        }

        if (sender.getBalance().compareTo(transaction.amount()) < 0) {
            throw new Exception("Insufficient balance.");
        }

        // Authorization check
        ResponseEntity<StatusDTO> authorizationResponseEntity = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", StatusDTO.class);
        StatusDTO authorizationResponse = authorizationResponseEntity.getBody();

        if (authorizationResponse == null) {
            throw new Exception("Authorization service offline.");
        }
        if ("fail".equalsIgnoreCase(authorizationResponse.status())) {
            throw new Exception("Authorization denied.");
        }

        ResponseEntity<NotificationStatusDTO> notificationResponseEntity = restTemplate.postForEntity("https://util.devi.tools/api/v1/notify", sender, NotificationStatusDTO.class);
        NotificationStatusDTO notificationResponse = notificationResponseEntity.getBody();

        if (notificationResponse == null) {
            throw new Exception("Notification service offline.");
        }
        if ("fail".equalsIgnoreCase(notificationResponse.status())) {
            throw new Exception("Notification denied.");
        }

        Transaction newTransaction = new Transaction(transaction.amount(), sender, receiver);
        sender.setBalance(sender.getBalance().subtract(transaction.amount()));
        receiver.setBalance(receiver.getBalance().add(transaction.amount()));
        transactionRepository.save(newTransaction);
        return newTransaction;
    }
}
