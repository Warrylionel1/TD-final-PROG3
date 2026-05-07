-- ======================================================
-- DATA INSERTION FOR THURSDAY 06 MAY EVALUATION
-- PostgreSQL, safe version without CTEs
-- ======================================================

-- Optional: clean existing data (uncomment only tables that exist)

TRUNCATE TABLE collectivity_transaction, member_payment, financial_account,
    membership_fee, collectivity_structure, member_referee,
    collectivity_member, collectivity, member CASCADE;


-- ------------------------------------------------------
-- 1. MEMBERS
-- ------------------------------------------------------
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, join_date) VALUES
                                                                                                                                        ('C1-M1', 'Membre 1', 'Prénom 1', '1980-02-01', 'MALE', 'Lot II V M Ambato', 'Riziculteur', '0341234567', 'member.1@fed-agri.mg', 'PRESIDENT', '2026-01-01'),
                                                                                                                                        ('C1-M2', 'Membre 2', 'Prénom 2', '1982-03-05', 'MALE', 'Lot II F Ambato', 'Agriculteur', '0321234567', 'member.2@fed-agri.mg', 'VICE_PRESIDENT', '2026-01-01'),
                                                                                                                                        ('C1-M3', 'Membre 3', 'Prénom 3', '1992-03-10', 'MALE', 'Lot II J Ambato', 'Collecteur', '0331234567', 'member.3@fed-agri.mg', 'SECRETARY', '2026-01-01'),
                                                                                                                                        ('C1-M4', 'Membre 4', 'Prénom 4', '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato', 'Distributeur', '0381234567', 'member.4@fed-agri.mg', 'TREASURER', '2026-01-01'),
                                                                                                                                        ('C1-M5', 'Membre 5', 'Prénom 5', '1999-08-21', 'MALE', 'Lot UV 80 Ambato', 'Riziculteur', '0373434567', 'member.5@fed-agri.mg', 'SENIOR', '2026-01-01'),
                                                                                                                                        ('C1-M6', 'Membre 6', 'Prénom 6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato', 'Riziculteur', '0372234567', 'member.6@fed-agri.mg', 'SENIOR', '2026-01-01'),
                                                                                                                                        ('C1-M7', 'Membre 7', 'Prénom 7', '1998-01-31', 'MALE', 'Lot UV 7 Ambato', 'Riziculteur', '0374234567', 'member.7@fed-agri.mg', 'SENIOR', '2026-01-01'),
                                                                                                                                        ('C1-M8', 'Membre 8', 'Prénom 8', '1975-08-20', 'MALE', 'Lot UV 8 Ambato', 'Riziculteur', '0370234567', 'member.8@fed-agri.mg', 'SENIOR', '2026-01-01'),
                                                                                                                                        ('C3-M1', 'Membre 9', 'Prénom 9', '1988-01-02', 'MALE', 'Lot 33 J Antsirabe', 'Apiculteur', '034034567', 'member.9@fed-agri.mg', 'PRESIDENT', '2026-01-01'),
                                                                                                                                        ('C3-M2', 'Membre 10', 'Prénom 10', '1982-03-05', 'MALE', 'Lot 2 J Antsirabe', 'Agriculteur', '0338634567', 'member.10@fed-agri.mg', 'VICE_PRESIDENT', '2026-01-01'),
                                                                                                                                        ('C3-M3', 'Membre 11', 'Prénom 11', '1992-03-12', 'MALE', 'Lot 8 KM Antsirabe', 'Collecteur', '0338234567', 'member.11@fed-agri.mg', 'SECRETARY', '2026-01-01'),
                                                                                                                                        ('C3-M4', 'Membre 12', 'Prénom 12', '1988-05-10', 'FEMALE', 'Lot A K 50 Antsirabe', 'Distributeur', '0382334567', 'member.12@fed-agri.mg', 'TREASURER', '2026-01-01'),
                                                                                                                                        ('C3-M5', 'Membre 13', 'Prénom 13', '1999-08-11', 'MALE', 'Lot UV 80 Antsirabe', 'Apiculteur', '0373365567', 'member.13@fed-agri.mg', 'SENIOR', '2026-01-01'),
                                                                                                                                        ('C3-M6', 'Membre 14', 'Prénom 14', '1998-08-09', 'FEMALE', 'Lot UV 6 Antsirabe', 'Apiculteur', '0378234567', 'member.14@fed-agri.mg', 'SENIOR', '2026-01-01'),
                                                                                                                                        ('C3-M7', 'Membre 15', 'Prénom 15', '1998-01-13', 'MALE', 'Lot UV 7 Antsirabe', 'Apiculteur', '0374914567', 'member.15@fed-agri.mg', 'SENIOR', '2026-01-01'),
                                                                                                                                        ('C3-M8', 'Membre 16', 'Prénom 16', '1975-08-02', 'MALE', 'Lot UV 8 Antsirabe', 'Apiculteur', '0370634567', 'member.16@fed-agri.mg', 'SENIOR', '2026-01-01');

