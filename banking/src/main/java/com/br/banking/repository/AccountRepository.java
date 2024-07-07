package com.br.banking.repository;

import com.br.banking.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByAccountNumber(String accountNumber);

    Account findByAccountNumberAndUserFin(String AccountNumber, String fin);
    Account findByAccountNumber(String AccountNumber);

}
