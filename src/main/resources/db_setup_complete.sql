-- ============================================================================
-- MONEY TRANSFER SYSTEM - COMPLETE DATABASE SETUP SCRIPT
-- ============================================================================
-- This script creates:
-- 1. MoneyTransferSystem database
-- 2. Account table
-- 3. TransactionLog table
-- 4. RewardPoints table
-- 5. RewardTransactions table
-- 6. All necessary indexes
-- 7. Sample data for testing
-- ============================================================================

-- Step 1: Create Database
-- ============================================================================
DROP DATABASE IF EXISTS MoneyTransferSystem;
CREATE DATABASE MoneyTransferSystem CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE MoneyTransferSystem;

-- Step 2: Create Account Table
-- ============================================================================
CREATE TABLE IF NOT EXISTS account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    holder_name VARCHAR(100) NOT NULL,
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    version INT NOT NULL DEFAULT 0,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_account_status (status),
    INDEX idx_account_holder (holder_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Step 3: Create TransactionLog Table
-- ============================================================================
CREATE TABLE IF NOT EXISTS transaction_log (
    id CHAR(36) PRIMARY KEY,
    from_account_id BIGINT NOT NULL,
    to_account_id BIGINT NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    failure_reason VARCHAR(255),
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    idempotency_key VARCHAR(100) UNIQUE,
    FOREIGN KEY (from_account_id) REFERENCES account(id) ON DELETE RESTRICT,
    FOREIGN KEY (to_account_id) REFERENCES account(id) ON DELETE RESTRICT,
    INDEX idx_from_account (from_account_id),
    INDEX idx_to_account (to_account_id),
    INDEX idx_status (status),
    INDEX idx_created_on (created_on)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Step 4: Create RewardPoints Table
-- ============================================================================
CREATE TABLE IF NOT EXISTS reward_points (
    account_id BIGINT PRIMARY KEY,
    total_points BIGINT NOT NULL DEFAULT 0,
    last_earned_on TIMESTAMP NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
    INDEX idx_reward_points_account_id (account_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Step 5: Create RewardTransactions Table
-- ============================================================================
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
    INDEX idx_reward_transactions_from_account_id (from_account_id, created_on DESC),
    INDEX idx_reward_transactions_to_account_id (to_account_id, created_on DESC),
    INDEX idx_reward_transactions_related (related_transaction_id),
    INDEX idx_reward_transactions_created_on (created_on)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Step 6: Insert Sample Data
-- ============================================================================
-- Create sample accounts
INSERT INTO account (holder_name, balance, status) VALUES
('Lavi', 10000.00, 'ACTIVE'),
('John Doe', 5000.00, 'ACTIVE'),
('Jane Smith', 15000.00, 'ACTIVE'),
('Ravi Kumar', 8000.00, 'ACTIVE'),
('Priya Singh', 12000.00, 'ACTIVE');

-- Initialize reward points for all accounts
INSERT INTO reward_points (account_id, total_points)
SELECT id, 0 FROM account;

-- Step 7: Verification Queries (can be used to verify data)
-- ============================================================================
-- SELECT 'Accounts Created' as Status, COUNT(*) as Total FROM account;
-- SELECT 'Reward Points Initialized' as Status, COUNT(*) as Total FROM reward_points;
-- SELECT 'All Tables Ready' as Status, COUNT(*) as Tables FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'MoneyTransferSystem';

-- ============================================================================
-- SETUP COMPLETE!
-- ============================================================================
-- You can now:
-- 1. Run the backend: mvn spring-boot:run
-- 2. Login with: username=Ganesha, password=pass123 (or username=lavi, password=pass123)
-- 3. Make transfers and earn reward points!
-- ============================================================================
