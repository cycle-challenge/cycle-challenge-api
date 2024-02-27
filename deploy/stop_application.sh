#!/bin/bash
echo "> 이전 서버의 프로세스 중지" >> /home/ubuntu/build/deploy.log
sudo pkill -f 'java -jar'