#!/usr/bin/env bash
#register api engine
curl -i -X POST \
  --url http://devasu018.dev.digant.antwerpen.local:8001/apis/ \
  --data 'path=/dev/apiengine/v1' \
  --data 'name=dev.apiengine.v1' \
  --data 'target_url=http://devasu016.dev.digant.antwerpen.local/API-Engine-web/v1/' \
  --data 'strip_path=true'

#enable CORS
curl -X POST http://devasu018.dev.digant.antwerpen.local:8001/apis/dev.apiengine.v1/plugins \
    --data "name=cors" \
    --data "value.origin=*" \
    --data "value.methods=GET,HEAD,PUT,PATCH,POST,DELETE" \
    --data "value.headers=Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, apikey, Authorization" \
    --data "value.credentials=true" \
    --data "value.max_age=3600"

#enable Keyauth
curl -X POST http://devasu018.dev.digant.antwerpen.local:8001/apis/dev.apiengine.v1/plugins \
    --data "name=keyauth" \
    --data "value.key_names=apikey"

#create marketplace consumer, username should be unique
curl -X POST http://devasu018.dev.digant.antwerpen.local:8001/consumers \
    --data "username=dev.marketplace.v1"

#create publisher consumer
curl -X POST http://devasu018.dev.digant.antwerpen.local:8001/consumers \
    --data "username=dev.publisher.v1"

#enable keyauth for marketplace and return API key, result should be captured and is the API key for the given consumer
curl -X POST http://devasu018.dev.digant.antwerpen.local:8001/consumers/dev.marketplace.v1/keyauth \
    --data "key=229e2ea08ba94919c9d221cdf3be1f7d"

#enable keyauth for publisher and return API key, result should be captured and is the API key for the given consumer
curl -X POST http://devasu018.dev.digant.antwerpen.local:8001/consumers/dev.publisher.v1/keyauth \
    --data "key=***REMOVED***"

#Analytics
curl -X POST http://devasu018.dev.digant.antwerpen.local:8001/apis/dev.apiengine.v1/plugins/ \
    --data "name=mashape-analytics" \
    --data "value.service_token=558a95f80f7a734609de5c04" \
    --data "value.environment=RTE"




#register api engine authorization endpoints
curl -i -X POST \
  --url http://devasu018.dev.digant.antwerpen.local:8001/apis/ \
  --data 'path=/dev/apiengineauth/v1' \
  --data 'name=dev.apiengineauth.v1' \
  --data 'target_url=http://devasu016.dev.digant.antwerpen.local/API-Engine-auth/v1/' \
  --data 'strip_path=true'

#enable CORS
curl -X POST http://devasu018.dev.digant.antwerpen.local:8001/apis/dev.apiengineauth.v1/plugins \
    --data "name=cors" \
    --data "value.origin=*" \
    --data "value.methods=GET,HEAD,PUT,PATCH,POST,DELETE" \
    --data "value.headers=Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, apikey, Authorization" \
    --data "value.credentials=true" \
    --data "value.max_age=3600"

#enable Keyauth
curl -X POST http://devasu018.dev.digant.antwerpen.local:8001/apis/dev.apiengineauth.v1/plugins \
    --data "name=keyauth" \
    --data "value.key_names=apikey"

#Analytics
curl -X POST http://devasu018.dev.digant.antwerpen.local:8001/apis/dev.apiengineauth.v1/plugins/ \
    --data "name=mashape-analytics" \
    --data "value.service_token=558a95f80f7a734609de5c04" \
    --data "value.environment=RTE"