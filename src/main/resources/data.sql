-- ============================================================
-- Insertion des données de test pour PostgreSQL
-- ============================================================

-- Activer l'extension pour les UUID aléatoires (si nécessaire)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ------------------------------------------------------------
-- 1. Membres (member)
-- ------------------------------------------------------------
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, join_date) VALUES
                                                                                                                                        ('C1-M1', 'Nom1', 'Prénom1', '1980-02-01', 'MALE', 'Lot II V M Ambato', 'Riziculteur', '0341234567', 'member.1@fed-agri.mg', 'PRESIDENT', '2026-01-01'),
                                                                                                                                        ('C1-M2', 'Nom2', 'Prénom2', '1982-03-05', 'MALE', 'Lot II F Ambato', 'Agriculteur', '0321234567', 'member.2@fed-agri.mg', 'VICE_PRESIDENT', '2026-01-01'),
                                                                                                                                        ('C1-M3', 'Nom3', 'Prénom3', '1992-03-10', 'MALE', 'Lot II J Ambato', 'Collecteur', '0331234567', 'member.3@fed-agri.mg', 'SECRETARY', '2026-01-01'),
                                                                                                                                        ('C1-M4', 'Nom4', 'Prénom4', '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato', 'Distributeur', '0381234567', 'member.4@fed-agri.mg', 'TREASURER', '2026-01-01'),
                                                                                                                                        ('C1-M5', 'Nom5', 'Prénom5', '1999-08-21', 'MALE', 'Lot UV 80 Ambato', 'Riziculteur', '0373434567', 'member.5@fed-agri.mg', 'SENIOR', '2026-01-01'),
                                                                                                                                        ('C1-M6', 'Nom6', 'Prénom6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato', 'Riziculteur', '0372234567', 'member.6@fed-agri.mg', 'SENIOR', '2026-01-01'),
                                                                                                                                        ('C1-M7', 'Nom7', 'Prénom7', '1998-01-31', 'MALE', 'Lot UV 7 Ambato', 'Riziculteur', '0374234567', 'member.7@fed-agri.mg', 'SENIOR', '2026-01-01'),
                                                                                                                                        ('C1-M8', 'Nom8', 'Prénom8', '1975-08-20', 'MALE', 'Lot UV 8 Ambato', 'Riziculteur', '0370234567', 'member.8@fed-agri.mg', 'SENIOR', '2026-01-01');

INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, join_date) VALUES
                                                                                                                                        ('C3-M1', 'Nom9', 'Prénom9', '1988-01-02', 'MALE', 'Lot 33 J Antsirabe', 'Apiculteur', '034034567', 'member.9@fed-agri.mg', 'PRESIDENT', '2026-01-01'),
                                                                                                                                        ('C3-M2', 'Nom10', 'Prénom10', '1982-03-05', 'MALE', 'Lot 2 J Antsirabe', 'Agriculteur', '0338634567', 'member.10@fed-agri.mg', 'VICE_PRESIDENT', '2026-01-01'),
                                                                                                                                        ('C3-M3', 'Nom11', 'Prénom11', '1992-03-12', 'MALE', 'Lot 8 KM Antsirabe', 'Collecteur', '0338234567', 'member.11@fed-agri.mg', 'SECRETARY', '2026-01-01'),
                                                                                                                                        ('C3-M4', 'Nom12', 'Prénom12', '1988-05-10', 'FEMALE', 'Lot A K 50 Antsirabe', 'Distributeur', '0382334567', 'member.12@fed-agri.mg', 'TREASURER', '2026-01-01'),
                                                                                                                                        ('C3-M5', 'Nom13', 'Prénom13', '1999-08-11', 'MALE', 'Lot UV 80 Antsirabe', 'Apiculteur', '0373365567', 'member.13@fed-agri.mg', 'SENIOR', '2026-01-01'),
                                                                                                                                        ('C3-M6', 'Nom14', 'Prénom14', '1998-08-09', 'FEMALE', 'Lot UV 6 Antsirabe', 'Apiculteur', '0378234567', 'member.14@fed-agri.mg', 'SENIOR', '2026-01-01'),
                                                                                                                                        ('C3-M7', 'Nom15', 'Prénom15', '1998-01-13', 'MALE', 'Lot UV 7 Antsirabe', 'Apiculteur', '0374914567', 'member.15@fed-agri.mg', 'SENIOR', '2026-01-01'),
                                                                                                                                        ('C3-M8', 'Nom16', 'Prénom16', '1975-08-02', 'MALE', 'Lot UV 8 Antsirabe', 'Apiculteur', '0370634567', 'member.16@fed-agri.mg', 'SENIOR', '2026-01-01');

