--liquibase formatted sql

--changeset michael-bill:messages_table
create table if not exists messages
(
    id bigserial primary key,
    chat_id bigint not null references chat (id) on delete cascade,
    sender_id bigint references users (id) not null,
    text_content text check (char_length(text_content) >= 1),
    file_path text,
    file_name text,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp,
    is_read boolean not null default false,

    check (text_content is not null or file_path is not null)
);

create index idx_messages_chat_id on messages(chat_id);
create index idx_messages_sender_id on messages(sender_id);

create trigger trg_update_messages_updated_at
before update on messages
for each row
execute procedure update_updated_at();
