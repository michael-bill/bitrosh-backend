--liquibase formatted sql

--changeset michael-bill:sprint_table
create table if not exists sprint
(
    id integer primary key,
    workspace_id varchar(128) references workspace (name) not null
);

create index idx_sprint_workspace_id on sprint(workspace_id);
