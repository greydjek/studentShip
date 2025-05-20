-- liquibase formatted sql

-- changeset Cherkasov A.V:2

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS company
(
    id           UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    company_name TEXT  NOT NULL CHECK(LENGTH(company_name)<100),
    address      TEXT CHECK(LENGTH(address)<200),
    created_at   DATE DEFAULT CURRENT_DATE
);