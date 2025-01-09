--liquibase formatted sql

--changeset michael-bill:folder_table
create table if not exists folder (
    id bigserial primary key,
    user_id bigint references users (id) not null,
    name text not null,
    workspace_id varchar(128) not null references workspace (name) on delete cascade
);
