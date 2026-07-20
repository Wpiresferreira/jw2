package me.devw.wallet.dto;

import me.devw.wallet.entity.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountBalanceResponse {

        public UUID id;
        public String name;
        public AccountType accountType;
        public String icon;
        public String icon_color;
        public BigDecimal balance;
        public String currency;
        public Number position;

}
