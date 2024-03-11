package com.devrezaur.main.controller;



import com.devrezaur.main.UserNotFoundException;
import com.devrezaur.main.model.User2;
import com.devrezaur.main.repository.UserRepository2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/users")

public class UserController2 {

    @Autowired

    private UserRepository2 userRepository;

    @GetMapping

    public List<User2> getAllUsers() {

        return userRepository.findAll();

    }

    @GetMapping("/{id}")

    public User2 getUserById(@PathVariable Long id) {

        return userRepository.findById(id)

                .orElse(null); // Return null if user not found

    }

    @DeleteMapping("/{id}")

    public String deleteUser(@PathVariable Long id) {

        userRepository.deleteById(id);

        return "User with ID " + id + " deleted successfully";

    }

    @PostMapping

    public String createUser(@RequestBody User2 user) {

        userRepository.save(user);

        return "User created successfully";

    }

    @PutMapping("/{id}")

    public String updateUser(@PathVariable Long id, @RequestBody User2 userDetails) {

        User2 user = userRepository.findById(id)

                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setFirstName(userDetails.getFirstName());

        user.setLastName(userDetails.getLastName());

        user.setEmail(userDetails.getEmail());

        userRepository.save(user);

        return "User updated successfully";

    }

}