-- ------------------------------------------------------
-- 2. COLLECTIVITIES
-- ------------------------------------------------------
INSERT INTO collectivity (id, location, specialty, creation_date, federation_approval, number, name) VALUES
                                                                                                         ('col-1', 'Ambatondrazaka', 'Riziculture', '2024-01-01', true, '1', 'Mpanorina'),
                                                                                                         ('col-2', 'Ambatondrazaka', 'Pisciculture', '2024-01-01', true, '2', 'Dobo voalohany'),
                                                                                                         ('col-3', 'Brickaville', 'Apiculture', '2024-01-01', true, '3', 'Tantely mamy');

-- ------------------------------------------------------
-- 3. COLLECTIVITY-MEMBER LINKS
-- ------------------------------------------------------
INSERT INTO collectivity_member (collectivity_id, member_id) VALUES
                                                                 ('col-1', 'C1-M1'), ('col-1', 'C1-M2'), ('col-1', 'C1-M3'), ('col-1', 'C1-M4'),
                                                                 ('col-1', 'C1-M5'), ('col-1', 'C1-M6'), ('col-1', 'C1-M7'), ('col-1', 'C1-M8'),
                                                                 ('col-2', 'C1-M1'), ('col-2', 'C1-M2'), ('col-2', 'C1-M3'), ('col-2', 'C1-M4'),
                                                                 ('col-2', 'C1-M5'), ('col-2', 'C1-M6'), ('col-2', 'C1-M7'), ('col-2', 'C1-M8'),
                                                                 ('col-3', 'C3-M1'), ('col-3', 'C3-M2'), ('col-3', 'C3-M3'), ('col-3', 'C3-M4'),
                                                                 ('col-3', 'C3-M5'), ('col-3', 'C3-M6'), ('col-3', 'C3-M7'), ('col-3', 'C3-M8');

-- ------------------------------------------------------
-- 4. REFEREE RELATIONS
-- ------------------------------------------------------
INSERT INTO member_referee (member_id, referee_id, relation_type) VALUES
                                                                      ('C1-M3', 'C1-M1', NULL), ('C1-M3', 'C1-M2', NULL),
                                                                      ('C1-M4', 'C1-M1', NULL), ('C1-M4', 'C1-M2', NULL),
                                                                      ('C1-M5', 'C1-M1', NULL), ('C1-M5', 'C1-M2', NULL),
                                                                      ('C1-M6', 'C1-M1', NULL), ('C1-M6', 'C1-M2', NULL),
                                                                      ('C1-M7', 'C1-M1', NULL), ('C1-M7', 'C1-M2', NULL),
                                                                      ('C1-M8', 'C1-M6', NULL), ('C1-M8', 'C1-M7', NULL),
                                                                      ('C3-M3', 'C3-M1', NULL), ('C3-M3', 'C3-M2', NULL),
                                                                      ('C3-M4', 'C3-M1', NULL), ('C3-M4', 'C3-M2', NULL),
                                                                      ('C3-M5', 'C3-M1', NULL), ('C3-M5', 'C3-M2', NULL),
                                                                      ('C3-M6', 'C3-M1', NULL), ('C3-M6', 'C3-M2', NULL),
                                                                      ('C3-M7', 'C3-M1', NULL), ('C3-M7', 'C3-M2', NULL),
                                                                      ('C3-M8', 'C3-M1', NULL), ('C3-M8', 'C3-M2', NULL);

-- ------------------------------------------------------
-- 5. COLLECTIVITY STRUCTURES (president, vice, treasurer, secretary)
-- ------------------------------------------------------
INSERT INTO collectivity_structure (collectivity_id, president_id, vice_president_id, treasurer_id, secretary_id) VALUES
                                                                                                                      ('col-1', 'C1-M1', 'C1-M2', 'C1-M4', 'C1-M3'),
                                                                                                                      ('col-2', 'C1-M5', 'C1-M6', 'C1-M8', 'C1-M7'),
                                                                                                                      ('col-3', 'C3-M1', 'C3-M2', 'C3-M4', 'C3-M3');

