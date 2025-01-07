#!/bin/bash

git pull origin main

mvn clean install -DskipTests=true

APP_JAR=$(ls target/bitrosh-backend*.jar 2>/dev/null | head -n 1)

if [ -z "$APP_JAR" ]; then
  echo "No JAR file found after build process."
  exit 1
fi

PID=$(pgrep -f "$APP_JAR")
if [ -n "$PID" ]; then
  echo "Application already running with PID: $PID. Terminating process."
  kill "$PID"
  sleep 2
  if kill -0 "$PID" >/dev/null 2>&1; then
    echo "Process did not terminate. Forcing termination."
    kill -9 "$PID"
  fi
fi

nohup java -jar "$APP_JAR" > out.txt 2>&1 &

NEW_PID=$!
echo "New application started with PID: $NEW_PID"
