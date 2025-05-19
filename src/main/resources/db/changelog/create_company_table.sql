-- liquibase formatted sql

-- changeset Cherkasov A.V:2

CREATE TABLE IF NOT EXISTS company
(
    id           UUID  uuid_generate_v4() PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    address      TEXT,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);