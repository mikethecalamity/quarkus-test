#!/bin/bash

service=$1
port=$2

# Setup postgres
#./run_psql.sh $service

# Get the IP adress of the postgres container
POSTGRES_HOST=$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' postgresql)

# Run the service
./run.sh $service $port "--build-arg POSTGRES_HOST=${POSTGRES_HOST}"
