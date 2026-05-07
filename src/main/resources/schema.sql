
-- =========================
-- MEMBER
-- =========================
CREATE TABLE member (
                        id VARCHAR PRIMARY KEY,

                        first_name VARCHAR NOT NULL,
                        last_name VARCHAR NOT NULL,
                        birth_date DATE,
                        gender VARCHAR CHECK (gender IN ('MALE', 'FEMALE')),
                        address VARCHAR,
                        profession VARCHAR,
                        phone_number VARCHAR,
                        email VARCHAR UNIQUE,

                        occupation VARCHAR CHECK (
                            occupation IN (
                                           'JUNIOR',
                                           'SENIOR',
                                           'SECRETARY',
                                           'TREASURER',
                                           'VICE_PRESIDENT',
                                           'PRESIDENT'
                                )
                            ),

                        join_date DATE DEFAULT CURRENT_DATE
);

-- =========================
-- COLLECTIVITY
-- =========================
CREATE TABLE collectivity (
                              id VARCHAR PRIMARY KEY,

                              location VARCHAR NOT NULL,
                              specialty VARCHAR NOT NULL,
                              creation_date DATE DEFAULT CURRENT_DATE,

                              federation_approval BOOLEAN NOT NULL,

                              number VARCHAR(50) UNIQUE,
                              name VARCHAR(255) UNIQUE
);

-- =========================
-- COLLECTIVITY_MEMBER
-- =========================
CREATE TABLE collectivity_member (
                                     collectivity_id VARCHAR,
                                     member_id VARCHAR,

                                     PRIMARY KEY (collectivity_id, member_id),

                                     FOREIGN KEY (collectivity_id)
                                         REFERENCES collectivity(id)
                                         ON DELETE CASCADE,

                                     FOREIGN KEY (member_id)
                                         REFERENCES member(id)
                                         ON DELETE CASCADE
);

-- =========================
-- MEMBER_REFEREE
-- =========================
CREATE TABLE member_referee (
                                member_id VARCHAR,
                                referee_id VARCHAR,
                                relation_type VARCHAR,

                                PRIMARY KEY (member_id, referee_id),

                                FOREIGN KEY (member_id)
                                    REFERENCES member(id)
                                    ON DELETE CASCADE,

                                FOREIGN KEY (referee_id)
                                    REFERENCES member(id)
                                    ON DELETE CASCADE
);

-- =========================
-- COLLECTIVITY_STRUCTURE
-- =========================
CREATE TABLE collectivity_structure (
                                        collectivity_id VARCHAR PRIMARY KEY,

                                        president_id VARCHAR NOT NULL,
                                        vice_president_id VARCHAR NOT NULL,
                                        treasurer_id VARCHAR NOT NULL,
                                        secretary_id VARCHAR NOT NULL,

                                        FOREIGN KEY (collectivity_id)
                                            REFERENCES collectivity(id)
                                            ON DELETE CASCADE,

                                        FOREIGN KEY (president_id) REFERENCES member(id),
                                        FOREIGN KEY (vice_president_id) REFERENCES member(id),
                                        FOREIGN KEY (treasurer_id) REFERENCES member(id),
                                        FOREIGN KEY (secretary_id) REFERENCES member(id),

    -- sécurité métier : éviter doublons de rôles
                                        CONSTRAINT uq_president UNIQUE (president_id),
                                        CONSTRAINT uq_vice_president UNIQUE (vice_president_id),
                                        CONSTRAINT uq_treasurer UNIQUE (treasurer_id),
                                        CONSTRAINT uq_secretary UNIQUE (secretary_id)

);

ALTER TABLE collectivity ADD COLUMN number VARCHAR(50) UNIQUE;
ALTER TABLE collectivity ADD COLUMN name VARCHAR(255) UNIQUE;

CREATE TABLE membership_fee (
                                id VARCHAR(36) PRIMARY KEY,
                                collectivity_id VARCHAR(36) NOT NULL,
                                eligible_from DATE NOT NULL,
                                frequency VARCHAR(20) NOT NULL,
                                amount DECIMAL(15,2) NOT NULL,
                                label VARCHAR(255),
                                status VARCHAR(20) NOT NULL,
                                FOREIGN KEY (collectivity_id) REFERENCES collectivity(id) ON DELETE CASCADE
);

CREATE TABLE financial_account (
                                   id VARCHAR(36) PRIMARY KEY,
                                   collectivity_id VARCHAR(36) NOT NULL,
                                   type VARCHAR(20) NOT NULL,  -- 'CASH', 'MOBILE_BANKING', 'BANK'
                                   amount DECIMAL(15,2) NOT NULL DEFAULT 0,
    -- for MOBILE_BANKING
                                   holder_name VARCHAR(255),
                                   mobile_banking_service VARCHAR(30),
                                   mobile_number VARCHAR(20),
    -- for BANK
                                   bank_name VARCHAR(50),
                                   bank_code INT,
                                   bank_branch_code INT,
                                   bank_account_number INT,
                                   bank_account_key INT,
                                   FOREIGN KEY (collectivity_id) REFERENCES collectivity(id) ON DELETE CASCADE
);

