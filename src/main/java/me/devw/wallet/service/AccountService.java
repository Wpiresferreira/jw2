package me.devw.wallet.service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import me.devw.wallet.dto.AccountBalanceResponse;
import me.devw.wallet.entity.Account;
import me.devw.wallet.entity.AccountType;
import me.devw.wallet.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AccountService {

    @PersistenceContext
    private EntityManager em;

    public Account findById(UUID id) {
        return em.find(Account.class, id);
    }

//    public boolean existsByUsername(String username) {
//        Long count = em.createQuery(
//                        "SELECT COUNT(u) FROM User u WHERE u.username = :username",
//                        Long.class
//                )
//                .setParameter("username", username)
//                .getSingleResult();
//
//        return count > 0;
//    }

    @Transactional
    public void save(Account account) {
        Account saved = account;
        saved.setPosition(
                saved.getPosition() == null
                        ? BigDecimal.ONE
                        : saved.getPosition()
        );
        em.persist(saved);


    }

    public Account getOpeningEquity(UUID userId) {
        return em.createQuery("""
        SELECT a FROM Account a
        WHERE a.userId = :userId
        AND a.accountType = :type
    """, Account.class)
                .setParameter("userId", userId)
                .setParameter("type", AccountType.EQUITY)
                .getSingleResult();
    }

    public List<AccountBalanceResponse> findBalances(UUID userId) {
        List<Object[]> rows = em.createNativeQuery("""
        SELECT 
            a.id,
            a.name,
            a.type,
            a.icon,
            a.icon_color,
            a.position,
            a.currency,
            COALESCE(SUM(
                CASE
                    WHEN t.credit_account_id = a.id THEN t.amount
                    WHEN t.debit_account_id = a.id THEN -t.amount
                    ELSE 0
                END
            ), 0) AS balance
        FROM accounts a
        LEFT JOIN transactions t
            ON a.id = t.credit_account_id
            OR a.id = t.debit_account_id
WHERE a.user_id = ? AND a.type != 'EQUITY' 
        GROUP BY a.id, a.name
    """)
                .setParameter(1, userId)
                .getResultList();

        return rows.stream().map(r -> {
            AccountBalanceResponse dto = new AccountBalanceResponse();
            dto.id = (UUID) r[0];
            dto.name = (String) r[1];
            dto.accountType = AccountType.valueOf((String) r[2]);
            dto.icon = (String) r[3];
            dto.icon_color = (String) r[4];
            dto.position = (Integer) r[5];
            dto.currency = (String) r[6];
            dto.balance = (BigDecimal) r[7];

            return dto;
        }).toList();
    }
}