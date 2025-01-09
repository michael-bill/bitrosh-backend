--liquibase formatted sql

--changeset michael-bill:chat_folder_table
create table if not exists chat_folder (
    folder_id bigint not null references folder (id) on delete cascade,
    chat_id bigint not null references chat (id) on delete cascade
);
