--liquibase formatted sql

--changeset michael-bill:card_comments_table
create table if not exists card_comments
(
    id integer primary key,
    user_id bigint references users (id) not null,
    card_id integer  not null,
    reply_to_comment_id integer references card_comments (id),
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

alter table card_comments
add constraint card_comments_card_id_fkey
foreign key (card_id) references card (id) on delete cascade;
