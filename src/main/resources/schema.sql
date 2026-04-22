
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