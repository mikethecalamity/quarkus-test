#!/bin/bash

service=$1
./gradlew $service:quarkusBuild
sudo docker build -t $service -f modules/$service/Dockerfile .
sudo docker run -it -p 8080:8080 $service

