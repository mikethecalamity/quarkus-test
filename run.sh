#!/bin/bash

service=$1

./gradlew -p apps $service:quarkusBuild

if [ $? -eq 0 ]; then
  sudo docker build -t $service -f apps/subprojects/$service/Dockerfile .
   
  if [ $? -eq 0 ]; then
    sudo docker stop $service
    sudo docker rm $service
    sudo docker run --name $service -it -p 8080:8080 -d $service
  fi
fi
