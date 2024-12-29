CREATE TABLE user
(
    id       SERIAL PRIMARY KEY,
    email    VARCHAR(255) UNIQUE      NOT NULL,
    password VARCHAR(255)             NOT NULL,
    role     ENUM ('OWNER', 'DRIVER') NOT NULL
);


CREATE TABLE IF NOT EXISTS owner
(
    id      SERIAL PRIMARY KEY,
    userId  INTEGER UNIQUE not null,
    name    VARCHAR(255)   NOT NULL,
    surname VARCHAR(255)   NOT NULL,
    CONSTRAINT fk_owner_id__id FOREIGN KEY (userId) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE RESTRICT
)