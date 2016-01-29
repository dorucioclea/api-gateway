#!/usr/bin/env bash
#register api engine
curl -i -X POST \
  --url http://rasu076.rte.antwerpen.local:8001/apis/ \
  --data 'request_path=/rte/apiengine/v1' \
  --data 'name=rte.apiengine.v1' \
  --data 'upstream_url=http://rasu073.rte.antwerpen.local/API-Engine-web/v1/' \
  --data 'strip_request_path=true'

#enable CORS
curl -X POST http://rasu076.rte.antwerpen.local:8001/apis/rte.apiengine.v1/plugins \
    --data "name=cors" \
    --data "config.origin=*" \
    --data "config.methods=GET,HEAD,PUT,PATCH,POST,DELETE" \
    --data "config.headers=Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, apikey, Authorization" \
    --data "config.credentials=true" \
    --data "config.max_age=3600"

#enable Keyauth
curl -X POST http://rasu076.rte.antwerpen.local:8001/apis/rte.apiengine.v1/plugins \
    --data "name=key-auth" \
    --data "config.key_names=apikey"

#enable JWT
curl -X POST http://rasu076.rte.antwerpen.local:8001/apis/rte.apiengine.v1/plugins \
    --data "name=jwt" \
    --data "config.claims_to_verify=exp"

#create marketplace consumer, username should be unique
curl -X POST http://rasu076.rte.antwerpen.local:8001/consumers \
    --data "username=rte.marketplace.v1"

#create publisher consumer
curl -X POST http://rasu076.rte.antwerpen.local:8001/consumers \
    --data "username=rte.publisher.v1"

#enable keyauth for marketplace and return API key, result should be captured and is the API key for the given consumer
curl -X POST http://rasu076.rte.antwerpen.local:8001/consumers/rte.marketplace.v1/key-auth \
    --data "key=229e2ea08ba94919c9d221cdf3be1f7d"

#enable keyauth for publisher and return API key, result should be captured and is the API key for the given consumer
curl -X POST http://rasu076.rte.antwerpen.local:8001/consumers/rte.publisher.v1/key-auth \
    --data "key=***REMOVED***"

#Analytics
curl -X POST http://rasu076.rte.antwerpen.local:8001/apis/rte.apiengine.v1/plugins/ \
    --data "name=mashape-analytics" \
    --data "config.service_token=558a95f80f7a734609de5c04" \
    --data "config.environment=digi-acc"




#register api engine authorization endpoints
curl -i -X POST \
  --url http://rasu076.rte.antwerpen.local:8001/apis/ \
  --data 'request_path=/rte/apiengineauth/v1' \
  --data 'name=rte.apiengineauth.v1' \
  --data 'upstream_url=http://rasu073.rte.antwerpen.local/API-Engine-auth/v1/' \
  --data 'strip_request_path=true'

#enable CORS
curl -X POST http://rasu076.rte.antwerpen.local:8001/apis/rte.apiengineauth.v1/plugins \
    --data "name=cors" \
    --data "config.origin=*" \
    --data "config.methods=GET,HEAD,PUT,PATCH,POST,DELETE" \
    --data "config.headers=Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, apikey, Authorization" \
    --data "config.credentials=true" \
    --data "config.max_age=3600"

#enable Keyauth
curl -X POST http://rasu076.rte.antwerpen.local:8001/apis/rte.apiengineauth.v1/plugins \
    --data "name=key-auth" \
    --data "config.key_names=apikey"

#Analytics
curl -X POST http://rasu076.rte.antwerpen.local:8001/apis/rte.apiengineauth.v1/plugins/ \
    --data "name=mashape-analytics" \
    --data "config.service_token=558a95f80f7a734609de5c04" \
    --data "config.environment=digi-acc"