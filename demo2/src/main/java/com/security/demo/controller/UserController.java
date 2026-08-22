package com.security.demo.controller;

import com.security.demo.model.Customer;
import com.security.demo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        customer.setPwd(passwordEncoder.encode(customer.getPwd()));
        Customer savedUser = customerRepository.save(customer);
        if(savedUser.getId() > 0) {
            return ResponseEntity.status(201).body("User registered successfully");
        } else {
            return ResponseEntity.status(400).body("User registration failed");
        }
    }
    @GetMapping("/allusers")
    public ResponseEntity<List<String>> getAllUsers() {
        Iterable<Customer> users = customerRepository.findAll();
        List<String> userEmails = StreamSupport
                .stream(users.spliterator(), false)
                .map(Customer::getEmail)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userEmails);
    }

}
