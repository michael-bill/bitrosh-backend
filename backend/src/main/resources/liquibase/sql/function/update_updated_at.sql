--liquibase formatted sql

--changeset michael-bill:update_updated_at splitStatements:false
create or replace function update_updated_at()
returns trigger as $$
begin
    new.updated_at = current_timestamp;
    return new;
end;
$$ language plpgsql;
