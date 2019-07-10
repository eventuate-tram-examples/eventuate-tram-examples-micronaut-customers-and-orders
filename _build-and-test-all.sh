#! /bin/bash

set -e


. ./set-env-${DATABASE?}.sh

docker-compose -f docker-compose-${DATABASE?}${MODE?}.yml down -v

docker-compose -f docker-compose-${DATABASE?}${MODE?}.yml up -d --build zookeeper ${DATABASE?} kafka

./wait-for-${DATABASE?}.sh

docker-compose -f docker-compose-${DATABASE?}${MODE?}.yml up -d --build cdcservice

./wait-for-services.sh $DOCKER_HOST_IP "8099" "actuator/health"

./gradlew -x :end-to-end-tests:test build

docker-compose -f docker-compose-${DATABASE?}${MODE?}.yml up -d --build

./wait-for-services.sh $DOCKER_HOST_IP "8081 8083" "actuator/health"
./wait-for-services.sh $DOCKER_HOST_IP "8082" "health"

./gradlew :end-to-end-tests:cleanTest :end-to-end-tests:test

docker-compose -f docker-compose-${DATABASE?}${MODE?}.yml down -v
