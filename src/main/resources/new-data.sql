-- ============================================================
-- Insertion des collectivités (collectivity)
-- ============================================================

INSERT INTO collectivity (id, location, specialty, creation_date, federation_approval, number, name) VALUES
    ('col-1', 'Ambatondrazaka', 'Riziculture', '2026-01-01', TRUE, '1', 'Mpanorina'),
    ('col-2', 'Ambatondrazaka', 'Pisciculture', '2026-01-01', TRUE, '2', 'Dobo voalohany'),
    ('col-3', 'Brickaville', 'Apiculture', '2026-01-01', TRUE, '3', 'Tantely mamy');

INSERT INTO member (id,first_name,last_name,birth_date,gender,address,profession,phone_number,email,occupation) VALUES
      ('C1-M1', 'Prénom membre 1', 'Nom membre 1', '1980-02-01', 'MALE', 'Lot II V Ambato', 'Riziculteur', '0341234567', 'member.1@fed-agri.mg', 'PRESIDENT'),
      ('C1-M2', 'Prénom membre 2', 'Nom membre 2', '1982-03-05', 'MALE', 'Lot II F Ambato', 'Agriculteur', '0321234567', 'member.2@fed-agri.mg', 'VICE_PRESIDENT'),
      ('C1-M3', 'Prénom membre 3', 'Nom membre 3', '1992-03-10', 'MALE', 'Lot II J Ambato', 'Collecteur', '0331234567', 'member.3@fed-agri.mg', 'SECRETARY'),
      ('C1-M4', 'Prénom membre 4', 'Nom membre 4', '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato', 'Distributeur', '0381234567', 'member.4@fed-agri.mg', 'TREASURER'),
      ('C1-M5', 'Prénom membre 5', 'Nom membre 5', '1999-08-21', 'MALE', 'Lot UV 80 Ambato', 'Riziculteur', '0373434567', 'member.5@fed-agri.mg', 'SENIOR'),
      ('C1-M6', 'Prénom membre 6', 'Nom membre 6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato', 'Riziculteur', '0372234567', 'member.6@fed-agri.mg', 'SENIOR'),
      ('C1-M7', 'Prénom membre 7', 'Nom membre 7', '1998-01-31', 'MALE', 'Lot UV 7 Ambato', 'Riziculteur', '0374234567', 'member.7@fed-agri.mg', 'SENIOR'),
      ('C1-M8', 'Prénom membre 8', 'Nom membre 8', '1975-08-20', 'MALE', 'Lot UV 8 Ambato', 'Riziculteur', '0370234567', 'member.8@fed-agri.mg', 'SENIOR');

INSERT INTO collectivity_member (collectivity_id, member_id)
SELECT 'col-1', id FROM member WHERE id LIKE 'C1-M%';

INSERT INTO member_referee (member_id, referee_id, relation_type) VALUES
-- C1-M3 référencé par M1 et M2
('C1-M3', 'C1-M1', 'SUPERVISOR'),
('C1-M3', 'C1-M2', 'SUPERVISOR'),
-- C1-M4
('C1-M4', 'C1-M1', 'SUPERVISOR'),
('C1-M4', 'C1-M2', 'SUPERVISOR'),
-- C1-M5
('C1-M5', 'C1-M1', 'SUPERVISOR'),
('C1-M5', 'C1-M2', 'SUPERVISOR'),
-- C1-M6
('C1-M6', 'C1-M1', 'SUPERVISOR'),
('C1-M6', 'C1-M2', 'SUPERVISOR'),
-- C1-M7
('C1-M7', 'C1-M1', 'SUPERVISOR'),
('C1-M7', 'C1-M2', 'SUPERVISOR'),
-- C1-M8 référencé par M6 et M7
('C1-M8', 'C1-M6', 'SUPERVISOR'),
('C1-M8', 'C1-M7', 'SUPERVISOR');

