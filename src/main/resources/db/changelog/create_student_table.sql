-- liquibase formatted sql

-- changeset Cherkasov A.V:1

CREATE TABLE IF NOT EXISTS student
(
    id UUID NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    first_name TEXT CHECK(LENGTH(first_name)<50) NOT NULL,
    middle_name TEXT CHECK(LENGTH(middle_name)<50) NOT NULL,
    last_name TEXT CHECK(LENGTH(last_name)<50),
    mobile_phone TEXT CHECK(LENGTH(mobile_phone)<13),
    specialization TEXT CHECK(LENGTH(specialization)<60),
    course INT CHECK(course < 5 AND course > 0),
    worker TEXT CHECK(LENGTH(worker)<200),
    created_at DATE DEFAULT CURRENT_DATE
);

