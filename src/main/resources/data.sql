--test data
TRUNCATE TABLE collectivity RESTART IDENTITY CASCADE;

INSERT INTO member VALUES
                       ('M1','A','A','2000-01-01','MALE','addr','farm','01','a@mail.com','SENIOR','2023-01-01'),
                       ('M2','B','B','2000-01-01','MALE','addr','farm','02','b@mail.com','SENIOR','2023-02-01'),
                       ('M3','C','C','2000-01-01','MALE','addr','farm','03','c@mail.com','SENIOR','2023-03-01'),
                       ('M4','D','D','2000-01-01','MALE','addr','farm','04','d@mail.com','SENIOR','2023-04-01'),
                       ('M5','E','E','2000-01-01','MALE','addr','farm','05','e@mail.com','SENIOR','2023-05-01'),

                       ('M6','F','F','2000-01-01','MALE','addr','farm','06','f@mail.com','JUNIOR','2025-10-01'),
                       ('M7','G','G','2000-01-01','MALE','addr','farm','07','g@mail.com','JUNIOR','2025-11-01'),
                       ('M8','H','H','2000-01-01','MALE','addr','farm','08','h@mail.com','JUNIOR','2025-12-01'),
                       ('M9','I','I','2000-01-01','MALE','addr','farm','09','i@mail.com','JUNIOR','2026-01-01'),
                       ('M10','J','J','2000-01-01','MALE','addr','farm','10','j@mail.com','JUNIOR','2026-02-01');

INSERT INTO collectivity VALUES
    (1, 'Antananarivo', true);

INSERT INTO collectivity_member VALUES
                                    (1,'M1'),
                                    (1,'M2'),
                                    (1,'M3'),
                                    (1,'M4'),
                                    (1,'M5'),
                                    (1,'M6'),
                                    (1,'M7'),
                                    (1,'M8'),
                                    (1,'M9'),
                                    (1,'M10');

INSERT INTO collectivity_structure VALUES
    (1,'M1','M2','M3','M4');