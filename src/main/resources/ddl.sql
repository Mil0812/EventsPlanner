DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS my_events;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS day_task;

CREATE TABLE users
(
    id        UUID PRIMARY KEY,
    login     VARCHAR(20) NOT NULL UNIQUE,
    password  VARCHAR(20) NOT NULL,
    firstName VARCHAR(30) NOT NULL,
    avatar    BYTEA       NULL,
    CONSTRAINT users_login_key UNIQUE (login),
    CONSTRAINT users_login_min_length_check CHECK (LENGTH(login) >= 5)
);

CREATE TABLE my_events
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(40)  NOT NULL UNIQUE,
    description VARCHAR(140) NOT NULL UNIQUE,
    date        TIMESTAMP,
    image       BYTEA,
    user_id     UUID         NOT NULL,
    CONSTRAINT event_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE day_task
(
    id           UUID PRIMARY KEY,
    user_id      UUID        NOT NULL,
    name         VARCHAR(40) NOT NULL UNIQUE,
    created_at   TEXT        NOT NULL,
    completeness BOOLEAN DEFAULT false,
    CONSTRAINT task_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);