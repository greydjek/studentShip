-- liquibase formatted sql

-- changeset Cherkasov A.V:3

CREATE TABLE IF NOT EXISTS internship
(
    internship_id  UUID uuid_generate_v4() PRIMARY KEY,
    student_id     UUID REFERENCES student (id) ON DELETE CASCADE,
    company_id     UUID REFERENCES company (id) ON DELETE CASCADE,
    start_date     DATE NOT NULL,
    end_date       DATE NOT NULL,
    position       VARCHAR(100),
    comments_works TEXT,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_dates CHECK (end_date >= start_date)
);