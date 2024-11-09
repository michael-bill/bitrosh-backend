--liquibase formatted sql

--changeset michael-bill:user_workspace_table
create table if not exists user_workspace
(
    user_id      varchar(32) references users (login)     not null,
    workspace_id varchar(128) references workspace (name) not null,

    primary key (user_id, workspace_id)
);

create index idx_user_workspace_user_id on user_workspace(user_id);
create index idx_user_workspace_workspace_id on user_workspace(workspace_id);
