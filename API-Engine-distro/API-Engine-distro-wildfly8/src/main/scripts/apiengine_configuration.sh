#!/usr/bin/env bash
#register api
curl -i -X POST \
  --url http://apim.t1t.be:8001/apis/ \
  --data 'path=/dev/apiengine/v1' \
  --data 'name=dev.apiengine.v1' \
  --data 'target_url=http://api.t1t.be/API-Engine-web/v1/' \
  --data 'strip_path=true'

#enable CORS
curl -X POST http://apim.t1t.be:8001/apis/dev.apiengine.v1/plugins \
    --data "name=cors" \
    --data "value.origin=*" \
    --data "value.methods=GET,HEAD,PUT,PATCH,POST,DELETE" \
    --data "value.headers=Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, apikey" \
    --data "value.credentials=true" \
    --data "value.max_age=3600"

#enable Keyauth
curl -X POST http://apim.t1t.be:8001/apis/dev.apiengine.v1/plugins \
    --data "name=keyauth" \
    --data "value.key_names=apikey"

#create marketplace consumer, username should be unique
curl -X POST http://apim.t1t.be:8001/consumers \
    --data "username=dev.marketplace.v1"

#create publisher consumer
curl -X POST http://apim.t1t.be:8001/consumers \
    --data "username=dev.publisher.v1"

#enable keyauth for marketplace and return API key, result should be captured and is the API key for the given consumer
curl -X POST http://apim.t1t.be:8001/consumers/dev.marketplace.v1/keyauth \
    --data "key=6b8406cc81fe4ca3cc9cd4a0abfb97c2"

#enable keyauth for publisher and return API key, result should be captured and is the API key for the given consumer
curl -X POST http://apim.t1t.be:8001/consumers/dev.publisher.v1/keyauth \
    --data "key=***REMOVED***"
