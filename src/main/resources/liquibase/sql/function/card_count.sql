--liquibase formatted sql

--changeset michael-bill:card_count splitStatements:false

-- Увеличение счетчика при добавлении карточки
create or replace function increase_card_count()
returns trigger as $$
begin
    update board_columns
    set card_count = card_count + 1
    where id = new.column_id;
    return new;
end;
$$ language plpgsql;

-- Уменьшение счетчика при удалении карточки
create or replace function decrease_card_count()
returns trigger as $$
begin
    update board_columns
    set card_count = card_count - 1
    where id = old.column_id;
    return old;
end;
$$ language plpgsql;

-- Обновление счетчиков при перемещении карточки между колонками
create or replace function update_card_count_on_move()
returns trigger as $$
begin
    -- Уменьшаем счетчик в старой колонке
    update board_columns
    set card_count = card_count - 1
    where id = old.column_id;

    -- Увеличиваем счетчик в новой колонке
    update board_columns
    set card_count = card_count + 1
    where id = new.column_id;

    return new;
end;
$$ language plpgsql;
