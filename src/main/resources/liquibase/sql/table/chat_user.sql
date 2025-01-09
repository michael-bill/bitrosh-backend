--liquibase formatted sql

--changeset michael-bill:chat_users
create table if not exists chat_user
(
    id bigserial primary key,
    chat_id bigint not null references chat (id) on delete cascade,
    user_id bigint references users (id) not null,
    join_at timestamp not null,
    role varchar(16) not null
);

create index idx_chat_user_chat_id on chat_user(chat_id);
create index idx_chat_user_user_id on chat_user(user_id);
