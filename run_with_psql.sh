#!/bin/bash

service=$1

# Setup postgres
./run_psql.sh $service

# Run the service
./run.sh $service
