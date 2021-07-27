#!/usr/bin/env bash

#generate backend jar
sh gradlew clean build

#copy jar file
cp build/libs/performance-test-java-mysql.jar ./dockercompose/performance-test-java-mysql/app.jar

#start the containers
docker-compose up -d