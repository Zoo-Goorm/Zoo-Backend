#!/bin/bash

sudo nohup /home/ubuntu/node_exporter-1.8.2.linux-amd64/node_exporter > /home/ubuntu/my-app.log 2>&1 &

echo "🚀 [$(date)] blue 컨테이너 실행 시작"
sudo docker-compose -f /home/ubuntu/docker-compose-blue.yml pull && \
sudo docker-compose -f /home/ubuntu/docker-compose-blue.yml up -d
echo "✅ [$(date)] blue 컨테이너 실행 성공" || \
echo "❌ [$(date)] blue 컨테이너 실행 실패"

echo "🚀 [$(date)] nginx 컨테이너 실행 시작"
sudo docker-compose -f /home/ubuntu/docker-compose-nginx.yml up -d && \
echo "✅ [$(date)] nginx 컨테이너 실행 성공" || \
echo "❌ [$(date)] nginx 컨테이너 실행 실패"