INSERT INTO collectivity_structure (collectivity_id, president_id,vice_president_id,treasurer_id,secretary_id
) VALUES ('col-1','C1-M1','C1-M2','C1-M4','C1-M3');

INSERT INTO collectivity_member (collectivity_id, member_id)
SELECT 'col-2', id FROM member WHERE id IN ('C1-M1','C1-M2','C1-M3','C1-M4','C1-M5','C1-M6','C1-M7','C1-M8'
    );

INSERT INTO member (id,first_name,last_name,birth_date,gender,address,profession,phone_number,email,occupation) VALUES
      ('C3-M1', 'Prénom membre 9', 'Nom membre 9', '1988-01-02', 'MALE', 'Lot 33 J Antsirabe', 'Apiculteur', '034034567', 'member.9@fed-agri.mg', 'PRESIDENT'),
      ('C3-M2', 'Prénom membre 10', 'Nom membre 10', '1982-03-05', 'MALE', 'Lot 2 J Antsirabe', 'Agriculteur', '0338634567', 'member.10@fed-agri.mg', 'VICE_PRESIDENT'),
      ('C3-M3', 'Prénom membre 11', 'Nom membre 11', '1992-03-12', 'MALE', 'Lot 8 KM Antsirabe', 'Collecteur', '0338234567', 'member.11@fed-agrimg', 'SECRETARY'),
      ('C3-M4', 'Prénom membre 12', 'Nom membre 12', '1988-05-10', 'FEMALE', 'Lot A K 50 Antsirabe', 'Distributeur', '0382334567', 'member.12@fed-agri.mg', 'TREASURER'),
      ('C3-M5', 'Prénom membre 13', 'Nom membre 13', '1999-08-11', 'MALE', 'Lot UV 80 Antsirabe', 'Apiculteur', '0373365567', 'member.13@fed-agri.mg', 'SENIOR'),
      ('C3-M6', 'Prénom membre 14', 'Nom membre 14', '1998-08-09', 'FEMALE', 'Lot UV 6 Antsirabe', 'Apiculteur', '0378234567', 'member.14@fed-agri.mg', 'SENIOR'),
      ('C3-M7', 'Prénom membre 15', 'Nom membre 15', '1998-01-13', 'MALE', 'Lot UV 7 Antsirabe', 'Apiculteur', '0374914567', 'member.15@fed-agri.mg', 'SENIOR'),
      ('C3-M8', 'Prénom membre 16', 'Nom membre 16', '1975-08-02', 'MALE', 'Lot UV 8 Antsirabe', 'Apiculteur', '0370634567', 'member.16@fed-agri.mg', 'SENIOR');

INSERT INTO collectivity_member (collectivity_id, member_id)
SELECT 'col-3', id FROM member WHERE id LIKE 'C3-M%';

INSERT INTO member_referee (member_id, referee_id, relation_type) VALUES

-- Références vers collectivité 1
('C3-M1', 'C1-M1', 'SUPERVISOR'),
('C3-M1', 'C1-M2', 'SUPERVISOR'),

('C3-M2', 'C1-M1', 'SUPERVISOR'),
('C3-M2', 'C1-M2', 'SUPERVISOR'),

-- Références internes
('C3-M3', 'C3-M1', 'SUPERVISOR'),
('C3-M3', 'C3-M2', 'SUPERVISOR'),

('C3-M4', 'C3-M1', 'SUPERVISOR'),
('C3-M4', 'C3-M2', 'SUPERVISOR'),

('C3-M5', 'C3-M1', 'SUPERVISOR'),
('C3-M5', 'C3-M2', 'SUPERVISOR'),

('C3-M6', 'C3-M1', 'SUPERVISOR'),
('C3-M6', 'C3-M2', 'SUPERVISOR'),

('C3-M7', 'C3-M1', 'SUPERVISOR'),
('C3-M7', 'C3-M2', 'SUPERVISOR'),

('C3-M8', 'C3-M1', 'SUPERVISOR'),
('C3-M8', 'C3-M2', 'SUPERVISOR');


