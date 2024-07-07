package com.br.banking.service;

import com.br.banking.config.JwtService;
import com.br.banking.dto.UserDto;
import com.br.banking.dto.response.UserResponse;
import com.br.banking.dto.auth.AuthenticationRequest;
import com.br.banking.dto.auth.AuthenticationResponse;
import com.br.banking.dto.response.AccountResponse;
import com.br.banking.entity.Account;
import com.br.banking.entity.User;
import com.br.banking.enums.ErrorCodeEnum;
import com.br.banking.enums.Role;
import com.br.banking.exceptions.AlreadyRegisteredUserException;
import com.br.banking.exceptions.TakenEmailException;
import com.br.banking.exceptions.UserNotFoundException;
import com.br.banking.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    AuthenticationManager authenticationManager;

    JwtService jwtService;

    public String saveUser(UserDto userDto) {

        Optional<User> userByFin = userRepository.findUserByFin(userDto.getFin());
        Optional<User> userByEmail = userRepository.findUserByEmail(userDto.getEmail());

        userByFin.ifPresentOrElse(
                existingUser -> {
                    throw new AlreadyRegisteredUserException(ErrorCodeEnum.ALREADY_REGISTERED_ERROR);
                },
                () -> userByEmail.ifPresentOrElse(
                        existingUser -> {
                            throw new AlreadyRegisteredUserException(ErrorCodeEnum.ALREADY_REGISTERED_ERROR);
                        },
                        () -> {
                            User user = User.builder()
                                    .firstName(userDto.getFirstName())
                                    .lastName(userDto.getLastName())
                                    .email(userDto.getEmail())
                                    .password(passwordEncoder.encode(userDto.getPassword()))
                                    .fin(userDto.getFin())
                                    .role(Role.USER)
                                    .build();
                            userRepository.save(user);
                        }
                )
        );
        return "successfully registered!";
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var user = userRepository.findUserByFin(request.getFin())
                .orElseThrow(() -> new BadCredentialsException("try again !"));

        if (authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getFin(),
                        request.getPassword())).isAuthenticated()) {
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            throw new BadCredentialsException("try again!");
        }
    }

    public String deleteUserByFin(String fin) {
        userRepository.findUserByFin(fin).ifPresentOrElse(
                userRepository::delete,
                () -> {
                    throw new UserNotFoundException(ErrorCodeEnum.USER_NOT_FOUND);
                }
        );
        return "user successfully deleted!";
    }

    public String editUserDetails(UserDto userDto) {
        String fin = SecurityContextHolder.getContext().getAuthentication().getName();
        User userByFin = userRepository.findByFin(fin);

        if (userDto.getEmail() != null && !userDto.getEmail().equals(userByFin.getEmail())) {
            if (userRepository.findUserByEmail(userDto.getEmail()).isPresent()) {
                throw new TakenEmailException(ErrorCodeEnum.TAKEN_EMAIL_ERROR);
            } else {
                userByFin.setEmail(userDto.getEmail());
            }
        }
        if (userDto.getFirstName() != null && !userDto.getFirstName().equals(userByFin.getFirstName())) {
            userByFin.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null && !userDto.getLastName().equals(userByFin.getLastName())) {
            userByFin.setLastName(userDto.getLastName());
        }
        if (userDto.getPassword() != null && !userDto.getPassword().equals(userByFin.getPassword())) {
            userByFin.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        userRepository.save(userByFin);
        return "user successfully updated!";
    }

    public Set<AccountResponse> getMyAccounts(String fin) {
        var user = userRepository.findByFin(fin);
        Set<Account> accountSet = user.getAccounts();
        Set<Account> accountSetCopy = new HashSet<>();
        for (Account acc : accountSet) {
            if (acc.getActivity() != null && acc.getActivity()) {
                accountSetCopy.add(acc);
            }
        }
        return accountSetCopy.stream()
                .map(account -> new AccountResponse(account.getAccountNumber(), account.getBalance(), account.getActivity(),account.getCurrency()))
                .collect(Collectors.toSet());
    }

    public UserResponse getUserInfos(String fin) {
        User user = userRepository.findByFin(fin);
        if (userRepository.findUserByFin(fin).isPresent()) {
            return UserResponse.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .fin(user.getFin())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
        }
        else{
            throw new UserNotFoundException(ErrorCodeEnum.USER_NOT_FOUND);
        }
    }

}
