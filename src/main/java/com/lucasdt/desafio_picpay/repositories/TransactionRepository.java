package com.lucasdt.desafio_picpay.repositories;

import com.lucasdt.desafio_picpay.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
