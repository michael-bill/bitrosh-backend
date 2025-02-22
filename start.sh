#!/bin/bash
set -e  # Прерывать выполнение при ошибках

# 1. Обновление кода
echo "=== Pulling latest code ==="
git pull

# 2. Остановка и удаление контейнеров проекта
echo -e "\n=== Stopping and cleaning project containers ==="
docker-compose down --rmi local --volumes --remove-orphans

# 3. Запуск новой сборки
echo -e "\n=== Starting fresh deployment ==="
docker-compose up --build --force-recreate -d

echo -e "\n=== Deployment complete! ==="
