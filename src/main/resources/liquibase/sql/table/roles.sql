--liquibase formatted sql

--changeset michael-bill:roles_table
create table if not exists roles
(
    id bigserial primary key,
    role text not null
);
