package me.devw.wallet.service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import me.devw.wallet.entity.Statement;
import me.devw.wallet.entity.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TransactionService {

    @PersistenceContext
    private EntityManager em;

    public List<Statement> getStatement(UUID userId, UUID accountId, int year, int month) {


        System.out.println("accountId = " + accountId);
        System.out.println("year = " + year);
        System.out.println("month = " + month);

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

        BigDecimal runningBalance = getInitialBalance(userId, accountId, year, month);

        for (Object[] row : rows) {
            UUID id = (UUID) row[0];
            java.time.LocalDate date = ((java.sql.Date) row[1]).toLocalDate();
            String description = (String) row[2];
            BigDecimal amount = (BigDecimal) row[3];
            Boolean isCredit = (Boolean) row[4];

            // Atualiza saldo
            if (isCredit) {
                runningBalance = runningBalance.add(amount);
            } else {
                runningBalance = runningBalance.subtract(amount);
            }

            Statement s = new Statement(
                    id,
                    date,
                    description,
                    amount,
                    isCredit,
                    runningBalance
            );

            list.add(s);
        }



        return list;
    }


    @Transactional
    public void save(Transaction transaction) {
        em.persist(transaction);
    }


    public BigDecimal getInitialBalance(UUID userId, UUID accountId, int year, int month) {
        Object result = em.createNativeQuery("""
        SELECT COALESCE(
            SUM(
                CASE 
                    WHEN t.credit_account_id = ?2 THEN t.amount
                    ELSE -t.amount
                END
            ), 0
        )
        FROM transactions t
        WHERE t.user_id = ?1
          AND (t.debit_account_id = ?2 OR t.credit_account_id = ?2)
          AND t.transaction_date < make_date(?3, ?4, 1)
    """)
                .setParameter(1, userId)
                .setParameter(2, accountId)
                .setParameter(3, year)
                .setParameter(4, month)
                .getSingleResult();

        return (BigDecimal) result;
    }
}