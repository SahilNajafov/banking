package com.br.banking.service;

import com.br.banking.dto.UserDto;
import com.br.banking.entity.User;
import com.br.banking.enums.ErrorCodeEnum;
import com.br.banking.exceptions.TakenEmailException;
import com.br.banking.exceptions.UserNotFoundException;
import com.br.banking.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminService {

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    public UserDto getUserByFin(String fin) {
        return userRepository.findUserByFin(fin)
                .map(user -> UserDto.builder()
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .password(user.getPassword())
                        .fin(user.getFin())
                        .build())
                .orElseThrow(() -> new UserNotFoundException(ErrorCodeEnum.USER_NOT_FOUND));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
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

    public String editUserDetailsByFin(String fin, UserDto userDto) {

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
}
