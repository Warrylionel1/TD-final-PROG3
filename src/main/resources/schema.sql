CREATE DATABASE agricultural_federation;

\c agricultural_federation;

CREATE TYPE gender_enum AS ENUM ('MALE', 'FEMALE');
CREATE TYPE occupation_enum AS ENUM ('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');
CREATE TYPE payment_status_enum AS ENUM ('PAID', 'UNPAID');

-- SERIAL = INT auto-incrémenté
CREATE TABLE IF NOT EXISTS collectivities (
                                              id SERIAL PRIMARY KEY,
                                              location VARCHAR(255) NOT NULL,
    federation_approval BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS members (
                                       id SERIAL PRIMARY KEY,
                                       first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    gender gender_enum NOT NULL,
    address TEXT NOT NULL,
    profession VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    occupation occupation_enum NOT NULL DEFAULT 'JUNIOR',
    collectivity_id INTEGER REFERENCES collectivities(id) ON DELETE SET NULL,
    join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    registration_fee_paid payment_status_enum DEFAULT 'UNPAID',
    membership_dues_paid payment_status_enum DEFAULT 'UNPAID'
    );

CREATE TABLE IF NOT EXISTS collectivity_structure (
                                                      id SERIAL PRIMARY KEY,
                                                      collectivity_id INTEGER NOT NULL REFERENCES collectivities(id) ON DELETE CASCADE,
    president_id INTEGER REFERENCES members(id),
    vice_president_id INTEGER REFERENCES members(id),
    treasurer_id INTEGER REFERENCES members(id),
    secretary_id INTEGER REFERENCES members(id),
    mandate_start DATE NOT NULL,
    mandate_end DATE NOT NULL,
    UNIQUE(collectivity_id),
    UNIQUE(president_id),
    UNIQUE(vice_president_id),
    UNIQUE(treasurer_id),
    UNIQUE(secretary_id)
    );

CREATE TABLE IF NOT EXISTS sponsorships (
                                            id SERIAL PRIMARY KEY,
                                            member_id INTEGER NOT NULL REFERENCES members(id) ON DELETE CASCADE,
    referee_id INTEGER NOT NULL REFERENCES members(id) ON DELETE CASCADE,
    relation VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(member_id, referee_id)