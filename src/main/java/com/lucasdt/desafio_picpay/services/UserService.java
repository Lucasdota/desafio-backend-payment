package com.lucasdt.desafio_picpay.services;

import com.lucasdt.desafio_picpay.dtos.UserDTO;
import com.lucasdt.desafio_picpay.entities.User;
import com.lucasdt.desafio_picpay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> create(UserDTO user) {
        User newUser = new User(user.firstName(), user.lastName(), user.document(), user.email(), user.password(), user.userType(), user.balance());
        userRepository.save(newUser);
        return list();
    }

    public List<User> list() {
        return userRepository.findAll();
    }

}
