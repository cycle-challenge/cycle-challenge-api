#!/bin/bash
source ~/.profile
echo "> 새로운 프로세스 시작" >> /home/ubuntu/build/deploy.log
java -jar /home/ubuntu/build/*.jar > /home/ubuntu/build/run.log 2> /home/ubuntu/build/error.log < /dev/null &