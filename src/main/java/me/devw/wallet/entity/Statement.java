package me.devw.wallet.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Statement {
    @Id
    private Long id;
    private UUID transactionId;
    private LocalDate date;
    private String description;
    private BigDecimal amount;
    private boolean isCredit; // true = entrou, false = saiu

    public Statement(UUID transactionId, LocalDate date, String description, BigDecimal amount, boolean isCredit) {
        this.transactionId = transactionId;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.isCredit = isCredit;
    }

    public Statement() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public UUID getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public boolean isCredit() {
        return isCredit;
    }
    public void setCredit(boolean isCredit) {
        this.isCredit = isCredit;
    }

}