-- ------------------------------------------------------------
-- 2. Collectivités (collectivity)
-- ------------------------------------------------------------
INSERT INTO collectivity (id, location, specialty, creation_date, federation_approval, number, name) VALUES
                                                                                                         ('col-1', 'Ambatondrazaka', 'Riziculture', '2026-01-01', TRUE, '1', 'Mpanorina'),
                                                                                                         ('col-2', 'Ambatondrazaka', 'Pisciculture', '2026-01-01', TRUE, '2', 'Dobo voalahany'),
                                                                                                         ('col-3', 'Brickaville', 'Apiculture', '2026-01-01', TRUE, '3', 'Tantely mamy');

-- ------------------------------------------------------------
-- 3. Relations collectivité-membre (collectivity_member)
-- ------------------------------------------------------------
INSERT INTO collectivity_member (collectivity_id, member_id) VALUES
                                                                 ('col-1', 'C1-M1'), ('col-1', 'C1-M2'), ('col-1', 'C1-M3'), ('col-1', 'C1-M4'),
                                                                 ('col-1', 'C1-M5'), ('col-1', 'C1-M6'), ('col-1', 'C1-M7'), ('col-1', 'C1-M8');

INSERT INTO collectivity_member (collectivity_id, member_id) VALUES
                                                                 ('col-2', 'C1-M1'), ('col-2', 'C1-M2'), ('col-2', 'C1-M3'), ('col-2', 'C1-M4'),
                                                                 ('col-2', 'C1-M5'), ('col-2', 'C1-M6'), ('col-2', 'C1-M7'), ('col-2', 'C1-M8');

INSERT INTO collectivity_member (collectivity_id, member_id) VALUES
                                                                 ('col-3', 'C3-M1'), ('col-3', 'C3-M2'), ('col-3', 'C3-M3'), ('col-3', 'C3-M4'),
                                                                 ('col-3', 'C3-M5'), ('col-3', 'C3-M6'), ('col-3', 'C3-M7'), ('col-3', 'C3-M8');

-- ------------------------------------------------------------
-- 4. Structure des collectivités (collectivity_structure)
-- ------------------------------------------------------------
INSERT INTO collectivity_structure (collectivity_id, president_id, vice_president_id, treasurer_id, secretary_id) VALUES
    ('col-1', 'C1-M1', 'C1-M2', 'C1-M4', 'C1-M3');

INSERT INTO collectivity_structure (collectivity_id, president_id, vice_president_id, treasurer_id, secretary_id) VALUES
    ('col-2', 'C1-M5', 'C1-M6', 'C1-M8', 'C1-M7');

INSERT INTO collectivity_structure (collectivity_id, president_id, vice_president_id, treasurer_id, secretary_id) VALUES
    ('col-3', 'C3-M1', 'C3-M2', 'C3-M4', 'C3-M3');

-- ------------------------------------------------------------
-- 5. Relations de parrainage (member_referee)
-- ------------------------------------------------------------
INSERT INTO member_referee (member_id, referee_id, relation_type) VALUES
                                                                      ('C1-M3', 'C1-M1', NULL), ('C1-M3', 'C1-M2', NULL),
                                                                      ('C1-M4', 'C1-M1', NULL), ('C1-M4', 'C1-M2', NULL),
                                                                      ('C1-M5', 'C1-M1', NULL), ('C1-M5', 'C1-M2', NULL),
                                                                      ('C1-M6', 'C1-M1', NULL), ('C1-M6', 'C1-M2', NULL),
                                                                      ('C1-M7', 'C1-M1', NULL), ('C1-M7', 'C1-M2', NULL),
                                                                      ('C1-M8', 'C1-M6', NULL), ('C1-M8', 'C1-M7', NULL);

