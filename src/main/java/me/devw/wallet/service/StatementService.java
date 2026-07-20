package me.devw.wallet.service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import me.devw.wallet.dto.AccountBalanceResponse;
import me.devw.wallet.entity.Account;
import me.devw.wallet.entity.AccountType;
import me.devw.wallet.entity.Statement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class StatementService {

    @PersistenceContext
    private EntityManager em;


//    @Transactional
//    public void save(Account account) {
//        em.persist(account);
//
//
//    }


    public List<Statement> getStatement(UUID userId, UUID accountId, int year, int month) {

        List<Object[]> rows = em.createNativeQuery("""
    SELECT 
        t.id,
        t.transaction_date,
        t.description,
        t.amount,
        CASE 
            WHEN t.credit_account_id = ?2 THEN true
            ELSE false
        END AS is_credit
    FROM transactions t
    WHERE t.user_id = ?1
      AND (t.debit_account_id = ?2 OR t.credit_account_id = ?2)
      AND EXTRACT(YEAR FROM t.transaction_date) = ?3
      AND EXTRACT(MONTH FROM t.transaction_date) = ?4
    ORDER BY t.transaction_date ASC, t.created_at ASC
""")
                .setParameter(1, userId)
                .setParameter(2, accountId)
                .setParameter(3, year)
                .setParameter(4, month)
                .getResultList();


        List<Statement> list = new ArrayList<>();

        for (Object[] row : rows) {
            Statement s = new Statement(
                    (UUID) row[0],
                    ((java.sql.Date) row[1]).toLocalDate(),
                    (String) row[2],
                    (BigDecimal) row[3],
                    (Boolean) row[4]
            );
            list.add(s);
        }

        return list;
    }

}