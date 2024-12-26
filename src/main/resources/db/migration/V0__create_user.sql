CREATE TABLE user
(
    id       SERIAL PRIMARY KEY,
    email    VARCHAR(255) UNIQUE      NOT NULL,
    password VARCHAR(255)             NOT NULL,
    role     ENUM ('OWNER', 'DRIVER') NOT NULL
);