INSERT INTO collectivity_structure (collectivity_id,president_id,vice_president_id,treasurer_id,secretary_id) VALUES (
    'col-3',
    'C3-M1',
    'C3-M2',
    'C3-M4',
    'C3-M3'
    );

-- Compte bancaire 1
INSERT INTO financial_account (
    id,
    collectivity_id,
    type,
    amount,
    bank_name,
    bank_code,
    bank_branch_code,
    bank_account_number,
    bank_account_key,
    holder_name
) VALUES (
             'C3-A-BANK-1',
             'col-3',
             'BANK',
             0,
             'BMOI',
             4,
             1,
             1234567890,
             12,
             'Koto'
         );

-- Compte bancaire 2
INSERT INTO financial_account (
    id,
    collectivity_id,
    type,
    amount,
    bank_name,
    bank_code,
    bank_branch_code,
    bank_account_number,
    bank_account_key,
    holder_name
) VALUES (
             'C3-A-BANK-2',
             'col-3',
             'BANK',
             0,
             'BRED',
             8,
             3,
             4567890123,
             58,
             'Naivo'
         );

INSERT INTO financial_account (
    id,
    collectivity_id,
    type,
    amount,
    holder_name,
    mobile_banking_service,
    mobile_number
) VALUES (
             'C3-A-MOBILE-1',
             'col-3',
             'MOBILE_BANKING',
             0,
             'Kolo',
             'MVOLA',
             '0341889612'
         );

-- =========================
-- COLLECTIVITE 1 - COTISATIONS
-- =========================

INSERT INTO membership_fee (
    id,
    collectivity_id,
    eligible_from,
    frequency,
    amount,
    label,
    status
) VALUES (
             'cot-1',
             'col-1',
             '2026-01-01',
             'ANNUALLY',
             200000,
             'Cotisation annuelle',
             'ACTIVE'
         );

INSERT INTO membership_fee (
    id,
    collectivity_id,
    eligible_from,
    frequency,
    amount,
    label,
    status
) VALUES (
             'cot-2',
             'col-1',
             '2026-04-30',
             'PUNCTUALLY',
             20000,
             'Famangiana',
             'ACTIVE'
         );

-- =========================
-- COLLECTIVITE 2 - COTISATIONS
-- =========================

INSERT INTO membership_fee (
    id,
    collectivity_id,
    eligible_from,
    frequency,
    amount,
    label,
    status
) VALUES (
             'cot-3',
             'col-2',
             '2026-01-01',
             'ANNUALLY',
             200000,
             'Cotisation annuelle',
             'ACTIVE'
         );

INSERT INTO membership_fee (
    id,
    collectivity_id,
    eligible_from,
    frequency,
    amount,
    label,
    status
) VALUES (
             'cot-4',
             'col-2',
             '2025-01-01',
             'ANNUALLY',
             100000,
             'Cotisation 2025',
             'INACTIVE'
         );

-- =========================
-- COLLECTIVITE 3 - COTISATIONS
-- =========================

INSERT INTO membership_fee (
    id,
    collectivity_id,
    eligible_from,
    frequency,
    amount,
    label,
    status
) VALUES (
             'cot-5',
             'col-3',
             '2026-04-01',
             'MONTHLY',
             25000,
             'Cotisation mensuelle',
             'ACTIVE'
         );

-- =========================
-- COLLECTIVITE 1 - PAYMENTS
-- =========================

INSERT INTO member_payment VALUES
                               ('mp-1','C1-M1','cot-1',200000,'CASH','C1-A-CASH','2026-01-01'),
                               ('mp-2','C1-M2','cot-1',200000,'CASH','C1-A-CASH','2026-01-01'),
                               ('mp-3','C1-M3','cot-1',200000,'MOBILE_MONEY','C1-A-MOBILE-1','2026-01-01'),
                               ('mp-4','C1-M4','cot-1',200000,'MOBILE_MONEY','C1-A-MOBILE-1','2026-01-01'),
                               ('mp-5','C1-M5','cot-1',150000,'MOBILE_MONEY','C1-A-MOBILE-1','2026-01-01'),
                               ('mp-6','C1-M6','cot-1',100000,'CASH','C1-A-CASH','2026-05-01'),
                               ('mp-7','C1-M7','cot-1',60000,'CASH','C1-A-CASH','2026-05-01'),
                               ('mp-8','C1-M8','cot-1',90000,'CASH','C1-A-CASH','2026-05-01');

