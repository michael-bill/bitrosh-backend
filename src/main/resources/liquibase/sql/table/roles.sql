--liquibase formatted sql

--changeset michael-bill:roles_table
create table if not exists roles
(
    id bigserial primary key,
    name text not null
);

insert into roles values (1, 'ADMIN'), (2, 'USER_RW'), (3, 'USER_RO');
