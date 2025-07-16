CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT PRIMARY KEY UNIQUE ,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles
(
    id   BIGINT PRIMARY KEY UNIQUE,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

INSERT INTO roles (id, name)
VALUES (1,'ROLE_USER'),
       (2,'ROLE_ADMIN');

INSERT INTO users(id, username, password)
VALUES (1,'user', '$2a$12$XfPp/7OVTEcXw14JoFqTN.wOcHsQiGkCom/O/0W4nKWTbf/bTeZzq'),
       (2,'admin', '$2a$12$rA4K/0qXR/BtXKtc2WSNCOAytFpLtDNgiHlzmj4jeacX2LRtvO3wS');

INSERT INTO users_roles(user_id, role_id)
VALUES (1,1),
       (2,2);
