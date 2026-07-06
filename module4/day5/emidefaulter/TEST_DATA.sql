-- EMI Defaulter Management System - Test Data SQL Script
-- Execute this script to populate the database with test data for API testing

-- Insert Test Customers
INSERT INTO customers (customer_name, email, phone_number, password, city, credit_score, role) VALUES
('John Doe', 'john@example.com', '9876543210', '$2a$10$Hc1FZmIH3pXd5WqXxT6FUO0KcNnA2p4U.xqX.VJ8LJ5v2Qw7qZcKi', 'Mumbai', 750, 'CUSTOMER'),
('Jane Smith', 'jane@example.com', '9876543211', '$2a$10$Hc1FZmIH3pXd5WqXxT6FUO0KcNnA2p4U.xqX.VJ8LJ5v2Qw7qZcKi', 'Delhi', 680, 'CUSTOMER'),
('Admin User', 'admin@bank.com', '9876543212', '$2a$10$Hc1FZmIH3pXd5WqXxT6FUO0KcNnA2p4U.xqX.VJ8LJ5v2Qw7qZcKi', 'Mumbai', 850, 'ADMIN'),
('Recovery Manager', 'manager@bank.com', '9876543213', '$2a$10$Hc1FZmIH3pXd5WqXxT6FUO0KcNnA2p4U.xqX.VJ8LJ5v2Qw7qZcKi', 'Bangalore', 800, 'RECOVERY_MANAGER'),
('Auditor User', 'auditor@bank.com', '9876543214', '$2a$10$Hc1FZmIH3pXd5WqXxT6FUO0KcNnA2p4U.xqX.VJ8LJ5v2Qw7qZcKi', 'Mumbai', 780, 'AUDITOR'),
('Rahul Kumar', 'rahul@example.com', '9876543215', '$2a$10$Hc1FZmIH3pXd5WqXxT6FUO0KcNnA2p4U.xqX.VJ8LJ5v2Qw7qZcKi', 'Delhi', 620, 'CUSTOMER'),
('Priya Singh', 'priya@example.com', '9876543216', '$2a$10$Hc1FZmIH3pXd5WqXxT6FUO0KcNnA2p4U.xqX.VJ8LJ5v2Qw7qZcKi', 'Bangalore', 700, 'CUSTOMER'),
('Amit Patel', 'amit@example.com', '9876543217', '$2a$10$Hc1FZmIH3pXd5WqXxT6FUO0KcNnA2p4U.xqX.VJ8LJ5v2Qw7qZcKi', 'Mumbai', 550, 'CUSTOMER'),
('Neha Verma', 'neha@example.com', '9876543218', '$2a$10$Hc1FZmIH3pXd5WqXxT6FUO0KcNnA2p4U.xqX.VJ8LJ5v2Qw7qZcKi', 'Delhi', 450, 'CUSTOMER'),
('Rohan Gupta', 'rohan@example.com', '9876543219', '$2a$10$Hc1FZmIH3pXd5WqXxT6FUO0KcNnA2p4U.xqX.VJ8LJ5v2Qw7qZcKi', 'Bangalore', 380, 'CUSTOMER');

-- Insert Test Loans
-- Loan for John Doe (Customer ID 1)
INSERT INTO loans (customer_id, loan_type, loan_amount, emi_amount, interest_rate, tenure_months, loan_status) VALUES
(1, 'PERSONAL', 500000, 15000, 10.5, 36, 'ACTIVE'),
(1, 'AUTO', 800000, 22000, 8.5, 48, 'ACTIVE'),

-- Loans for Jane Smith (Customer ID 2)
(2, 'HOME', 2500000, 45000, 7.5, 60, 'ACTIVE'),
(2, 'PERSONAL', 300000, 10000, 11.0, 36, 'DEFAULTED'),

-- Loans for Rahul Kumar (Customer ID 6)
(6, 'PERSONAL', 400000, 12000, 10.5, 36, 'ACTIVE'),
(6, 'AUTO', 600000, 16000, 9.0, 48, 'ACTIVE'),

-- Loans for Priya Singh (Customer ID 7)
(7, 'HOME', 1500000, 30000, 7.5, 60, 'ACTIVE'),

-- Loans for Amit Patel (Customer ID 8)
(8, 'PERSONAL', 250000, 8500, 12.0, 36, 'DEFAULTED'),

-- Loans for Neha Verma (Customer ID 9)
(9, 'AUTO', 400000, 11000, 10.5, 48, 'DEFAULTED'),

-- Loans for Rohan Gupta (Customer ID 10)
(10, 'PERSONAL', 150000, 5500, 13.0, 36, 'DEFAULTED');

-- Insert EMI Payments for John's first loan
INSERT INTO emi_payments (loan_id, amount_paid, due_date, payment_date, payment_status) VALUES
(1, 15000, '2026-06-01', '2026-05-28', 'PAID'),
(1, 15000, '2026-07-01', '2026-06-30', 'PAID'),
(1, 15000, '2026-08-01', NULL, 'PENDING'),
(1, 15000, '2026-09-01', NULL, 'PENDING'),

-- EMI Payments for Jane's defaulted loan (missed payments)
(4, 10000, '2026-05-01', NULL, 'MISSED'),
(4, 10000, '2026-06-01', NULL, 'MISSED'),
(4, 10000, '2026-07-01', NULL, 'MISSED'),

-- EMI Payments for Priya's loan
(7, 30000, '2026-06-01', '2026-05-30', 'PAID'),
(7, 30000, '2026-07-01', '2026-07-01', 'PAID'),
(7, 30000, '2026-08-01', NULL, 'PENDING'),

-- EMI Payments for Amit's defaulted loan
(8, 8500, '2026-05-01', NULL, 'MISSED'),
(8, 8500, '2026-06-01', NULL, 'MISSED'),
(8, 8500, '2026-07-01', NULL, 'MISSED'),

-- EMI Payments for Neha's defaulted loan
(9, 11000, '2026-05-01', NULL, 'MISSED'),
(9, 11000, '2026-06-01', NULL, 'MISSED'),
(9, 11000, '2026-07-01', NULL, 'MISSED');

-- Insert Penalties (Auto-generated for missed EMI payments)
INSERT INTO penalties (payment_id, penalty_amount, penalty_reason, generated_date) VALUES
(5, 200, 'Auto-generated for missed EMI payment', '2026-05-02'),
(6, 200, 'Auto-generated for missed EMI payment', '2026-06-02'),
(7, 200, 'Auto-generated for missed EMI payment', '2026-07-02'),
(11, 170, 'Auto-generated for missed EMI payment', '2026-05-02'),
(12, 170, 'Auto-generated for missed EMI payment', '2026-06-02'),
(13, 170, 'Auto-generated for missed EMI payment', '2026-07-02'),
(14, 220, 'Auto-generated for missed EMI payment', '2026-05-02'),
(15, 220, 'Auto-generated for missed EMI payment', '2026-06-02'),
(16, 220, 'Auto-generated for missed EMI payment', '2026-07-02');

-- Note: All passwords are encrypted bcrypt hashes for password: 'password123'
-- To create your own, use a bcrypt generator or Spring Security's BCryptPasswordEncoder
-- Example: password123 = $2a$10$Hc1FZmIH3pXd5WqXxT6FUO0KcNnA2p4U.xqX.VJ8LJ5v2Qw7qZcKi

