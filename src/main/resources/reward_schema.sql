-- SQL Script to Create Reward Points Table
-- This table stores reward points earned by each account
-- Run this script on the MoneyTransferSystem database

CREATE TABLE IF NOT EXISTS reward_points (
    account_id BIGINT PRIMARY KEY,
    total_points BIGINT NOT NULL DEFAULT 0,
    last_earned_on TIMESTAMP NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
);

-- SQL Script to Create Reward Transactions Table
-- This table logs each reward transaction/earning
CREATE TABLE IF NOT EXISTS reward_transactions (
    id CHAR(36) PRIMARY KEY,
    from_account_id BIGINT NOT NULL,
    to_account_id BIGINT NOT NULL,
    transfer_amount DECIMAL(19, 2) NOT NULL,
    points_earned BIGINT NOT NULL,
    related_transaction_id CHAR(36) NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_account_id) REFERENCES account(id) ON DELETE CASCADE,
    FOREIGN KEY (to_account_id) REFERENCES account(id) ON DELETE CASCADE,
    FOREIGN KEY (related_transaction_id) REFERENCES transaction_log(id) ON DELETE CASCADE,
    INDEX idx_from_account (from_account_id),
    INDEX idx_to_account (to_account_id),
    INDEX idx_related_transaction (related_transaction_id),
    INDEX idx_created_on (created_on)
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_reward_points_account_id ON reward_points(account_id);
CREATE INDEX IF NOT EXISTS idx_reward_transactions_from_account_id ON reward_transactions(from_account_id, created_on DESC);
CREATE INDEX IF NOT EXISTS idx_reward_transactions_to_account_id ON reward_transactions(to_account_id, created_on DESC);