-- =========================
-- COLLECTIVITE 1 - TRANSACTIONS
-- =========================

INSERT INTO collectivity_transaction VALUES
                                         ('ct-1','col-1','2026-01-01',200000,'CASH','C1-A-CASH','C1-M1'),
                                         ('ct-2','col-1','2026-01-01',200000,'CASH','C1-A-CASH','C1-M2'),
                                         ('ct-3','col-1','2026-01-01',200000,'MOBILE_MONEY','C1-A-MOBILE-1','C1-M3'),
                                         ('ct-4','col-1','2026-01-01',200000,'MOBILE_MONEY','C1-A-MOBILE-1','C1-M4'),
                                         ('ct-5','col-1','2026-01-01',150000,'MOBILE_MONEY','C1-A-MOBILE-1','C1-M5'),
                                         ('ct-6','col-1','2026-05-01',100000,'CASH','C1-A-CASH','C1-M6'),
                                         ('ct-7','col-1','2026-05-01',60000,'CASH','C1-A-CASH','C1-M7'),
                                         ('ct-8','col-1','2026-05-01',90000,'CASH','C1-A-CASH','C1-M8');

-- =========================
-- COLLECTIVITE 2 - PAYMENTS
-- =========================

INSERT INTO member_payment VALUES
                               ('mp-c2-1','C1-M1','cot-3',120000,'CASH','C2-A-CASH','2026-01-01'),
                               ('mp-c2-2','C1-M2','cot-3',180000,'CASH','C2-A-CASH','2026-01-01'),
                               ('mp-c2-3','C1-M3','cot-3',200000,'CASH','C2-A-CASH','2026-01-01'),
                               ('mp-c2-4','C1-M4','cot-3',200000,'CASH','C2-A-CASH','2026-01-01'),
                               ('mp-c2-5','C1-M5','cot-3',200000,'CASH','C2-A-CASH','2026-01-01'),
                               ('mp-c2-6','C1-M6','cot-3',200000,'CASH','C2-A-CASH','2026-01-01'),
                               ('mp-c2-7','C1-M7','cot-3',80000,'MOBILE_MONEY','C2-A-MOBILE-1','2026-01-01'),
                               ('mp-c2-8','C1-M8','cot-3',120000,'MOBILE_MONEY','C2-A-MOBILE-1','2026-01-01');

-- =========================
-- COLLECTIVITE 2 - TRANSACTIONS
-- =========================

INSERT INTO collectivity_transaction VALUES
                                         ('ct-c2-1','col-2','2026-01-01',120000,'CASH','C2-A-CASH','C1-M1'),
                                         ('ct-c2-2','col-2','2026-01-01',180000,'CASH','C2-A-CASH','C1-M2'),
                                         ('ct-c2-3','col-2','2026-01-01',200000,'CASH','C2-A-CASH','C1-M3'),
                                         ('ct-c2-4','col-2','2026-01-01',200000,'CASH','C2-A-CASH','C1-M4'),
                                         ('ct-c2-5','col-2','2026-01-01',200000,'CASH','C2-A-CASH','C1-M5'),
                                         ('ct-c2-6','col-2','2026-01-01',200000,'CASH','C2-A-CASH','C1-M6'),
                                         ('ct-c2-7','col-2','2026-01-01',80000,'MOBILE_MONEY','C2-A-MOBILE-1','C1-M7'),
                                         ('ct-c2-8','col-2','2026-01-01',120000,'MOBILE_MONEY','C2-A-MOBILE-1','C1-M8');

