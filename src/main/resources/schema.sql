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
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- 1(a). USERS VERIFICATION TABLE
-- =========================
CREATE TABLE users_verification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id CHAR(36) NOT NULL,
    otp VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
    user_id CHAR(36) NOT NULL,
    currency CHAR(3) NOT NULL DEFAULT 'INR',
    available_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00,
    pending_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00,
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_wallet_user FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY unique_user_currency (user_id, currency)
);

-- =========================
-- 4. TRANSACTIONS TABLE
-- =========================
CREATE TABLE transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    wallet_id CHAR(36) NOT NULL,
    type ENUM('TOPUP', 'WITHDRAW', 'TRANSFER', 'RECEIVE', 'REFUND', 'ADJUSTMENT') NOT NULL,
    status ENUM('INITIATED', 'PENDING', 'SUCCESS', 'FAILED') NOT NULL DEFAULT 'INITIATED',
    amount DECIMAL(18,2) NOT NULL,
    currency CHAR(3) NOT NULL DEFAULT 'INR',
    related_wallet_id CHAR(36) NULL,
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
    idempotency_key BIGINT PRIMARY KEY AUTO_INCREMENT,,
    user_id CHAR(36) NOT NULL,
    request_hash VARCHAR(255) NOT NULL,
    transaction_id CHAR(36) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_idempotency_user FOREIGN KEY (user_id) REFERENCES users(id)
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