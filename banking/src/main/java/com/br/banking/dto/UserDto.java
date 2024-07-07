package com.br.banking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
    @Email(message = "email should be valid")
    @NotBlank
    String email;
    @NotBlank
    String password;
    @NotBlank
    String fin;
}
