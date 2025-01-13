package com.lucasdt.desafio_picpay.controllers;

import com.lucasdt.desafio_picpay.dtos.UserDTO;
import com.lucasdt.desafio_picpay.entities.User;
import com.lucasdt.desafio_picpay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    ResponseEntity<User> create(@RequestBody UserDTO user) {
        User newUser = new User(user.name(), user.document(), user.email(), user.password(), user.userType(), user.balance());
        return ResponseEntity.ok(userService.create(newUser));
    }

    @GetMapping
    ResponseEntity<List<User>> list() {
        return ResponseEntity.ok(userService.list());
    }
}
