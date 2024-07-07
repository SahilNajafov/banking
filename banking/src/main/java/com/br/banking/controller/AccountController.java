package com.br.banking.controller;

import com.br.banking.dto.AccountDto;
import com.br.banking.service.AccountService;
import com.br.banking.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AccountController {

    UserService userService;
    AccountService accountService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody @Valid AccountDto accountDto) {
        return accountService.createAccount(accountDto);
    }

    @DeleteMapping("/delete/{num}")
    public String delete(@PathVariable("num") Long accountNumber){
        return accountService.deleteAccountByAccountNumber(accountNumber);
    }
}
