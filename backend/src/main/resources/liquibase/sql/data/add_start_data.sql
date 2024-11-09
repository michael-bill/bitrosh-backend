--liquibase formatted sql

--changeset michael-bill:add_test_data

-- Вставка данных в таблицу users
insert into users (login, username, password)
values
    ('user1', 'Алиса Иванова', 'password123'),
    ('user2', 'Боб Смирнов', 'securepass456'),
    ('user3', 'Чарли Петров', 'mypassword789');

-- Вставка данных в таблицу workspace
insert into workspace (name, title, created_at)
values
    ('workspace1', 'Проект Альфа', CURRENT_TIMESTAMP),
    ('workspace2', 'Проект Бета', CURRENT_TIMESTAMP);

-- Вставка данных в таблицу roles
insert into roles (id, role)
values
    (1, 'admin'),
    (2, 'member'),
    (3, 'guest');

-- Вставка данных в таблицу user_roles
insert into user_roles (user_id, role_id)
values
    ('user1', 1),
    ('user2', 2),
    ('user3', 3);

-- Вставка данных в таблицу user_workspace
insert into user_workspace (user_id, workspace_id)
values
    ('user1', 'workspace1'),
    ('user2', 'workspace1'),
    ('user1', 'workspace2'),
    ('user3', 'workspace2');

-- Вставка данных в таблицу sprint
insert into sprint (id, workspace_id)
values
    (1, 'workspace1'),
    (2, 'workspace2');

-- Вставка данных в таблицу board_columns
insert into board_columns (id, workspace_id, title, level, color)
values
    (1, 'workspace1', 'К выполнению', 1, 'red'),
    (2, 'workspace1', 'В процессе', 2, 'yellow'),
    (3, 'workspace1', 'Сделано', 3, 'green'),
    (4, 'workspace2', 'Бэклог', 1, 'blue'),
    (5, 'workspace2', 'Завершено', 2, 'purple');

-- Вставка данных в таблицу chat
insert into chat (id, workspace_id, created_by, title, type, creation_date)
values
    (1, 'workspace1', 'user1', 'Общий чат', 'group', CURRENT_TIMESTAMP),
    (2, 'workspace1', 'user2', 'Чат разработки', 'group', CURRENT_TIMESTAMP),
    (3, 'workspace2', 'user3', 'Чат дизайна', 'group', CURRENT_TIMESTAMP);

-- Вставка данных в таблицу chat_user
insert into chat_user (id, chat_id, user_id, join_at, role)
values
    (1, 1, 'user1', CURRENT_TIMESTAMP, 'admin'),
    (2, 1, 'user2', CURRENT_TIMESTAMP, 'member'),
    (3, 2, 'user1', CURRENT_TIMESTAMP, 'member'),
    (4, 2, 'user2', CURRENT_TIMESTAMP, 'admin'),
    (5, 3, 'user3', CURRENT_TIMESTAMP, 'admin'),
    (6, 1, 'user3', CURRENT_TIMESTAMP, 'guest'),
    (7, 3, 'user2', CURRENT_TIMESTAMP, 'member');

-- Вставка данных в таблицу card
insert into card (id, column_id, created_by, executor_id, sprint_id, title, content, comments, created_at, deadline)
values
    (1, 1, 'user1', 'user2', 1, 'Задача 1', 'Реализовать функцию X', 'Нет комментариев', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + interval '7 days'),
    (2, 2, 'user2', 'user3', 1, 'Задача 2', 'Исправить баг Y', 'Требует внимания', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + interval '5 days'),
    (3, 4, 'user3', null, 2, 'Задача 3', 'Разработать логотип', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + interval '14 days'),
    (4, 1, 'user2', 'user3', 1, 'Проверка статусов 1', 'Задача для показа статусов', 'Нет комментариев', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + interval '10 days'),
    (5, 3, 'user1', 'user2', 1, 'Задача завершена', 'Эта задача уже была завершена', 'Отлично!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - interval '1 day'),
    (6, 2, 'user3', null, 1, 'Задача в процессе', 'Сейчас в работе', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + interval '5 days');

-- Вставка данных в таблицу messages
insert into messages (id, conversation_id, sender_id, card_id, text_content, file_path, created_at, is_read)
values
    (1, 1, 'user1', null, 'Всем привет!', null, CURRENT_TIMESTAMP, false),
    (2, 1, 'user2', null, 'Привет, Алиса!', null, CURRENT_TIMESTAMP, false),
    (3, 2, 'user2', 1, null, null, CURRENT_TIMESTAMP, false),
    (4, 2, 'user1', null, 'Я завершил Задачу 1', null, CURRENT_TIMESTAMP, false),
    (5, 3, 'user3', null, 'Прикрепляю идеи дизайна', '/files/design1.png', CURRENT_TIMESTAMP, false);

-- Вставка данных в таблицу poll
insert into poll (id, author_id, chat_id, title, variants, answers)
values
    (1, 'user1', 1, 'Выберите время для встречи', ARRAY['Понедельник', 'Вторник', 'Среда'], ARRAY[0,0,0]),
    (2, 'user3', 3, 'Выберите предпочитаемый логотип', ARRAY['Логотип1', 'Логотип2'], ARRAY[0,0]);

-- Вставка данных в таблицу card_comments
insert into card_comments (id, user_id, card_id, reply_to_comment_id, content, created_at)
values
    (1, 'user2', 1, null, 'Отличная работа над этой задачей!', CURRENT_TIMESTAMP),
    (2, 'user1', 1, 1, 'Спасибо!', CURRENT_TIMESTAMP),
    (3, 'user3', 3, null, 'Пожалуйста, посмотрите дизайны.', CURRENT_TIMESTAMP);
