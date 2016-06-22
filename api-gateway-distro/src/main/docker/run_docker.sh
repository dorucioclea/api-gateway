#!/usr/bin/env bash
#you should have your docker machine running
docker start cassandra
docker start kong
docker start api-engine-db
docker start api-engine-inst1



