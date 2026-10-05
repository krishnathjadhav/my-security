package com.security.demo.controller;

import com.security.demo.model.Accounts;
import com.security.demo.repository.AccountsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    AccountsRepository accountsRepository;
    @GetMapping("/myAccount")
    public Accounts getAccountDetails (@RequestParam long id) {
        Accounts accounts = accountsRepository.findByCustomerId(id);
        if (accounts != null) {
            return accounts;
        } else {
            return null;
        }
    }

}
