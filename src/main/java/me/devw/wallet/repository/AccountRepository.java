package me.devw.wallet.repository;

import jakarta.persistence.EntityManager;
import me.devw.wallet.dto.AccountBalanceResponse;
import me.devw.wallet.entity.Account;
import me.devw.wallet.entity.AccountType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class AccountRepository {

    private final EntityManager entityManager;

    public AccountRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Account account) {
        entityManager.persist(account);
    }

    public Account findById(UUID id) {
        return entityManager.find(Account.class, id);
    }


}