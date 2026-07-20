package me.devw.wallet.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID user_id;


    @Column(nullable = false)
    private UUID debit_account_id;


    @Column(nullable = false)
    private UUID credit_account_id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column()
    private String description;

    @Column(nullable = false)
    private LocalDate created_at;


    @Column(nullable = false)
    private LocalDate transaction_date;


    //    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID();
//        this.createdAt = LocalDateTime.now();
    }

        public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getUser_id() {
        return this.user_id;
    }
    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }
    public UUID getDebitAccountId() {
        return debit_account_id;
    }
    public void setDebit_account_id(UUID debit_account_id) {
        this.debit_account_id =  debit_account_id;
    }
    public UUID getCreditAccountId() {
        return credit_account_id;
    }
    public void setCredit_account_id(UUID credit_account_id) {
        this.credit_account_id =  credit_account_id;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDate getTransactionDate() {
        return transaction_date;
    }
    public void setCreatedAt(LocalDate created_at) {
        this.created_at = created_at;
    }

    public LocalDate getCreatedAt() {
        return created_at;
    }
    public void setTransactionDate(LocalDate transaction_date) {
        this.transaction_date = transaction_date;
    }

}