
-- =========================
-- CLEAN
-- =========================
TRUNCATE TABLE member_referee CASCADE;
TRUNCATE TABLE collectivity_member CASCADE;
TRUNCATE TABLE collectivity_structure CASCADE;
TRUNCATE TABLE collectivity CASCADE;
TRUNCATE TABLE member CASCADE;

-- =========================
-- MEMBERS
-- =========================
INSERT INTO member (
    id, first_name, last_name, birth_date, gender,
    address, profession, phone_number, email,
    occupation, join_date
) VALUES
      ('M1','A','A','2000-01-01','MALE','addr','farm','01','a@mail.com','SENIOR','2023-01-01'),
      ('M2','B','B','2000-01-01','MALE','addr','farm','02','b@mail.com','SENIOR','2023-02-01'),
      ('M3','C','C','2000-01-01','MALE','addr','farm','03','c@mail.com','SENIOR','2023-03-01'),
      ('M4','D','D','2000-01-01','MALE','addr','farm','04','d@mail.com','SENIOR','2023-04-01'),
      ('M5','E','E','2000-01-01','MALE','addr','farm','05','e@mail.com','SENIOR','2023-05-01'),

      ('M6','F','F','2000-01-01','MALE','addr','farm','06','f@mail.com','JUNIOR','2023-06-01'),
      ('M7','G','G','2000-01-01','MALE','addr','farm','07','g@mail.com','JUNIOR','2023-07-01'),
      ('M8','H','H','2000-01-01','MALE','addr','farm','08','h@mail.com','JUNIOR','2023-08-01'),
      ('M9','I','I','2000-01-01','MALE','addr','farm','09','i@mail.com','JUNIOR','2023-09-01'),
      ('M10','J','J','2000-01-01','MALE','addr','farm','10','j@mail.com','JUNIOR','2023-10-01');

-- =========================
-- COLLECTIVITY
-- =========================
INSERT INTO collectivity (
    id, location, specialty, creation_date,
    federation_approval, number, name
) VALUES (
             'C1',
             'Antananarivo',
             'Rice Farming',
             CURRENT_DATE,
             true,
             'COLL-001',
             'Collectivity A'
         );

-- =========================
-- COLLECTIVITY MEMBERS
-- =========================
INSERT INTO collectivity_member (
    collectivity_id, member_id
) VALUES
      ('C1','M1'),
      ('C1','M2'),
      ('C1','M3'),
      ('C1','M4'),
      ('C1','M5'),
      ('C1','M6'),
      ('C1','M7'),
      ('C1','M8'),
      ('C1','M9'),
      ('C1','M10');

-- =========================
-- STRUCTURE
-- =========================
INSERT INTO collectivity_structure (
    collectivity_id,
    president_id,
    vice_president_id,
    treasurer_id,
    secretary_id
) VALUES (
             'C1','M1','M2','M3','M4'
         );

-- =========================
-- REFEREES (B-2 TEST CASE)
-- =========================
INSERT INTO member_referee (
    member_id,
    referee_id,
    relation_type
) VALUES
      ('M6','M1','friend'),
      ('M6','M2','colleague'),

      ('M7','M1','friend'),
      ('M7','M3','family'),

      ('M8','M2','friend'),
      ('M8','M3','colleague');