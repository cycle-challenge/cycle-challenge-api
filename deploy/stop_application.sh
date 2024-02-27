#!/bin/bash

# 8080 포트를 사용하는 프로세스의 PID 찾기
PID=$(lsof -ti:8080)

# PID가 존재하면 (즉, 해당 포트를 사용하는 프로세스가 있으면)
if [ ! -z "$PID" ]; then
  echo "> 8080 포트를 사용하는 이전 서버 프로세스 PID($PID) 중지" >> /home/ubuntu/build/deploy.log
  # 프로세스 종료
  sudo kill $PID
else
  echo "> 8080 포트를 사용하는 서버 프로세스 없음" >> /home/ubuntu/build/deploy.log
fi