#!/bin/bash

service=$1

./gradlew $service:quarkusBuild

if [ $? -eq 0 ]; then
  sudo docker build -t $service -f modules/$service/Dockerfile .
   
  if [ $? -eq 0 ]; then
    sudo docker run -it -p 8080:8080 $service
  fi
fi
