--liquibase formatted sql

--changeset michael-bill:card_comments_table
create table if not exists card_comments
(
    id bigserial primary key,
    user_id bigint references users (id) not null,
    card_id bigint not null references card (id) on delete cascade,
    reply_to_comment_id bigint references card_comments (id),
    content text not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp
);

create index idx_card_comments_card_id on card_comments(card_id);
create index idx_card_comments_user_id on card_comments(user_id);

create trigger trg_update_card_comments_updated_at
before update on card_comments
for each row
execute procedure update_updated_at();