CREATE TABLE member_payment (
                                id VARCHAR(36) PRIMARY KEY,
                                member_id VARCHAR(36) NOT NULL,
                                membership_fee_id VARCHAR(36) NOT NULL,
                                amount DECIMAL(15,2) NOT NULL,
                                payment_mode VARCHAR(20) NOT NULL,
                                account_credited_id VARCHAR(36) NOT NULL,
                                creation_date DATE NOT NULL,
                                FOREIGN KEY (member_id) REFERENCES member(id),
                                FOREIGN KEY (membership_fee_id) REFERENCES membership_fee(id),
                                FOREIGN KEY (account_credited_id) REFERENCES financial_account(id)
);

CREATE TABLE collectivity_transaction (
                                          id VARCHAR(36) PRIMARY KEY,
                                          collectivity_id VARCHAR(36) NOT NULL,
                                          creation_date DATE NOT NULL,
                                          amount DECIMAL(15,2) NOT NULL,
                                          payment_mode VARCHAR(20) NOT NULL,
                                          account_credited_id VARCHAR(36) NOT NULL,
                                          member_debited_id VARCHAR(36) NOT NULL,
                                          FOREIGN KEY (collectivity_id) REFERENCES collectivity(id),
                                          FOREIGN KEY (account_credited_id) REFERENCES financial_account(id),
                                          FOREIGN KEY (member_debited_id) REFERENCES member(id)
);

-- =========================
-- COLLECTIVITY_ACTIVITY
-- =========================
CREATE TABLE collectivity_activity (
                                       id VARCHAR(36) PRIMARY KEY,

                                       collectivity_id VARCHAR(36) NOT NULL,

                                       label VARCHAR NOT NULL,

                                       activity_type VARCHAR(20) NOT NULL CHECK (
                                           activity_type IN ('MEETING', 'TRAINING', 'OTHER')
                                           ),

                                       executive_date DATE,

                                       week_ordinal INT CHECK (week_ordinal BETWEEN 1 AND 5),

                                       day_of_week VARCHAR(2) CHECK (
                                           day_of_week IN ('MO', 'TU', 'WE', 'TH', 'FR', 'SA', 'SU')
                                           ),

                                       CONSTRAINT fk_activity_collectivity
                                           FOREIGN KEY (collectivity_id)
                                               REFERENCES collectivity(id)
                                               ON DELETE CASCADE,

                                       CONSTRAINT chk_activity_date_or_recurrence CHECK (
                                           (executive_date IS NOT NULL AND week_ordinal IS NULL AND day_of_week IS NULL)
                                               OR
                                           (executive_date IS NULL AND week_ordinal IS NOT NULL AND day_of_week IS NOT NULL)
                                           )
);

CREATE TABLE activity_member_concerned (
                                           activity_id VARCHAR(36),
                                           occupation VARCHAR(30) NOT NULL CHECK (
                                               occupation IN (
                                                              'JUNIOR',
                                                              'SENIOR',
                                                              'SECRETARY',
                                                              'TREASURER',
                                                              'VICE_PRESIDENT',
                                                              'PRESIDENT'
                                                   )
                                               ),

                                           PRIMARY KEY (activity_id, occupation),

                                           FOREIGN KEY (activity_id)
                                               REFERENCES collectivity_activity(id)
                                               ON DELETE CASCADE
);

CREATE TABLE activity_member_attendance (
                                            id VARCHAR(36) PRIMARY KEY,

                                            activity_id VARCHAR(36) NOT NULL,
                                            member_id VARCHAR(36) NOT NULL,

                                            attendance_status VARCHAR(20) NOT NULL CHECK (
                                                attendance_status IN ('ATTENDED', 'MISSING', 'UNDEFINED')
                                                ),

                                            CONSTRAINT fk_attendance_activity
                                                FOREIGN KEY (activity_id)
                                                    REFERENCES collectivity_activity(id)
                                                    ON DELETE CASCADE,

                                            CONSTRAINT fk_attendance_member
                                                FOREIGN KEY (member_id)
                                                    REFERENCES member(id)
                                                    ON DELETE CASCADE,

                                            CONSTRAINT uq_activity_member UNIQUE (activity_id, member_id)
);

-- =========================
-- NETOYAGE BDD
-- =========================
TRUNCATE TABLE
    activity_member_attendance,
    activity_member_concerned,
    collectivity_activity,
    collectivity_transaction,
    member_payment,
    financial_account,
    membership_fee,
    collectivity_structure,
    member_referee,
    collectivity_member,
    collectivity,
    member
    CASCADE;