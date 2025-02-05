#!/bin/bash

service=$1

# Pull and run the postgresql image
sudo docker pull postgres:16
sudo docker stop postgresql
sudo docker rm postgresql
sudo docker run --name postgresql -e POSTGRES_HOST_AUTH_METHOD=trust -p 5432:5432 -d postgres
sleep 10

# Load the initial data into the DB
sudo docker cp apps/subprojects/$service/db/setup.sql postgresql:setup.sql
sudo docker exec -u postgres postgresql psql postgres postgres -f setup.sql
