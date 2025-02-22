--liquibase formatted sql

--changeset michael-bill:add_test_data

-- 1. Заполнение workspace
insert into workspace (name, title, created_at) values
('ws1', 'Первое рабочее пространство', '2024-01-01 09:00:00'),
('ws2', 'Второе рабочее пространство', '2024-02-01 10:00:00');

-- 2. Заполнение users (сначала без current_workspace_id)
insert into users (username, password, role) values
('admin', '$2a$10$RPNOc9j0WOphyWlKxqzs1ukBSm2yI0WszzqCMv4oHwvFE0hjrtEQm', 'ADMIN'),
('user1', '$2a$10$BkyS7WBOpdq/IWnwdd49u.65xbKC7EVAUWlWnYNyrmZP1jIIe6eqi', 'USER'),
('user2', '$2a$10$YRlZeQZ9h.TOFPfsbR083OcWyyJWpZfV6K7UvNJ4XFdIZyt4Kx7nW', 'USER'),
('manager', '$2a$10$Kcwrys2Fic0lRCfG0SAljeR1H4GaS6QkQ0fr4nTwy/ZTX2ndmtREW', 'USER');

-- 3. Связываем пользователей с workspace
insert into user_workspace (user_id, workspace_id, role_id) values
(1, 'ws1', 1),
(2, 'ws1', 2),
(3, 'ws1', 3),
(4, 'ws2', 1);

-- 4. Обновляем current_workspace_id у пользователей
update users set current_workspace_id = 'ws1' WHERE id IN (1,2,3);
update users set current_workspace_id = 'ws2' WHERE id = 4;

-- 5. Создаем колонки доски
insert into board_columns (workspace_id, title, level, color) values
('ws1', 'Запланировано', 1, '#FF0000'),
('ws1', 'В работе', 2, '#00FF00'),
('ws1', 'Готово', 3, '#0000FF'),
('ws2', 'Backlog', 1, '#CCCCCC'),
('ws2', 'Sprint', 2, '#FFFF00');

-- 6. Создаем спринты
insert into sprint (workspace_id) values
('ws1'), ('ws1'), ('ws2');

-- 7. Создаем папки
insert into folder (user_id, name, workspace_id) values
(1, 'Мои чаты', 'ws1'),
(2, 'Архив', 'ws1'),
(4, 'Основные', 'ws2');

-- 8. Заполняем чаты
insert into chat (workspace_id, created_by, created_at, title, type) values
('ws1', 1, '2024-01-02 10:00:00', 'Общий чат', 'GROUP'),
('ws1', 2, '2024-01-03 11:00:00', 'Технические вопросы', 'CHANNEL'),
('ws1', 3, '2024-01-03 11:30:00', null, 'PRIVATE'),
('ws2', 4, '2024-02-02 12:00:00', 'Основной чат', 'GROUP');

-- 9. Связь пользователей с чатами
insert into chat_user (chat_id, user_id, join_at, role_id) values
(1, 1, '2024-01-02 10:00:00', 1),
(1, 2, '2024-01-02 10:05:00', 2),
(2, 2, '2024-01-03 11:00:00', 1),
(3, 1, '2024-02-02 12:00:00', 1),
(3, 2, '2024-02-02 12:00:00', 1);

-- 10. Сообщения
insert into messages (chat_id, sender_id, text_content, created_at) values
(1, 1, 'Добро пожаловать в общий чат!', '2024-01-02 10:00:00'),
(1, 2, 'Привет всем!', '2024-01-02 10:05:00'),
(3, 4, 'Начинаем новый проект', '2024-02-02 12:05:00');

-- 11. Карточки
insert into card (column_id, created_by, sprint_id, title, content, deadline) values
(1, 1, 1, 'Настройка сервера', 'Установить необходимое ПО', '2026-02-02 12:05:00'),
(2, 2, 1, 'Разработка фичи', 'Интеграция с API', '2026-02-02 12:05:00'),
(5, 4, 3, 'Планирование', 'Составить roadmap', '2026-02-02 12:05:00');

-- 12. Комментарии к карточкам
insert into card_comments (user_id, card_id, content) values
(1, 1, 'Начните с установки Docker'),
(2, 2, 'API документация готова?');

