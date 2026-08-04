package me.devw.wallet.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Statement {

    private final UUID id;
    private final LocalDate date;
    private final String description;
    private final BigDecimal amount;
    private final Boolean isCredit;
    private final BigDecimal balance;

    public Statement(UUID id,
                     LocalDate date,
                     String description,
                     BigDecimal amount,
                     Boolean isCredit,
                     BigDecimal balance) {

        this.id = id;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.isCredit = isCredit;
        this.balance = balance;
    }

    public UUID getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public Boolean getIsCredit() { return isCredit; }

    public BigDecimal getBalance() { return balance; } // <-- SEM ISSO NÃO SERIALIZA
}
