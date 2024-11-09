--liquibase formatted sql

--changeset michael-bill:board_columns_table
create table if not exists board_columns
(
    id           integer primary key,
    workspace_id varchar(128) references workspace (name)  not null,
    title        text check (char_length(title) >= 1)      not null,
    level        integer                                   not null,
    color        text                                      not null,
    card_count   integer                                   not null default 0,

    unique (workspace_id, level)
);

create index idx_board_columns_workspace_id on board_columns(workspace_id);
