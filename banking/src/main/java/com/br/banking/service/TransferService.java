package com.br.banking.service;

import com.br.banking.dto.TransferRequest;
import com.br.banking.dto.response.ExchangeRateResponse;
import com.br.banking.entity.Account;
import com.br.banking.enums.ErrorCodeEnum;
import com.br.banking.exceptions.*;
import com.br.banking.repository.AccountRepository;
import com.br.banking.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransferService {

    AccountRepository accountRepository;

    UserRepository userRepository;

    ExchangeRateService exchangeRateService;

    @Transactional
    public String transfer(String fin, TransferRequest transferRequest) {


        userRepository.findUserByFin(fin).orElseThrow(() -> new UserNotFoundException(ErrorCodeEnum.USER_NOT_FOUND));

        Account fromAccount = accountRepository
                .findByAccountNumberAndUserFin(transferRequest.getFromAccountNumber(), fin);
        Account toAccount = accountRepository
                .findByAccountNumber(transferRequest.getToAccountNumber());


        if (fromAccount == null) {
            throw new AccountNotFoundException(ErrorCodeEnum.SOURCE_ACCOUNT_NOT_FOUND);
        }
        if (toAccount == null) {
            throw new AccountNotFoundException(ErrorCodeEnum.DESTINATION_ACCOUNT_NOT_FOUND);
        }
        if (!toAccount.getActivity() || !fromAccount.getActivity()) {
            throw new NoneActiveAccountException(ErrorCodeEnum.NONE_ACTIVE_ACCOUNT);
        }
        if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
            throw new ImpossibleTransferException(ErrorCodeEnum.BAD_TRANSFER_ERROR);
        }
        if (fromAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new InsufficientAmountException(ErrorCodeEnum.INSUFFICIENT_AMOUNT_ERROR);
        }
        BigDecimal amount = transferRequest.getAmount();
        String transferCurrency=transferRequest.getCurrency();
        String sourceAccCurrency=fromAccount.getCurrency();
        String destinationAccCurrency=toAccount.getCurrency();


        BigDecimal forSource = exchangeRateService.convertCurrency(amount, transferCurrency, sourceAccCurrency);
        BigDecimal forDestination = exchangeRateService.convertCurrency(amount, transferCurrency, destinationAccCurrency);

        fromAccount.setBalance(fromAccount.getBalance().subtract(forSource));
        toAccount.setBalance(toAccount.getBalance().add(forDestination));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return "successful transfer!";
    }

}
