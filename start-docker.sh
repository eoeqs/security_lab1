#!/bin/bash

mvn clean package -DskipTests
docker-compose down
docker-compose up -d
sleep 10
docker-compose ps
docker-compose logs -f secure-api-app