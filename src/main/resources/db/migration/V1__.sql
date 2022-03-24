CREATE TABLE roles
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(60) NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    role_id BIGINT       NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (role_id, user_id)
);

CREATE TABLE users
(
    id        VARCHAR(255) NOT NULL,
    createdAt datetime NULL,
    updatedAt datetime NULL,
    deleted   BIT(1) NULL,
    name      VARCHAR(255) NULL,
    username  VARCHAR(255) NULL,
    email     VARCHAR(255) NULL,
    password  VARCHAR(255) NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users ADD CONSTRAINT uc_74165e195b2f7b25de690d14a UNIQUE (email);
ALTER TABLE users ADD CONSTRAINT uc_77584fbe74cc86922be2a3560 UNIQUE (username);

ALTER TABLE user_roles ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);
ALTER TABLE user_roles ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);

insert into roles(id, name) values (1, 'ROLE_USER');
insert into roles(id, name) values (2, 'ROLE_ADMIN');

insert into users(id, createdAt, updatedAt, deleted, name, username, email, password)
    values ('2cb9374e-4e52-4142-a1af-16144ef4a27d', null, null, false, 'Anil Senocak', 'user', 'user@senocak.com', 'zNzslkQ8/jhLTwQPpYZj07ViC83cZx6HkFtolTJ7cmM=');
insert into users(id, createdAt, updatedAt, deleted, name, username, email, password)
    values ('3cb9374e-4e52-4142-a1af-16144ef4a27d', null, null, false, 'Anil Senocak', 'admin', 'admin@senocak.com', '2RIiz37ZEi/DNA3szM0+tiTSSY+IOvgobCMkdEUzFqA=');

insert into user_roles(role_id, user_id) values (1, '2cb9374e-4e52-4142-a1af-16144ef4a27d');
insert into user_roles(role_id, user_id) values (2, '2cb9374e-4e52-4142-a1af-16144ef4a27d');
insert into user_roles(role_id, user_id) values (1, '3cb9374e-4e52-4142-a1af-16144ef4a27d');