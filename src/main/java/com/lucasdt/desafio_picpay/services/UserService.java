package com.lucasdt.desafio_picpay.services;

import com.lucasdt.desafio_picpay.dtos.UserDTO;
import com.lucasdt.desafio_picpay.entities.User;
import com.lucasdt.desafio_picpay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public User getUserById(Long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(() -> new Exception("User not found with id: " + id));
    }

}
