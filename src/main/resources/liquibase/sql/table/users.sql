--liquibase formatted sql

--changeset michael-bill:users_table
create table if not exists users (
    id bigserial primary key,
    username varchar(64) not null check (char_length(username) >= 4),
    password text not null,
    role varchar(64) not null,
    current_workspace_id varchar(128) references workspace (name)
);
