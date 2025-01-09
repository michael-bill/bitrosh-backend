--liquibase formatted sql

--changeset michael-bill:user_workspace_table
create table if not exists user_workspace
(
    user_id bigint references users (id) not null,
    workspace_id varchar(128) not null references workspace (name) on delete cascade,
    role_id bigint references roles (id) not null,

    primary key (user_id, workspace_id)
);

create index idx_user_workspace_user_id on user_workspace(user_id);
create index idx_user_workspace_workspace_id on user_workspace(workspace_id);
