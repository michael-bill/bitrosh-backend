--liquibase formatted sql

--changeset michael-bill:users_table
create table if not exists users (
    id bigserial primary key,
    username text not null check (char_length(username) >= 4),
    password text not null,
    role text not null,
    current_workspace_id varchar(128) references workspace (name)
);
