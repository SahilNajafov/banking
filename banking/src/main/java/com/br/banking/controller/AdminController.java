package com.br.banking.controller;

import com.br.banking.dto.UserDto;
import com.br.banking.dto.auth.AuthenticationRequest;
import com.br.banking.dto.auth.AuthenticationResponse;
import com.br.banking.entity.User;
import com.br.banking.service.AdminService;
import com.br.banking.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
//@PreAuthorize("hasRole('ADMIN')")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AdminController {

    AdminService adminService;


    @GetMapping("/{fin}")
    public UserDto getUser(@PathVariable("fin") String fin) {
        return adminService.getUserByFin(fin);
    }

    @GetMapping()
    public List<User> getAllUsers(){
        return adminService.getAllUsers();
    }

    @DeleteMapping("/{fin}")
    public String delete(@PathVariable("fin") String fin){
        return adminService.deleteUserByFin(fin);
    }

    @PutMapping("/{fin}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String edit(@PathVariable("fin") String fin,@RequestBody UserDto userDto) {
        return adminService.editUserDetailsByFin(fin,userDto);
    }

}
