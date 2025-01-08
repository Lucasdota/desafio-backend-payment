package com.lucasdt.desafio_picpay.services;

import com.lucasdt.desafio_picpay.entities.User;
import com.lucasdt.desafio_picpay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> create(User user) {
        userRepository.save(user);
        return list();
    }

    public List<User> list() {
        return userRepository.findAll();
    }

}
