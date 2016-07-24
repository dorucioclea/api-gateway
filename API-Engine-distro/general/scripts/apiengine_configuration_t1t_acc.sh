#!/usr/bin/env bash
#register api engine
curl -i -X POST \
  --url http://accapim.t1t.be:8001/apis/ \
  --data 'request_path=/acc/apiengine/v1' \
  --data 'name=acc.apiengine.v1' \
  --data 'upstream_url=http://accapi.t1t.be/API-Engine-web/v1/' \
  --data 'strip_request_path=true'

#enable CORS
curl -X POST http://accapim.t1t.be:8001/apis/acc.apiengine.v1/plugins \
    --data "name=cors" \
    --data "config.origin=*" \
    --data "config.methods=GET,HEAD,PUT,PATCH,POST,DELETE" \
    --data "config.headers=Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, apikey, Authorization" \
    --data "config.credentials=false" \
    --data "config.max_age=3600"

#enable Keyauth
curl -X POST http://accapim.t1t.be:8001/apis/acc.apiengine.v1/plugins \
    --data "name=key-auth" \
    --data "config.key_names=apikey"

#enable JWT - we enforce JWT with a request filter
curl -X POST http://accapim.t1t.be:8001/apis/acc.apiengine.v1/plugins \
    --data "name=jwt" \
    --data "config.claims_to_verify=exp"

#create marketplace consumer, username should be unique
curl -X POST http://accapim.t1t.be:8001/consumers \
    --data "username=all.marketplace.v1"
curl -X POST http://accapim.t1t.be:8001/consumers \
    --data "username=int.marketplace.v1"
curl -X POST http://accapim.t1t.be:8001/consumers \
    --data "username=ext.marketplace.v1"
curl -X POST http://accapim.t1t.be:8001/consumers \
    --data "username=acpaas.consent_app_nodejs.v1"

#enable keyauth for marketplace and return API key, result should be captured and is the API key for the given consumer
curl -X POST http://accapim.t1t.be:8001/consumers/all.marketplace.v1/key-auth \
    --data "key=6b8406cc81fe4ca3cc9cd4a0abfb97c2"
curl -X POST http://accapim.t1t.be:8001/consumers/int.marketplace.v1/key-auth \
    --data "key=***REMOVED***"
curl -X POST http://accapim.t1t.be:8001/consumers/ext.marketplace.v1/key-auth \
    --data "key=***REMOVED***"
curl -X POST http://accapim.t1t.be:8001/consumers/acpaas.consent_app_nodejs.v1/key-auth \
    --data "key=229e2ea08ba94919c9d221cdf3be1124"

#create publisher consumer
curl -X POST http://accapim.t1t.be:8001/consumers \
    --data "username=pub.publisher.v1"

#enable keyauth for publisher and return API key, result should be captured and is the API key for the given consumer
curl -X POST http://accapim.t1t.be:8001/consumers/pub.publisher.v1/key-auth \
    --data "key=***REMOVED***"

#Analytics
curl -X POST http://accapim.t1t.be:8001/apis/acc.apiengine.v1/plugins/ \
    --data "name=mashape-analytics" \
    --data "config.service_token=558a95f80f7a734609de5c04" \
    --data "config.environment=t1t-acc"




#register api engine authorization endpoints
curl -i -X POST \
  --url http://accapim.t1t.be:8001/apis/ \
  --data 'request_path=/acc/apiengineauth/v1' \
  --data 'name=acc.apiengineauth.v1' \
  --data 'upstream_url=http://accapi.t1t.be/API-Engine-auth/v1/' \
  --data 'strip_request_path=true'

#enable CORS
curl -X POST http://accapim.t1t.be:8001/apis/acc.apiengineauth.v1/plugins \
    --data "name=cors" \
    --data "config.origin=*" \
    --data "config.methods=GET,HEAD,PUT,PATCH,POST,DELETE" \
    --data "config.headers=Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, apikey, Authorization" \
    --data "config.credentials=true" \
    --data "config.max_age=3600"

#enable Keyauth
curl -X POST http://accapim.t1t.be:8001/apis/acc.apiengineauth.v1/plugins \
    --data "name=key-auth" \
    --data "config.key_names=apikey"

#Analytics
curl -X POST http://accapim.t1t.be:8001/apis/acc.apiengineauth.v1/plugins/ \
    --data "name=mashape-analytics" \
    --data "config.service_token=558a95f80f7a734609de5c04" \
    --data "config.environment=t1t-acc"
