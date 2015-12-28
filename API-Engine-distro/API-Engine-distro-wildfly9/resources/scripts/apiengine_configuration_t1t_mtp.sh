#!/usr/bin/env bash
#register api engine
curl -i -X POST \
  --url http://apim.t1t.be:8001/apis/ \
  --data 'request_path=/apiengine/v1' \
  --data 'name=apiengine.v1' \
  --data 'upstream_url=http://api.t1t.be/API-Engine-web/v1/' \
  --data 'strip_request_path=true'

#enable CORS
curl -X POST http://apim.t1t.be:8001/apis/apiengine.v1/plugins \
    --data "name=cors" \
    --data "config.origin=*" \
    --data "config.methods=GET,HEAD,PUT,PATCH,POST,DELETE" \
    --data "config.headers=Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, apikey, Authorization" \
    --data "config.credentials=true" \
    --data "config.max_age=3600"

#enable Keyauth
curl -X POST http://apim.t1t.be:8001/apis/apiengine.v1/plugins \
    --data "name=key-auth" \
    --data "config.key_names=apikey"

#enable JWT
curl -X POST http://apim.t1t.be:8001/apis/apiengine.v1/plugins \
    --data "name=jwt"

#create marketplace consumer, username should be unique
curl -X POST http://apim.t1t.be:8001/consumers \
    --data "username=marketplace.v1"

#create publisher consumer
curl -X POST http://apim.t1t.be:8001/consumers \
    --data "username=publisher.v1"

#enable keyauth for marketplace and return API key, result should be captured and is the API key for the given consumer
curl -X POST http://apim.t1t.be:8001/consumers/marketplace.v1/key-auth \
    --data "key=6b8406cc81fe4ca3cc9cd4a0abfb97c2"

#enable keyauth for publisher and return API key, result should be captured and is the API key for the given consumer
curl -X POST http://apim.t1t.be:8001/consumers/publisher.v1/key-auth \
    --data "key=***REMOVED***"

#Analytics
curl -X POST http://apim.t1t.be:8001/apis/apiengine.v1/plugins/ \
    --data "name=mashape-analytics" \
    --data "config.service_token=558a95f80f7a734609de5c04" \
    --data "config.environment=t1t-prod"




#register api engine authorization endpoints
curl -i -X POST \
  --url http://apim.t1t.be:8001/apis/ \
  --data 'request_path=/apiengineauth/v1' \
  --data 'name=apiengineauth.v1' \
  --data 'upstream_url=http://api.t1t.be/API-Engine-auth/v1/' \
  --data 'strip_request_path=true'

#enable CORS
curl -X POST http://apim.t1t.be:8001/apis/apiengineauth.v1/plugins \
    --data "name=cors" \
    --data "config.origin=*" \
    --data "config.methods=GET,HEAD,PUT,PATCH,POST,DELETE" \
    --data "config.headers=Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, apikey, Authorization" \
    --data "config.credentials=true" \
    --data "config.max_age=3600"

#enable Keyauth
curl -X POST http://apim.t1t.be:8001/apis/apiengineauth.v1/plugins \
    --data "name=key-auth" \
    --data "config.key_names=apikey"

#Analytics
curl -X POST http://apim.t1t.be:8001/apis/apiengineauth.v1/plugins/ \
    --data "name=mashape-analytics" \
    --data "config.service_token=558a95f80f7a734609de5c04" \
    --data "config.environment=t1t-prod"