-- =========================
-- COLLECTIVITE 3 - PAYMENTS
-- =========================

INSERT INTO member_payment VALUES
-- Avril 2026
('mp-c3-1','C3-M1','cot-5',25000,'BANK','C3-A-BANK-1','2026-04-01'),
('mp-c3-2','C3-M2','cot-5',25000,'BANK','C3-A-BANK-1','2026-04-01'),
('mp-c3-3','C3-M3','cot-5',25000,'BANK','C3-A-BANK-1','2026-04-01'),
('mp-c3-4','C3-M4','cot-5',25000,'BANK','C3-A-BANK-1','2026-04-01'),
('mp-c3-5','C3-M5','cot-5',25000,'BANK','C3-A-BANK-2','2026-04-01'),
('mp-c3-6','C3-M6','cot-5',25000,'BANK','C3-A-BANK-2','2026-04-01'),
('mp-c3-7','C3-M7','cot-5',25000,'CASH','C3-A-CASH','2026-04-01'),
('mp-c3-8','C3-M8','cot-5',25000,'CASH','C3-A-CASH','2026-04-01'),

-- Mai 2026
('mp-c3-9','C3-M1','cot-5',25000,'BANK','C3-A-BANK-1','2026-05-01'),
('mp-c3-10','C3-M2','cot-5',25000,'BANK','C3-A-BANK-1','2026-05-01'),
('mp-c3-11','C3-M3','cot-5',15000,'MOBILE_MONEY','C3-A-MOBILE-1','2026-05-01'),
('mp-c3-12','C3-M4','cot-5',15000,'MOBILE_MONEY','C3-A-MOBILE-1','2026-05-01'),
('mp-c3-13','C3-M5','cot-5',20000,'BANK','C3-A-BANK-2','2026-05-01'),
('mp-c3-14','C3-M6','cot-5',25000,'BANK','C3-A-BANK-2','2026-05-01'),
('mp-c3-15','C3-M7','cot-5',5000,'CASH','C3-A-CASH','2026-05-01'),
('mp-c3-16','C3-M8','cot-5',5000,'CASH','C3-A-CASH','2026-05-01');

-- =========================
-- COLLECTIVITE 3 - TRANSACTIONS
-- =========================

INSERT INTO collectivity_transaction VALUES
-- Avril 2026
('ct-c3-1','col-3','2026-04-01',25000,'BANK','C3-A-BANK-1','C3-M1'),
('ct-c3-2','col-3','2026-04-01',25000,'BANK','C3-A-BANK-1','C3-M2'),
('ct-c3-3','col-3','2026-04-01',25000,'BANK','C3-A-BANK-1','C3-M3'),
('ct-c3-4','col-3','2026-04-01',25000,'BANK','C3-A-BANK-1','C3-M4'),
('ct-c3-5','col-3','2026-04-01',25000,'BANK','C3-A-BANK-2','C3-M5'),
('ct-c3-6','col-3','2026-04-01',25000,'BANK','C3-A-BANK-2','C3-M6'),
('ct-c3-7','col-3','2026-04-01',25000,'CASH','C3-A-CASH','C3-M7'),
('ct-c3-8','col-3','2026-04-01',25000,'CASH','C3-A-CASH','C3-M8'),

-- Mai 2026
('ct-c3-9','col-3','2026-05-01',25000,'BANK','C3-A-BANK-1','C3-M1'),
('ct-c3-10','col-3','2026-05-01',25000,'BANK','C3-A-BANK-1','C3-M2'),
('ct-c3-11','col-3','2026-05-01',15000,'MOBILE_MONEY','C3-A-MOBILE-1','C3-M3'),
('ct-c3-12','col-3','2026-05-01',15000,'MOBILE_MONEY','C3-A-MOBILE-1','C3-M4'),
('ct-c3-13','col-3','2026-05-01',20000,'BANK','C3-A-BANK-2','C3-M5'),
('ct-c3-14','col-3','2026-05-01',25000,'BANK','C3-A-BANK-2','C3-M6'),
('ct-c3-15','col-3','2026-05-01',5000,'CASH','C3-A-CASH','C3-M7'),
('ct-c3-16','col-3','2026-05-01',5000,'CASH','C3-A-CASH','C3-M8');

