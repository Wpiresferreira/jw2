drop table users CASCADE;
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL,
                       created_at TIMESTAMP NOT NULL
);


CREATE TYPE account_type AS ENUM (
    'ASSET',
    'INCOME',
    'EXPENSE',
    'LIABILITY'
    );

CREATE EXTENSION IF NOT EXISTS pgcrypto;

drop table if exists accounts CASCADE;

CREATE TABLE accounts (
                          id UUID PRIMARY KEY  DEFAULT gen_random_uuid(),
                          user_id UUID NOT NULL,
                          name VARCHAR(100) NOT NULL,
                          type VARCHAR(20) NOT NULL,
                          icon VARCHAR(100),
                          currency VARCHAR(10) NOT NULL,
                          opening_date DATE NOT NULL DEFAULT DATE(NOW()),
                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),

                          CONSTRAINT fk_accounts_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE
);
CREATE INDEX idx_accounts_user_id
    ON accounts(user_id);

DROP TABLE IF EXISTS transactions;
CREATE TABLE transactions (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,

                              debit_account_id UUID NOT NULL REFERENCES accounts(id),
                              credit_account_id UUID NOT NULL REFERENCES accounts(id),

                              amount NUMERIC(15,2) NOT NULL CHECK (amount > 0),

                              description TEXT,
                              transaction_date DATE NOT NULL,

                              created_at TIMESTAMP NOT NULL DEFAULT NOW()
);