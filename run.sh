#!/bin/bash

service=$1
port=$2
build_args="$3"

./gradlew -p apps $service:quarkusBuild

set -x
if [ $? -eq 0 ]; then
  sudo docker build -t $service -f apps/subprojects/$service/Dockerfile ${build_args} .
   
  if [ $? -eq 0 ]; then
    sudo docker stop $service
    sudo docker rm $service
    sudo docker run --name $service -it -p $port:$port -d $service
  fi
fi
set +x
