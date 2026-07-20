package me.devw.wallet.dto;

import me.devw.wallet.entity.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateAccountRequest {

    public String name;
    public AccountType accountType;
    public String icon;
    public String icon_color;
    public BigDecimal opening_balance;
    public String currency;
    public Number position;

    public LocalDate created_at;

    public LocalDate opening_date;
}