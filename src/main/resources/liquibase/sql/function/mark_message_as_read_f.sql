--liquibase formatted sql

--changeset michael-bill:mark_message_as_read splitStatements:false
create or replace function mark_message_as_read_f(p_message_id bigint, p_user_id varchar)
returns void as $$
begin
    update messages
    set is_read = true
    where id = p_message_id and sender_id <> p_user_id;
end;
$$ language plpgsql;
