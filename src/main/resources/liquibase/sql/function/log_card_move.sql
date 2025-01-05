--liquibase formatted sql

--changeset michael-bill:log_card_move splitStatements:false
create or replace function log_card_move()
returns trigger as $$
begin
    if new.column_id <> old.column_id then
        insert into card_history (card_id, from_column, to_column, changed_by)
        values (new.id, old.column_id, new.column_id, current_user);
    end if;
    return new;
end;
$$ language plpgsql;
