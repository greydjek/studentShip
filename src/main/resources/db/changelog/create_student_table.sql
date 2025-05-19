-- liquibase formatted sql

-- changeset Cherkasov A.V:1

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS student
(
    id UUID NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    first_name VARCHAR(30) NOT NULL,
    middle_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30),
    mobile_phone BIGINT,
    specialization VARCHAR(50),
    cource INT,
    worker VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

