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
        return ResponseEntity.ok(userService.create(user));
    }


    @GetMapping
    ResponseEntity<List<User>> list() {
        return ResponseEntity.ok(userService.list());
    }
}