-- =========================
-- UPDATE ANCIENS MEMBRES
-- =========================

UPDATE member
SET join_date = '2026-01-01'
WHERE id IN (
             'C1-M1','C1-M2','C1-M3','C1-M4','C1-M5','C1-M6','C1-M7','C1-M8',
             'C3-M1','C3-M2','C3-M3','C3-M4','C3-M5','C3-M6','C3-M7','C3-M8'
    );
INSERT INTO member VALUES
                       ('C1-M9','Andry','Rakoto','1998-05-10','MALE','Tana','Etudiant','0340000001','m9@mail.com','JUNIOR','2026-01-02'),
                       ('C1-M10','Miora','Rasoanaivo','1995-11-20','FEMALE','Tana','Comptable','0340000002','m10@mail.com','SENIOR','2026-01-02');
INSERT INTO member VALUES
                       ('C2-M1','Hery','Rakotomalala','1992-02-14','MALE','Tana','Ingénieur','0340000003','m11@mail.com','SENIOR','2026-01-03'),
                       ('C2-M2','Lova','Andrianina','2000-07-18','FEMALE','Tana','Etudiante','0340000004','m12@mail.com','JUNIOR','2026-01-03');
INSERT INTO member VALUES
                       ('C3-M9','Fetra','Rabenja','1997-03-22','MALE','Tana','Technicien','0340000005','m13@mail.com','SENIOR','2026-01-04'),
                       ('C3-M10','Nantenaina','Ravo','1999-09-30','FEMALE','Tana','Secrétaire','0340000006','m14@mail.com','SECRETARY','2026-01-04');

INSERT INTO collectivity_member VALUES
                                    ('col-1','C1-M9'),
                                    ('col-1','C1-M10'),
                                    ('col-2','C2-M1'),
                                    ('col-2','C2-M2'),
                                    ('col-3','C3-M9'),
                                    ('col-3','C3-M10');

-- =========================
-- NOUVEAUX MEMBRES
-- =========================

INSERT INTO member VALUES
                       ('NM-1','Rakoto','Ando','2001-02-10','MALE','Antananarivo','Etudiant','0341000001','ando1@mail.com','JUNIOR','2026-04-01'),
                       ('NM-2','Rasoa','Miora','2002-06-15','FEMALE','Antananarivo','Etudiante','0341000002','miora2@mail.com','JUNIOR','2026-04-01'),

                       ('NM-3','Rabe','Lita','2003-09-21','FEMALE','Tana','Etudiante','0341000003','lita3@mail.com','JUNIOR','2026-05-01'),
                       ('NM-4','Rakoto','Nana','2000-12-05','MALE','Tana','Etudiant','0341000004','nana4@mail.com','JUNIOR','2026-06-01');

-- =========================
-- AFFECTATION COLLECTIVITE
-- =========================

INSERT INTO collectivity_member VALUES
                                    ('col-1','NM-1'),
                                    ('col-1','NM-2'),
                                    ('col-1','NM-3'),
                                    ('col-1','NM-4');

-- =========================
-- REFERENTS
-- =========================

INSERT INTO member_referee VALUES
                               ('NM-1','C1-M1','SPONSOR'),
                               ('NM-1','C1-M2','SPONSOR'),

                               ('NM-2','C1-M1','SPONSOR'),
                               ('NM-2','C1-M2','SPONSOR'),

                               ('NM-3','C1-M1','SPONSOR'),
                               ('NM-3','C1-M2','SPONSOR'),

                               ('NM-4','C1-M1','SPONSOR'),
                               ('NM-4','C1-M2','SPONSOR');



