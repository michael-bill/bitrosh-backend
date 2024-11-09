--liquibase formatted sql

--changeset michael-bill:messages_table
create table if not exists messages
(
    id              integer primary key,
    conversation_id integer references chat (id)         not null,
    sender_id       varchar(32) references users (login) not null,
    card_id         integer references card (id),
    text_content    text check (char_length(text_content) >= 1),
    file_path       text,
    created_at  timestamp not null default current_timestamp,
    updated_at  timestamp not null default current_timestamp,
    is_read         boolean           not null default false,

    check (card_id is not null or text_content is not null or file_path is not null)
);

create index idx_messages_conversation_id on messages(conversation_id);
create index idx_messages_sender_id on messages(sender_id);
create index idx_messages_card_id on messages(card_id);

create trigger trg_update_messages_updated_at
before update on messages
for each row
execute procedure update_updated_at();
