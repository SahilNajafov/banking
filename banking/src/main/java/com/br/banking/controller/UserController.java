package com.br.banking.controller;

import com.br.banking.dto.response.AccountResponse;
import com.br.banking.dto.UserDto;
import com.br.banking.dto.auth.AuthenticationRequest;
import com.br.banking.dto.auth.AuthenticationResponse;
import com.br.banking.dto.response.UserResponse;
import com.br.banking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/br")
public class UserController {

    private final UserService userService;


    @GetMapping("/my-accounts")
    public Set<AccountResponse> getAccounts() {
        String fin = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getMyAccounts(fin);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody @Valid UserDto userDto) {
        return userService.saveUser(userDto);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        return userService.authenticate(request);
    }

    @DeleteMapping("/delete")
    public String delete() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.deleteUserByFin(authentication.getName());
    }

    @PutMapping("/edit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String edit(@RequestBody UserDto userDto) {
        return userService.editUserDetails(userDto);
    }

    @GetMapping("/my-infos")
    public UserResponse getInfos(){
        String fin = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserInfos(fin);
    }
}
