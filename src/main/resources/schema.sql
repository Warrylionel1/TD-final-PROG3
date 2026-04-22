-- =========================
-- TABLE MEMBER
-- =========================
CREATE TABLE member (
                        id VARCHAR PRIMARY KEY,
                        first_name VARCHAR NOT NULL,
                        last_name VARCHAR NOT NULL,
                        birth_date DATE,
                        gender VARCHAR,
                        address VARCHAR,
                        profession VARCHAR,
                        phone_number VARCHAR,
                        email VARCHAR UNIQUE,
                        occupation VARCHAR,
                        join_date DATE DEFAULT CURRENT_DATE
);

-- =========================
-- TABLE COLLECTIVITY
-- =========================
CREATE TABLE collectivity (
                              id SERIAL PRIMARY KEY,
                              location VARCHAR NOT NULL,
                              federation_approval BOOLEAN NOT NULL
);

-- =========================
-- RELATION COLLECTIVITY ↔ MEMBER
-- =========================
CREATE TABLE collectivity_member (
                                     collectivity_id INT,
                                     member_id VARCHAR,
                                     PRIMARY KEY (collectivity_id, member_id),
                                     FOREIGN KEY (collectivity_id) REFERENCES collectivity(id) ON DELETE CASCADE,
                                     FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
);

-- =========================
-- RELATION PARRAINAGE (MEMBER ↔ MEMBER)
-- =========================
CREATE TABLE member_referee (
                                member_id VARCHAR,
                                referee_id VARCHAR,
                                PRIMARY KEY (member_id, referee_id),
                                FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE,
                                FOREIGN KEY (referee_id) REFERENCES member(id) ON DELETE CASCADE
);

-- =========================
-- STRUCTURE COLLECTIVITY
-- =========================
CREATE TABLE collectivity_structure (
                                        collectivity_id INT PRIMARY KEY,
                                        president_id VARCHAR NOT NULL,
                                        vice_president_id VARCHAR NOT NULL,
                                        treasurer_id VARCHAR NOT NULL,
                                        secretary_id VARCHAR NOT NULL,

                                        FOREIGN KEY (collectivity_id) REFERENCES collectivity(id) ON DELETE CASCADE,
                                        FOREIGN KEY (president_id) REFERENCES member(id),
                                        FOREIGN KEY (vice_president_id) REFERENCES member(id),
                                        FOREIGN KEY (treasurer_id) REFERENCES member(id),
                                        FOREIGN KEY (secretary_id) REFERENCES member(id)
);