INSERT INTO member_referee (member_id, referee_id, relation_type) VALUES
                                                                      ('C3-M3', 'C3-M1', NULL), ('C3-M3', 'C3-M2', NULL),
                                                                      ('C3-M4', 'C3-M1', NULL), ('C3-M4', 'C3-M2', NULL),
                                                                      ('C3-M5', 'C3-M1', NULL), ('C3-M5', 'C3-M2', NULL),
                                                                      ('C3-M6', 'C3-M1', NULL), ('C3-M6', 'C3-M2', NULL),
                                                                      ('C3-M7', 'C3-M1', NULL), ('C3-M7', 'C3-M2', NULL),
                                                                      ('C3-M8', 'C3-M1', NULL), ('C3-M8', 'C3-M2', NULL);

-- ------------------------------------------------------------
-- 6. Cotisations (membership_fee)
-- ------------------------------------------------------------
INSERT INTO membership_fee (id, collectivity_id, eligible_from, frequency, amount, label, status) VALUES
                                                                                                      ('cot-1', 'col-1', '2026-01-01', 'ANNUALLY', 100000, 'Cotisation annuelle', 'ACTIVE'),
                                                                                                      ('cot-2', 'col-2', '2026-01-01', 'ANNUALLY', 100000, 'Cotisation annuelle', 'ACTIVE'),
                                                                                                      ('cot-3', 'col-3', '2026-01-01', 'ANNUALLY', 50000, 'Cotisation annuelle', 'ACTIVE');

-- ------------------------------------------------------------
-- 7. Comptes financiers (financial_account)
-- ------------------------------------------------------------
INSERT INTO financial_account (id, collectivity_id, type, amount, holder_name, mobile_banking_service, mobile_number) VALUES
                                                                                                                          ('C1-A-CASH', 'col-1', 'CASH', 0, NULL, NULL, NULL),
                                                                                                                          ('C1-A-MOBILE-1', 'col-1', 'MOBILE_BANKING', 0, 'Mpanorina', 'ORANGE_MONEY', '0370489612'),
                                                                                                                          ('C2-A-CASH', 'col-2', 'CASH', 0, NULL, NULL, NULL),
                                                                                                                          ('C2-A-MOBILE-1', 'col-2', 'MOBILE_BANKING', 0, 'Dobo voalohany', 'ORANGE_MONEY', '0320489612'),
                                                                                                                          ('C3-A-CASH', 'col-3', 'CASH', 0, NULL, NULL, NULL);

-- ------------------------------------------------------------
-- 8. Paiements des membres (member_payment) et transactions (collectivity_transaction)
-- ------------------------------------------------------------
-- Collectivité 1
INSERT INTO member_payment (id, member_id, membership_fee_id, amount, payment_mode, account_credited_id, creation_date) VALUES
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M1', 'cot-1', 100000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M2', 'cot-1', 100000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M3', 'cot-1', 100000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M4', 'cot-1', 100000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M5', 'cot-1', 100000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M6', 'cot-1', 100000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M7', 'cot-1', 60000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M8', 'cot-1', 90000, 'CASH', 'C1-A-CASH', '2026-01-01');

INSERT INTO collectivity_transaction (id, collectivity_id, creation_date, amount, payment_mode, account_credited_id, member_debited_id) VALUES
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-1', '2026-01-01', 100000, 'CASH', 'C1-A-CASH', 'C1-M1'),
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-1', '2026-01-01', 100000, 'CASH', 'C1-A-CASH', 'C1-M2'),
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-1', '2026-01-01', 100000, 'CASH', 'C1-A-CASH', 'C1-M3'),
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-1', '2026-01-01', 100000, 'CASH', 'C1-A-CASH', 'C1-M4'),
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-1', '2026-01-01', 100000, 'CASH', 'C1-A-CASH', 'C1-M5'),
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-1', '2026-01-01', 100000, 'CASH', 'C1-A-CASH', 'C1-M6'),
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-1', '2026-01-01', 60000, 'CASH', 'C1-A-CASH', 'C1-M7'),
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-1', '2026-01-01', 90000, 'CASH', 'C1-A-CASH', 'C1-M8');

