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

    public User create(UserDTO data) {
        User user = new User(data.name(), data.document(), data.email(), data.password(), data.userType(), data.balance());
        return userRepository.save(user);
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
}
