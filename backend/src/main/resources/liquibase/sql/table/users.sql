--liquibase formatted sql

--changeset michael-bill:users_table
create table if not exists users
(
    login    varchar(32)    primary key check (char_length(login) >= 4),
    username varchar(128)   not null check (char_length(username) >= 4),
    password text           not null check (char_length(password) >= 8)
);
