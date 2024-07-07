package com.br.banking.service;

import com.br.banking.dto.AccountDto;
import com.br.banking.entity.Account;
import com.br.banking.entity.User;
import com.br.banking.enums.ErrorCodeEnum;
import com.br.banking.exceptions.AccountNotFoundException;
import com.br.banking.exceptions.UserNotFoundException;
import com.br.banking.repository.AccountRepository;
import com.br.banking.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AccountService {
    UserRepository userRepository;
    AccountRepository accountRepository;

    public String createAccount(AccountDto accountDto) {
        String fin = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByFin(fin);
        Random random = new Random();

        Account account = Account.builder()
                .accountNumber(String.valueOf(Math.abs(random.nextLong())))
                .balance(accountDto.getBalance())
                .activity(accountDto.getActivity())
                .user(user)
                .build();
        if(accountDto.getCurrency()==null){
            account.setCurrency("AZN");
        }
        else{
            account.setCurrency(accountDto.getCurrency());
        }

        account=accountRepository.save(account);

        user.getAccounts().add(account);
        userRepository.save(user);
        return "account saved successfully!";
    }

    public String deleteAccountByAccountNumber(Long accountNumber) {
        String accNumber = String.valueOf(accountNumber);
        accountRepository.findAccountByAccountNumber(accNumber).ifPresentOrElse(
                accountRepository::delete,
                () -> {
                    throw new AccountNotFoundException(ErrorCodeEnum.ACCOUNT_NOT_FOUND);
                }
        );
        return "account successfully deleted!";
    }
}
