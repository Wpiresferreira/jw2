package me.devw.wallet.dto;

import jakarta.json.bind.annotation.JsonbDateFormat;
import me.devw.wallet.entity.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateTransactionRequest {

    public String debitAccountId;
    public String creditAccountId;
    public BigDecimal amount;
    public String description;
    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
    public LocalDate date;
}