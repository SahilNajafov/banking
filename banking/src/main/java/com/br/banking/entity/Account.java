package com.br.banking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account extends AbstractEntity {

    @Column(name = "account_number", nullable = false, unique = true)
    String accountNumber;

    @Column(name = "balance")
    BigDecimal balance;

    @Column(name = "activity")
    Boolean activity;

    @Column(name = "currency")
    String currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @JsonBackReference
    User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(accountNumber, account.accountNumber) && Objects.equals(balance, account.balance) && Objects.equals(activity, account.activity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), accountNumber, balance, activity);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", activity=" + activity +
                '}';
    }
}
