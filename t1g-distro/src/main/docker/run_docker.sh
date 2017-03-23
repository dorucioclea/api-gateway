#!/usr/bin/env bash
#you should have your docker machine running
docker start cassandra
docker start kong
docker start api-gateway-db
docker start api-gateway-inst1



