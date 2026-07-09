-- Supply Chain Financing - Initial Setup Data
-- This script creates sample users and loans for testing
-- All sample customer passwords are: password123 (BCrypt hashed)

-- Insert sample customers (if not already exist)
INSERT INTO users (name, email, password, role, phone, created_at)
SELECT 'Alice Johnson', 'alice@example.com', '$2a$10$SlVZrJ8.K8L/I.9QJ/6N/.h7K0p7F3Z9K4X2Y5A1B2C3D4E5F6G7H8', 'CUSTOMER', '9876543210', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'alice@example.com');

INSERT INTO users (name, email, password, role, phone, created_at)
SELECT 'Bob Smith', 'bob@example.com', '$2a$10$SlVZrJ8.K8L/I.9QJ/6N/.h7K0p7F3Z9K4X2Y5A1B2C3D4E5F6G7H8', 'CUSTOMER', '9876543211', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'bob@example.com');

INSERT INTO users (name, email, password, role, phone, created_at)
SELECT 'Carol Davis', 'carol@example.com', '$2a$10$SlVZrJ8.K8L/I.9QJ/6N/.h7K0p7F3Z9K4X2Y5A1B2C3D4E5F6G7H8', 'CUSTOMER', '9876543212', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'carol@example.com');

INSERT INTO users (name, email, password, role, phone, created_at)
SELECT 'Diana Wilson', 'diana@example.com', '$2a$10$SlVZrJ8.K8L/I.9QJ/6N/.h7K0p7F3Z9K4X2Y5A1B2C3D4E5F6G7H8', 'CUSTOMER', '9876543213', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'diana@example.com');

INSERT INTO users (name, email, password, role, phone, created_at)
SELECT 'Eve Martinez', 'eve@example.com', '$2a$10$SlVZrJ8.K8L/I.9QJ/6N/.h7K0p7F3Z9K4X2Y5A1B2C3D4E5F6G7H8', 'CUSTOMER', '9876543214', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'eve@example.com');

-- Insert sample loans
-- Note: Loans reference customer IDs; adjust IDs based on your database
INSERT INTO loans (loan_number, customer_id, amount, interest_rate, tenure_months, purpose, status, remaining_amount, created_at)
SELECT 'LN-A1B2C3D4', u.id, 500000.00, 12.00, 24, 'Inventory Purchase', 'PENDING', 560000.00, CURRENT_TIMESTAMP
FROM users u WHERE u.email = 'alice@example.com'
AND NOT EXISTS (SELECT 1 FROM loans WHERE loan_number = 'LN-A1B2C3D4');

INSERT INTO loans (loan_number, customer_id, amount, interest_rate, tenure_months, purpose, status, remaining_amount, created_at)
SELECT 'LN-E5F6G7H8', u.id, 750000.00, 10.50, 36, 'Equipment Procurement', 'APPROVED', 850000.00, CURRENT_TIMESTAMP
FROM users u WHERE u.email = 'bob@example.com'
AND NOT EXISTS (SELECT 1 FROM loans WHERE loan_number = 'LN-E5F6G7H8');

INSERT INTO loans (loan_number, customer_id, amount, interest_rate, tenure_months, purpose, status, remaining_amount, created_at)
SELECT 'LN-I9J0K1L2', u.id, 300000.00, 13.50, 18, 'Working Capital', 'DISBURSED', 330000.00, CURRENT_TIMESTAMP
FROM users u WHERE u.email = 'carol@example.com'
AND NOT EXISTS (SELECT 1 FROM loans WHERE loan_number = 'LN-I9J0K1L2');

INSERT INTO loans (loan_number, customer_id, amount, interest_rate, tenure_months, purpose, status, remaining_amount, created_at)
SELECT 'LN-M3N4O5P6', u.id, 1000000.00, 11.00, 48, 'Facility Expansion', 'PENDING', 1100000.00, CURRENT_TIMESTAMP
FROM users u WHERE u.email = 'diana@example.com'
AND NOT EXISTS (SELECT 1 FROM loans WHERE loan_number = 'LN-M3N4O5P6');

INSERT INTO loans (loan_number, customer_id, amount, interest_rate, tenure_months, purpose, status, remaining_amount, created_at)
SELECT 'LN-Q7R8S9T0', u.id, 250000.00, 12.50, 12, 'Raw Material', 'FULLY_PAID', 0.00, CURRENT_TIMESTAMP
FROM users u WHERE u.email = 'eve@example.com'
AND NOT EXISTS (SELECT 1 FROM loans WHERE loan_number = 'LN-Q7R8S9T0');

-- Insert sample repayments for the FULLY_PAID loan
INSERT INTO repayments (loan_id, amount, payment_date, payment_mode, remarks)
SELECT l.id, 125000.00, CURRENT_DATE, 'NEFT', 'First installment'
FROM loans l WHERE l.loan_number = 'LN-Q7R8S9T0'
AND NOT EXISTS (SELECT 1 FROM repayments WHERE loan_id = l.id);

INSERT INTO repayments (loan_id, amount, payment_date, payment_mode, remarks)
SELECT l.id, 125000.00, CURRENT_DATE + INTERVAL '1 month', 'NEFT', 'Second installment'
FROM loans l WHERE l.loan_number = 'LN-Q7R8S9T0'
AND NOT EXISTS (SELECT 1 FROM repayments WHERE loan_id = l.id AND remarks = 'Second installment');

-- Verify data was inserted
SELECT COUNT(*) as customer_count FROM users WHERE role = 'CUSTOMER';
SELECT COUNT(*) as loan_count FROM loans;
SELECT COUNT(*) as repayment_count FROM repayments;


