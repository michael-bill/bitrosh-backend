--liquibase formatted sql

--changeset michael-bill:chat_table
create table if not exists chat
(
    id bigserial primary key,
    workspace_id varchar(128) not null references workspace (name) on delete cascade,
    created_by bigint references users (id) not null,
    created_at timestamp not null,
    title text check (char_length(title) >= 1),
    type text not null
);

create index idx_chat_workspace_id on chat(workspace_id);
create index idx_chat_created_by on chat(created_by);