-- Collectivité 2
INSERT INTO member_payment (id, member_id, membership_fee_id, amount, payment_mode, account_credited_id, creation_date) VALUES
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M1', 'cot-2', 60000, 'CASH', 'C2-A-CASH', '2026-01-01'),
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M2', 'cot-2', 90000, 'CASH', 'C2-A-CASH', '2026-01-01'),
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M3', 'cot-2', 100000, 'CASH', 'C2-A-CASH', '2026-01-01'),
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M4', 'cot-2', 100000, 'CASH', 'C2-A-CASH', '2026-01-01'),
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M5', 'cot-2', 100000, 'CASH', 'C2-A-CASH', '2026-01-01'),
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M6', 'cot-2', 100000, 'CASH', 'C2-A-CASH', '2026-01-01'),
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M7', 'cot-2', 40000, 'MOBILE_BANKING', 'C2-A-MOBILE-1', '2026-01-01'),
                                                                                                                            (gen_random_uuid()::VARCHAR, 'C1-M8', 'cot-2', 60000, 'MOBILE_BANKING', 'C2-A-MOBILE-1', '2026-01-01');

INSERT INTO collectivity_transaction (id, collectivity_id, creation_date, amount, payment_mode, account_credited_id, member_debited_id) VALUES
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-2', '2026-01-01', 60000, 'CASH', 'C2-A-CASH', 'C1-M1'),
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-2', '2026-01-01', 90000, 'CASH', 'C2-A-CASH', 'C1-M2'),
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-2', '2026-01-01', 100000, 'CASH', 'C2-A-CASH', 'C1-M3'),
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-2', '2026-01-01', 100000, 'CASH', 'C2-A-CASH', 'C1-M4'),
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-2', '2026-01-01', 100000, 'CASH', 'C2-A-CASH', 'C1-M5'),
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-2', '2026-01-01', 100000, 'CASH', 'C2-A-CASH', 'C1-M6'),
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-2', '2026-01-01', 40000, 'MOBILE_BANKING', 'C2-A-MOBILE-1', 'C1-M7'),
                                                                                                                                            (gen_random_uuid()::VARCHAR, 'col-2', '2026-01-01', 60000, 'MOBILE_BANKING', 'C2-A-MOBILE-1', 'C1-M8');

-- Collectivité 3 : aucun paiement ni transaction

select * from collectivity_activity;

DELETE FROM collectivity_activity;




-- =========================================================
-- ACTIVITÉS
-- =========================================================

INSERT INTO collectivity_activity (
    id,
    collectivity_id,
    label,
    activity_type,
    executive_date,
    week_ordinal,
    day_of_week
)
VALUES
    (
        'act-col1-meeting-1',
        'col-1',
        'Réunion hebdomadaire',
        'MEETING',
        '2026-05-20',
        NULL,
        NULL
    ),
    (
        'act-col1-training-1',
        'col-1',
        'Formation agricole',
        'TRAINING',
        NULL,
        2,
        'SA'
    ),
    (
        'act-col3-meeting-1',
        'col-3',
        'Assemblée générale',
        'MEETING',
        '2026-06-10',
        NULL,
        NULL
    );

-- =========================================================
-- OCCUPATIONS CONCERNÉES
-- =========================================================

INSERT INTO activity_member_concerned (
    activity_id,
    occupation
)
VALUES
    ('act-col1-meeting-1', 'PRESIDENT'),
    ('act-col1-meeting-1', 'SECRETARY'),

    ('act-col1-training-1', 'JUNIOR'),
    ('act-col1-training-1', 'SENIOR'),

    ('act-col3-meeting-1', 'PRESIDENT'),
    ('act-col3-meeting-1', 'TREASURER');

-- =========================================================
-- PRÉSENCES / ABSENCES
-- =========================================================

INSERT INTO activity_member_attendance (
    id,
    activity_id,
    member_id,
    attendance_status
)
VALUES
    (
        gen_random_uuid()::VARCHAR,
        'act-col1-meeting-1',
        'C1-M1',
        'PRESENT'
    ),
    (
        gen_random_uuid()::VARCHAR,
        'act-col1-meeting-1',
        'C1-M2',
        'ABSENT'
    ),
    (
        gen_random_uuid()::VARCHAR,
        'act-col1-training-1',
        'C1-M5',
        'PRESENT'
    ),
    (
        gen_random_uuid()::VARCHAR,
        'act-col3-meeting-1',
        'C3-M1',
        'PRESENT'
    );