-- ------------------------------------------------------
-- 6. FINANCIAL ACCOUNTS
-- ------------------------------------------------------
INSERT INTO financial_account (id, collectivity_id, type, amount, holder_name, mobile_banking_service, mobile_number, bank_name, bank_code, bank_branch_code, bank_account_number, bank_account_key) VALUES
                                                                                                                                                                                                         ('C1-A-CASH', 'col-1', 'CASH', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
                                                                                                                                                                                                         ('C1-A-MOBILE-1', 'col-1', 'MOBILE_BANKING', 0, 'Mpanorina', 'ORANGE_MONEY', '0370489612', NULL, NULL, NULL, NULL, NULL),
                                                                                                                                                                                                         ('C2-A-CASH', 'col-2', 'CASH', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
                                                                                                                                                                                                         ('C2-A-MOBILE-1', 'col-2', 'MOBILE_BANKING', 0, 'Dobo voalohany', 'ORANGE_MONEY', '0320489612', NULL, NULL, NULL, NULL, NULL),
                                                                                                                                                                                                         ('C3-A-CASH', 'col-3', 'CASH', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
                                                                                                                                                                                                         ('C3-A-BANK-1', 'col-3', 'BANK', 0, 'Koto', NULL, NULL, 'BMOI', 4, 1, 12345678901, 20),
                                                                                                                                                                                                         ('C3-A-BANK-2', 'col-3', 'BANK', 0, 'Naivo', NULL, NULL, 'BRED', 8, 3, 45678901235, 58),
                                                                                                                                                                                                         ('C3-A-MOBILE-1', 'col-3', 'MOBILE_BANKING', 0, 'Kolo', 'MVOLA', '0341889612', NULL, NULL, NULL, NULL, NULL);

-- ------------------------------------------------------
-- 7. MEMBERSHIP FEES
-- ------------------------------------------------------
INSERT INTO membership_fee (id, collectivity_id, eligible_from, frequency, amount, label, status) VALUES
                                                                                                      ('cot-1', 'col-1', '2026-01-01', 'ANNUALLY', 200000, 'Cotisation annuelle', 'ACTIVE'),
                                                                                                      ('cot-2', 'col-1', '2026-04-30', 'PUNCTUALLY', 20000, 'Famangiana', 'ACTIVE'),
                                                                                                      ('cot-3', 'col-2', '2026-01-01', 'ANNUALLY', 200000, 'Cotisation annuelle', 'ACTIVE'),
                                                                                                      ('cot-4', 'col-2', '2025-01-01', 'ANNUALLY', 100000, 'Cotisation 2025', 'INACTIVE'),
                                                                                                      ('cot-5', 'col-3', '2026-04-01', 'MONTHLY', 25000, 'Cotisation mensuelle', 'ACTIVE');

-- ------------------------------------------------------
-- 8. PAYMENTS & TRANSACTIONS (no CTEs, direct INSERTs)
-- ------------------------------------------------------

-- Collectivity 1 payments (Tableau 15)
INSERT INTO member_payment (id, member_id, membership_fee_id, amount, payment_mode, account_credited_id, creation_date) VALUES
                                                                                                                            ('pay_col1_1', 'C1-M1', 'cot-1', 200000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                                            ('pay_col1_2', 'C1-M2', 'cot-1', 200000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                                            ('pay_col1_3', 'C1-M3', 'cot-1', 200000, 'MOBILE_MONEY', 'C1-A-MOBILE-1', '2026-01-01'),
                                                                                                                            ('pay_col1_4', 'C1-M4', 'cot-1', 200000, 'MOBILE_MONEY', 'C1-A-MOBILE-1', '2026-01-01'),
                                                                                                                            ('pay_col1_5', 'C1-M5', 'cot-1', 150000, 'MOBILE_MONEY', 'C1-A-MOBILE-1', '2026-01-01'),
                                                                                                                            ('pay_col1_6', 'C1-M6', 'cot-1', 100000, 'CASH', 'C1-A-CASH', '2026-05-01'),
                                                                                                                            ('pay_col1_7', 'C1-M7', 'cot-1', 60000, 'CASH', 'C1-A-CASH', '2026-05-01'),
                                                                                                                            ('pay_col1_8', 'C1-M8', 'cot-1', 90000, 'CASH', 'C1-A-CASH', '2026-05-01');

INSERT INTO collectivity_transaction (id, collectivity_id, creation_date, amount, payment_mode, account_credited_id, member_debited_id) VALUES
                                                                                                                                            ('pay_col1_1', 'col-1', '2026-01-01', 200000, 'CASH', 'C1-A-CASH', 'C1-M1'),
                                                                                                                                            ('pay_col1_2', 'col-1', '2026-01-01', 200000, 'CASH', 'C1-A-CASH', 'C1-M2'),
                                                                                                                                            ('pay_col1_3', 'col-1', '2026-01-01', 200000, 'MOBILE_MONEY', 'C1-A-MOBILE-1', 'C1-M3'),
                                                                                                                                            ('pay_col1_4', 'col-1', '2026-01-01', 200000, 'MOBILE_MONEY', 'C1-A-MOBILE-1', 'C1-M4'),
                                                                                                                                            ('pay_col1_5', 'col-1', '2026-01-01', 150000, 'MOBILE_MONEY', 'C1-A-MOBILE-1', 'C1-M5'),
                                                                                                                                            ('pay_col1_6', 'col-1', '2026-05-01', 100000, 'CASH', 'C1-A-CASH', 'C1-M6'),
                                                                                                                                            ('pay_col1_7', 'col-1', '2026-05-01', 60000, 'CASH', 'C1-A-CASH', 'C1-M7'),
                                                                                                                                            ('pay_col1_8', 'col-1', '2026-05-01', 90000, 'CASH', 'C1-A-CASH', 'C1-M8');

-- Collectivity 2 payments (Tableau 16)
INSERT INTO member_payment (id, member_id, membership_fee_id, amount, payment_mode, account_credited_id, creation_date) VALUES
                                                                                                                            ('pay_col2_1', 'C1-M1', 'cot-3', 120000, 'CASH', 'C2-A-CASH', '2026-01-01'),
                                                                                                                            ('pay_col2_2', 'C1-M2', 'cot-3', 180000, 'CASH', 'C2-A-CASH', '2026-01-01'),
                                                                                                                            ('pay_col2_3', 'C1-M3', 'cot-3', 200000, 'CASH', 'C2-A-CASH', '2026-01-01'),
                                                                                                                            ('pay_col2_4', 'C1-M4', 'cot-3', 200000, 'CASH', 'C2-A-CASH', '2026-01-01'),
                                                                                                                            ('pay_col2_5', 'C1-M5', 'cot-3', 200000, 'CASH', 'C2-A-CASH', '2026-01-01'),
                                                                                                                            ('pay_col2_6', 'C1-M6', 'cot-3', 200000, 'CASH', 'C2-A-CASH', '2026-01-01'),
                                                                                                                            ('pay_col2_7', 'C1-M7', 'cot-3', 80000, 'MOBILE_MONEY', 'C2-A-MOBILE-1', '2026-01-01'),
                                                                                                                            ('pay_col2_8', 'C1-M8', 'cot-3', 120000, 'MOBILE_MONEY', 'C2-A-MOBILE-1', '2026-01-01');

INSERT INTO collectivity_transaction (id, collectivity_id, creation_date, amount, payment_mode, account_credited_id, member_debited_id) VALUES
                                                                                                                                            ('pay_col2_1', 'col-2', '2026-01-01', 120000, 'CASH', 'C2-A-CASH', 'C1-M1'),
                                                                                                                                            ('pay_col2_2', 'col-2', '2026-01-01', 180000, 'CASH', 'C2-A-CASH', 'C1-M2'),
                                                                                                                                            ('pay_col2_3', 'col-2', '2026-01-01', 200000, 'CASH', 'C2-A-CASH', 'C1-M3'),
                                                                                                                                            ('pay_col2_4', 'col-2', '2026-01-01', 200000, 'CASH', 'C2-A-CASH', 'C1-M4'),
                                                                                                                                            ('pay_col2_5', 'col-2', '2026-01-01', 200000, 'CASH', 'C2-A-CASH', 'C1-M5'),
                                                                                                                                            ('pay_col2_6', 'col-2', '2026-01-01', 200000, 'CASH', 'C2-A-CASH', 'C1-M6'),
                                                                                                                                            ('pay_col2_7', 'col-2', '2026-01-01', 80000, 'MOBILE_MONEY', 'C2-A-MOBILE-1', 'C1-M7'),
                                                                                                                                            ('pay_col2_8', 'col-2', '2026-01-01', 120000, 'MOBILE_MONEY', 'C2-A-MOBILE-1', 'C1-M8');

-- Collectivity 3 payments (Tableau 17)
INSERT INTO member_payment (id, member_id, membership_fee_id, amount, payment_mode, account_credited_id, creation_date) VALUES
                                                                                                                            ('pay_col3_1', 'C3-M1', 'cot-5', 25000, 'BANK', 'C3-A-BANK-1', '2026-04-01'),
                                                                                                                            ('pay_col3_2', 'C3-M2', 'cot-5', 25000, 'BANK', 'C3-A-BANK-1', '2026-04-01'),
                                                                                                                            ('pay_col3_3', 'C3-M3', 'cot-5', 25000, 'BANK', 'C3-A-BANK-1', '2026-04-01'),
                                                                                                                            ('pay_col3_4', 'C3-M4', 'cot-5', 25000, 'BANK', 'C3-A-BANK-1', '2026-04-01'),
                                                                                                                            ('pay_col3_5', 'C3-M5', 'cot-5', 25000, 'BANK', 'C3-A-BANK-2', '2026-04-01'),
                                                                                                                            ('pay_col3_6', 'C3-M6', 'cot-5', 25000, 'BANK', 'C3-A-BANK-2', '2026-04-01'),
                                                                                                                            ('pay_col3_7', 'C3-M7', 'cot-5', 25000, 'CASH', 'C3-A-CASH', '2026-04-01'),
                                                                                                                            ('pay_col3_8', 'C3-M8', 'cot-5', 25000, 'CASH', 'C3-A-CASH', '2026-04-01'),
                                                                                                                            ('pay_col3_9', 'C3-M1', 'cot-5', 25000, 'BANK', 'C3-A-BANK-1', '2026-05-01'),
                                                                                                                            ('pay_col3_10', 'C3-M2', 'cot-5', 25000, 'BANK', 'C3-A-BANK-1', '2026-05-01'),
                                                                                                                            ('pay_col3_11', 'C3-M3', 'cot-5', 15000, 'MOBILE_MONEY', 'C3-A-MOBILE-1', '2026-05-01'),
                                                                                                                            ('pay_col3_12', 'C3-M4', 'cot-5', 15000, 'MOBILE_MONEY', 'C3-A-MOBILE-1', '2026-05-01'),
                                                                                                                            ('pay_col3_13', 'C3-M5', 'cot-5', 20000, 'BANK', 'C3-A-BANK-2', '2026-05-01'),
                                                                                                                            ('pay_col3_14', 'C3-M6', 'cot-5', 25000, 'BANK', 'C3-A-BANK-2', '2026-05-01'),
                                                                                                                            ('pay_col3_15', 'C3-M7', 'cot-5', 5000, 'CASH', 'C3-A-CASH', '2026-05-01'),
                                                                                                                            ('pay_col3_16', 'C3-M8', 'cot-5', 5000, 'CASH', 'C3-A-CASH', '2026-05-01');

INSERT INTO collectivity_transaction (id, collectivity_id, creation_date, amount, payment_mode, account_credited_id, member_debited_id) VALUES
                                                                                                                                            ('pay_col3_1', 'col-3', '2026-04-01', 25000, 'BANK', 'C3-A-BANK-1', 'C3-M1'),
                                                                                                                                            ('pay_col3_2', 'col-3', '2026-04-01', 25000, 'BANK', 'C3-A-BANK-1', 'C3-M2'),
                                                                                                                                            ('pay_col3_3', 'col-3', '2026-04-01', 25000, 'BANK', 'C3-A-BANK-1', 'C3-M3'),
                                                                                                                                            ('pay_col3_4', 'col-3', '2026-04-01', 25000, 'BANK', 'C3-A-BANK-1', 'C3-M4'),
                                                                                                                                            ('pay_col3_5', 'col-3', '2026-04-01', 25000, 'BANK', 'C3-A-BANK-2', 'C3-M5'),
                                                                                                                                            ('pay_col3_6', 'col-3', '2026-04-01', 25000, 'BANK', 'C3-A-BANK-2', 'C3-M6'),
                                                                                                                                            ('pay_col3_7', 'col-3', '2026-04-01', 25000, 'CASH', 'C3-A-CASH', 'C3-M7'),
                                                                                                                                            ('pay_col3_8', 'col-3', '2026-04-01', 25000, 'CASH', 'C3-A-CASH', 'C3-M8'),
                                                                                                                                            ('pay_col3_9', 'col-3', '2026-05-01', 25000, 'BANK', 'C3-A-BANK-1', 'C3-M1'),
                                                                                                                                            ('pay_col3_10', 'col-3', '2026-05-01', 25000, 'BANK', 'C3-A-BANK-1', 'C3-M2'),
                                                                                                                                            ('pay_col3_11', 'col-3', '2026-05-01', 15000, 'MOBILE_MONEY', 'C3-A-MOBILE-1', 'C3-M3'),
                                                                                                                                            ('pay_col3_12', 'col-3', '2026-05-01', 15000, 'MOBILE_MONEY', 'C3-A-MOBILE-1', 'C3-M4'),
                                                                                                                                            ('pay_col3_13', 'col-3', '2026-05-01', 20000, 'BANK', 'C3-A-BANK-2', 'C3-M5'),
                                                                                                                                            ('pay_col3_14', 'col-3', '2026-05-01', 25000, 'BANK', 'C3-A-BANK-2', 'C3-M6'),
                                                                                                                                            ('pay_col3_15', 'col-3', '2026-05-01', 5000, 'CASH', 'C3-A-CASH', 'C3-M7'),
                                                                                                                                            ('pay_col3_16', 'col-3', '2026-05-01', 5000, 'CASH', 'C3-A-CASH', 'C3-M8');

-- ------------------------------------------------------
-- 9. NEW JUNIOR MEMBERS (Tables 18, 19, 20)
-- ------------------------------------------------------

-- Collectivity 1 (4 new juniors)
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, join_date) VALUES
                                                                                                                                        ('NEW_COL1_M1', 'Nouveau1', 'Nom1', '1990-01-01', 'MALE', 'Adresse random 1', 'Agriculteur', '0310000001', 'new1@example.com', 'JUNIOR', '2026-04-01'),
                                                                                                                                        ('NEW_COL1_M2', 'Nouveau2', 'Nom2', '1991-02-02', 'FEMALE', 'Adresse random 2', 'Commerçant', '0310000002', 'new2@example.com', 'JUNIOR', '2026-04-01'),
                                                                                                                                        ('NEW_COL1_M3', 'Nouveau3', 'Nom3', '1992-03-03', 'MALE', 'Adresse random 3', 'Éleveur', '0310000003', 'new3@example.com', 'JUNIOR', '2026-05-01'),
                                                                                                                                        ('NEW_COL1_M4', 'Nouveau4', 'Nom4', '1993-04-04', 'FEMALE', 'Adresse random 4', 'Pêcheur', '0310000004', 'new4@example.com', 'JUNIOR', '2026-06-01');

INSERT INTO collectivity_member (collectivity_id, member_id) VALUES
                                                                 ('col-1', 'NEW_COL1_M1'), ('col-1', 'NEW_COL1_M2'), ('col-1', 'NEW_COL1_M3'), ('col-1', 'NEW_COL1_M4');

INSERT INTO member_referee (member_id, referee_id, relation_type) VALUES
                                                                      ('NEW_COL1_M1', 'C1-M1', NULL), ('NEW_COL1_M1', 'C1-M2', NULL),
                                                                      ('NEW_COL1_M2', 'C1-M1', NULL), ('NEW_COL1_M2', 'C1-M2', NULL),
                                                                      ('NEW_COL1_M3', 'C1-M1', NULL), ('NEW_COL1_M3', 'C1-M2', NULL),
                                                                      ('NEW_COL1_M4', 'C1-M1', NULL), ('NEW_COL1_M4', 'C1-M2', NULL);

-- Collectivity 2 (3 new juniors)
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, join_date) VALUES
                                                                                                                                        ('NEW_COL2_M1', 'NouveauC2_1', 'NomC2_1', '1995-01-01', 'MALE', 'Adresse C2 1', 'Riziculteur', '0321000001', 'newc2_1@example.com', 'JUNIOR', '2026-03-01'),
                                                                                                                                        ('NEW_COL2_M2', 'NouveauC2_2', 'NomC2_2', '1996-02-02', 'FEMALE', 'Adresse C2 2', 'Agriculteur', '0321000002', 'newc2_2@example.com', 'JUNIOR', '2026-03-01'),
                                                                                                                                        ('NEW_COL2_M3', 'NouveauC2_3', 'NomC2_3', '1997-03-03', 'MALE', 'Adresse C2 3', 'Pisciculteur', '0321000003', 'newc2_3@example.com', 'JUNIOR', '2026-03-01');

INSERT INTO collectivity_member (collectivity_id, member_id) VALUES
                                                                 ('col-2', 'NEW_COL2_M1'), ('col-2', 'NEW_COL2_M2'), ('col-2', 'NEW_COL2_M3');

INSERT INTO member_referee (member_id, referee_id, relation_type) VALUES
                                                                      ('NEW_COL2_M1', 'C1-M1', NULL), ('NEW_COL2_M1', 'C1-M2', NULL),
                                                                      ('NEW_COL2_M2', 'C1-M1', NULL), ('NEW_COL2_M2', 'C1-M2', NULL),
                                                                      ('NEW_COL2_M3', 'C1-M1', NULL), ('NEW_COL2_M3', 'C1-M2', NULL);

-- Collectivity 3 (6 new juniors)
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, join_date) VALUES
                                                                                                                                        ('NEW_COL3_M1', 'NouveauC3_1', 'NomC3_1', '1998-01-01', 'MALE', 'Adresse C3 1', 'Apiculteur', '0331000001', 'newc3_1@example.com', 'JUNIOR', '2026-01-01'),
                                                                                                                                        ('NEW_COL3_M2', 'NouveauC3_2', 'NomC3_2', '1999-02-02', 'FEMALE', 'Adresse C3 2', 'Agriculteur', '0331000002', 'newc3_2@example.com', 'JUNIOR', '2026-02-01'),
                                                                                                                                        ('NEW_COL3_M3', 'NouveauC3_3', 'NomC3_3', '2000-03-03', 'MALE', 'Adresse C3 3', 'Éleveur', '0331000003', 'newc3_3@example.com', 'JUNIOR', '2026-02-01'),
                                                                                                                                        ('NEW_COL3_M4', 'NouveauC3_4', 'NomC3_4', '2001-04-04', 'FEMALE', 'Adresse C3 4', 'Apiculteur', '0331000004', 'newc3_4@example.com', 'JUNIOR', '2026-03-01'),
                                                                                                                                        ('NEW_COL3_M5', 'NouveauC3_5', 'NomC3_5', '2002-05-05', 'MALE', 'Adresse C3 5', 'Collecteur', '0331000005', 'newc3_5@example.com', 'JUNIOR', '2026-03-01'),
                                                                                                                                        ('NEW_COL3_M6', 'NouveauC3_6', 'NomC3_6', '2003-06-06', 'FEMALE', 'Adresse C3 6', 'Distributeur', '0331000006', 'newc3_6@example.com', 'JUNIOR', '2026-03-01');

INSERT INTO collectivity_member (collectivity_id, member_id) VALUES
                                                                 ('col-3', 'NEW_COL3_M1'), ('col-3', 'NEW_COL3_M2'), ('col-3', 'NEW_COL3_M3'),
                                                                 ('col-3', 'NEW_COL3_M4'), ('col-3', 'NEW_COL3_M5'), ('col-3', 'NEW_COL3_M6');

INSERT INTO member_referee (member_id, referee_id, relation_type) VALUES
                                                                      ('NEW_COL3_M1', 'C3-M1', NULL), ('NEW_COL3_M1', 'C3-M2', NULL),
                                                                      ('NEW_COL3_M2', 'C3-M1', NULL), ('NEW_COL3_M2', 'C3-M2', NULL),
                                                                      ('NEW_COL3_M3', 'C3-M1', NULL), ('NEW_COL3_M3', 'C3-M2', NULL),
                                                                      ('NEW_COL3_M4', 'C3-M1', NULL), ('NEW_COL3_M4', 'C3-M2', NULL),
                                                                      ('NEW_COL3_M5', 'C3-M1', NULL), ('NEW_COL3_M5', 'C3-M2', NULL),
                                                                      ('NEW_COL3_M6', 'C3-M1', NULL), ('NEW_COL3_M6', 'C3-M2', NULL);
