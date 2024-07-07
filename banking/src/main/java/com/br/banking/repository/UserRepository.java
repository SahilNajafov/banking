package com.br.banking.repository;


import com.br.banking.entity.Account;
import com.br.banking.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    Optional<User> findUserByFin(String fin);

    Optional<User> findUserByEmail(String email);

    User findByFin(String fin);

    @Query("SELECT n.id from User n where n.fin=:xxx")
    Long findIdByFin(@Param("xxx") String fin);

}
