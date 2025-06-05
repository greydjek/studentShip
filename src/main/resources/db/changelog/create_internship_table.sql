-- liquibase formatted sql

-- changeset Cherkasov A.V:3

CREATE TABLE IF NOT EXISTS internship
(
    internship_id  UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    student_id     UUID REFERENCES student (id) ON DELETE CASCADE,
    company_id     UUID REFERENCES company (id) ON DELETE CASCADE,
    start_date     DATE NOT NULL,
    end_date       DATE NOT NULL,
    position       TEXT,
    comments_works TEXT,
    created_at     DATE DEFAULT CURRENT_DATE,
    CONSTRAINT valid_dates CHECK (end_date >= start_date),
    CONSTRAINT position CHECK(length(position) <150),
    CONSTRAINT comments_works CHECK(length(comments_works)<500)

);