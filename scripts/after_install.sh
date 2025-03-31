#!/bin/bash

echo "✏ Parameter Store에서 환경변수 받아오기: $(date)"

echo "" > /home/ubuntu/.env

PARAMETERS=(
  "DATABASE_USERNAME"
  "DATABASE_PASSWORD"
  "DATABASE_URL"
  "DATABASE_DRIVER"
  "AWS_S3_ACCESSKEY"
  "AWS_S3_SECRETKEY"
  "AWS_S3_BUCKET"
  "AWS_REGION"
  "KAKAO_API_KEY"
  "KAKAO_CLIENT"
  "KAKAO_SECRET"
  "GOOGLE_CLIENT"
  "GOOGLE_SECRET"
  "JWT_SECRET"
  "FRONT_URL"
  "GOOGLE_REDIRECT_URI"
  "KAKAO_REDIRECT_URI"
  "ENV"
  "PROFILES"
  "GMAIL_ADDRESS"
  "GMAIL_PASSWORD"
  "REDIS_CLUSTER_NODES"
   "QR_SAVE_PATH"
)

for PARAM in "${PARAMETERS[@]}"
do
  VALUE=$(aws ssm get-parameter \
    --name "/prod/${PARAM}" \
    --with-decryption \
    --query Parameter.Value \
    --output text)

  echo "${PARAM}=${VALUE}" >> /home/ubuntu/.env
done

echo "✅ .env 파일 생성 완료!"
