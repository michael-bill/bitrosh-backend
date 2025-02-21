--liquibase formatted sql

--changeset michael-bill:card_table
create table if not exists card
(
    id bigserial primary key,
    column_id bigint references board_columns (id) not null,
    created_by bigint references users (id) not null,
    executor_id bigint references users (id),
    sprint_id bigint references sprint (id),
    title text check (char_length(title) >= 1),
    content text check (char_length(content) >= 1),
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp,
    deadline timestamp,

    check (title is not null or content is not null)
);

create index idx_card_column_id on card(column_id);
create index idx_card_created_by on card(created_by);
create index idx_card_executor_id on card(executor_id);
create index idx_card_sprint_id on card(sprint_id);

create trigger trg_update_card_updated_at
before update on card
for each row
execute procedure update_updated_at();

-- Триггер на вставку карточки
create trigger trg_card_insert
after insert on card
for each row
execute procedure increase_card_count();

-- Триггер на удаление карточки
create trigger trg_card_delete
after delete on card
for each row
execute procedure decrease_card_count();

-- Триггер на обновление карточки (перемещение между колонками)
create trigger trg_card_update
after update on card
for each row
when (old.column_id is distinct from new.column_id)
execute procedure update_card_count_on_move();
