--liquibase formatted sql

--changeset michael-bill:user_roles_table
create table if not exists user_roles
(
    user_id varchar(32) references users (login) not null,
    role_id integer references roles (id) not null,

    primary key (user_id, role_id)
);

create index idx_user_roles_user_id on user_roles(user_id);
create index idx_user_roles_role_id on user_roles(role_id);
