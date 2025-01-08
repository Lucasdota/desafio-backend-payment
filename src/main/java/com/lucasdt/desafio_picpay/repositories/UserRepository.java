package com.lucasdt.desafio_picpay.repositories;

import com.lucasdt.desafio_picpay.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
