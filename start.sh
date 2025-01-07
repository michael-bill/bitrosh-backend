#!/bin/bash
git pull origin main
mvn clean package
nohup java -jar target/backend*.jar > out.txt 2>&1 &
echo "Application started with PID: $!"
