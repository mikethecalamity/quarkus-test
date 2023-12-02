#!/bin/bash

service=$1
port=$2

# Setup postgres
./run_psql.sh $service

# Run the service
./run.sh $service $port
