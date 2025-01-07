--liquibase formatted sql

--changeset michael-bill:poll_table
create table if not exists poll
(
    id integer primary key,
    author_id bigint references users (id) not null,
    chat_id integer references chat (id) not null,
    title text not null,
    variants text[] not null,
    answers integer[] not null
);

create index idx_poll_chat_id on poll(chat_id);
create index idx_poll_author_id on poll(author_id);
