package me.devw.wallet.dto;

import me.devw.wallet.entity.Statement;

import java.util.List;
import java.util.UUID;

public class StatementResponse {
    public UUID accountId;
    public int year;
    public int month;
    public List<Statement> items;

    public StatementResponse(UUID accountId, int year, int month, List<Statement> items) {
        this.accountId = accountId;
        this.year = year;
        this.month = month;
        this.items = items;
    }
}
