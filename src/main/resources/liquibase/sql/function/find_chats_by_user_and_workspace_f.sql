--liquibase formatted sql

--changeset michael-bill:find_chats_by_user_and_workspace_f splitStatements:false
drop function if exists find_chats_by_user_and_workspace_f(bigint, text);

create or replace function find_chats_by_user_and_workspace_f(
    p_user_id bigint,
    p_workspace_name text
)
returns table(
    id bigint,
    type text,
    title text,
    createdAt timestamp,
    createdBy text,
    lastMessageText text,
    lastMessageTime timestamp,
    lastMessageSenderId bigint,
    foldersJsonArray json,
    participantsJsonArray json
) as $$
begin
    return query
        with chat_last_message as (
            select
                chat_id,
                max(created_at) as last_message_time
            from
                messages
            group by
                chat_id
        ),
        folder_info as (
            select
                cf.chat_id,
                json_agg(
                    json_build_object('folder_id', f.id, 'folder_name', f.name)
                ) as folders
            from
                chat_folder cf
            join folder f on cf.folder_id = f.id
            group by
                cf.chat_id
        ),
        chat_users as (
            select
                cu.chat_id,
                json_agg(
                    json_build_object('id', cu.user_id, 'username', u.username, 'role', r.name)
                ) as users
            from
                chat_user cu
            join users u on cu.user_id = u.id
            join roles r on cu.role_id = r.id
            group by
                cu.chat_id
        ),
        last_messages_detail as (
            select distinct on (chat_id)
                chat_id,
                sender_id,
                created_at as last_message_time,
                text_content as last_message_text
            from
                messages
            order by
                chat_id, created_at desc
        )
        select
            c.id as id,
            c.type as type,
            c.title as title,
            c.created_at as createdAt,
            u.username as createdBy,
            lmd.last_message_text as lastMessageText,
            lm.last_message_time as lastMessageTime,
            lmd.sender_id as lastMessageSenderId,
            coalesce(fi.folders, '[]') as foldersJsonArray,
            coalesce(cu.users, '[]') as participantsJsonArray
        from
            chat c
        join
            user_workspace uw on c.workspace_id = uw.workspace_id
        join
            users u on c.created_by = u.id
        left join
            chat_last_message lm on c.id = lm.chat_id
        left join
            last_messages_detail lmd on c.id = lmd.chat_id
        left join
            folder_info fi on c.id = fi.chat_id
        left join
            chat_users cu on c.id = cu.chat_id
        where
            uw.user_id = p_user_id and
            uw.workspace_id = p_workspace_name
        order by
            lm.last_message_time desc;
end;
$$ language plpgsql;
