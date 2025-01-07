--liquibase formatted sql

--changeset michael-bill:workspace_table
create table if not exists workspace
(
    name varchar(128) primary key check (char_length(name) >= 1),
    title varchar(256),
    created_at timestamp not null
);
