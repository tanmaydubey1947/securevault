-- =========================
-- 1. USERS TABLE
-- =========================
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    kyc_status ENUM('PENDING', 'VERIFIED', 'REJECTED') DEFAULT 'PENDING',
    phone_number VARCHAR(20) NULL,
    account_status ENUM('ACTIVE', 'SUSPENDED', 'CLOSED', 'PENDING_VERIFICATION') DEFAULT 'PENDING_VERIFICATION',
    role ENUM('ROLE_USER', 'ROLE_ADMIN') DEFAULT 'ROLE_USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE password_reset_tokens (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    email           VARCHAR(255) NOT NULL,
    token           VARCHAR(255) NOT NULL UNIQUE,
    used            BOOLEAN DEFAULT FALSE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_password_reset_email
        FOREIGN KEY (email) REFERENCES users (email)
        ON DELETE CASCADE
);

-- =========================
-- 2. BANK_ACCOUNTS TABLE
-- =========================
CREATE TABLE bank_accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id CHAR(36) NOT NULL,
    account_number VARCHAR(50) NOT NULL,
    ifsc_code VARCHAR(20) NOT NULL,
    bank_name VARCHAR(100) NOT NULL,
    verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_bank_user FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- =========================
-- 3. WALLETS TABLE
-- =========================
CREATE TABLE wallets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_email VARCHAR(255) NOT NULL UNIQUE,
    available_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00,
    pending_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00,
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_wallet_user_email
        FOREIGN KEY (user_email)
        REFERENCES users(email)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- =========================
-- 4. TRANSACTIONS TABLE
-- =========================
CREATE TABLE transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender VARCHAR(255) NOT NULL,
    type ENUM('TOPUP', 'WITHDRAW', 'TRANSFER', 'RECEIVE', 'REFUND', 'ADJUSTMENT') NOT NULL,
    status ENUM('PENDING', 'SUCCESS', 'FAILED', 'ACKNOWLEDGED', 'INITIATED') NOT NULL DEFAULT 'INITIATED',
    amount DECIMAL(18,2) NOT NULL,
    receiver VARCHAR(255) NULL,
    bank_ref VARCHAR(255) NULL,
    idempotency_key VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_txn_wallet FOREIGN KEY (wallet_id) REFERENCES wallets(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_txn_related_wallet FOREIGN KEY (related_wallet_id) REFERENCES wallets(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_txn_wallet (wallet_id),
    INDEX idx_txn_status (status),
    UNIQUE KEY unique_idempotency (idempotency_key)
);

-- =========================
-- 5. LEDGER_ENTRIES TABLE
-- =========================
CREATE TABLE ledger_entries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_id CHAR(36) NOT NULL,
    account_ref VARCHAR(255) NOT NULL,
    entry_type ENUM('DEBIT', 'CREDIT') NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ledger_txn FOREIGN KEY (transaction_id) REFERENCES transactions(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_ledger_txn (transaction_id)
);

-- =========================
-- 6. IDEMPOTENCY_KEYS TABLE
-- =========================
CREATE TABLE idempotency_keys (
    idempotency_key VARCHAR(255) PRIMARY KEY,
    user_email VARCHAR(255) NOT NULL,
    transaction_id BIGINT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_idempotency_user FOREIGN KEY (user_email) REFERENCES users (email)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- =========================
-- 7. NOTIFICATIONS TABLE
-- =========================
CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id CHAR(36) NOT NULL,
    type ENUM('EMAIL', 'SMS', 'PUSH') NOT NULL,
    message TEXT NOT NULL,
    status ENUM('PENDING', 'SENT', 'FAILED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_notif_status (status)
);