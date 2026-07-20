package me.devw.wallet.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AccountType accountType;

    private String icon;
    private String icon_color;

    private String currency;
    private BigDecimal position;

    @Column(name = "opening_date", nullable = false)
    private LocalDate openingDate;

    @Column(name = "created_at")
    private LocalDate createdAt;


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
    public UUID getUserId() {
        return this.userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public void setPosition(BigDecimal position) {
        this.position = position;
    }
    public BigDecimal getPosition() {
        return position;
    }
    public void setIconColor(String iconColor) {
        this.icon_color = iconColor;
    } public String getIconColor() {
        return icon_color;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    public LocalDate getOpeningDate() {
        return openingDate;
    }
    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }


}