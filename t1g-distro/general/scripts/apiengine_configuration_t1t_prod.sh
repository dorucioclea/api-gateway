#!/usr/bin/env bash
#register api gateway
curl -i -X POST \
  --url http://apim.t1t.be:8001/apis/ \
  --data 'request_path=/apigateway/v1' \
  --data 'name=apigateway.v1' \
  --data 'upstream_url=http://api.t1t.be/t1g-web/v1/' \
  --data 'strip_request_path=true'

#enable CORS
curl -X POST http://apim.t1t.be:8001/apis/apigateway.v1/plugins \
    --data "name=cors" \
    --data "config.origin=*" \
    --data "config.methods=GET,HEAD,PUT,PATCH,POST,DELETE" \
    --data "config.headers=Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, apikey, Authorization" \
    --data "config.credentials=true" \
    --data "config.max_age=3600"

#enable Keyauth
curl -X POST http://apim.t1t.be:8001/apis/apigateway.v1/plugins \
    --data "name=key-auth" \
    --data "config.key_names=apikey"

#enable JWT - we enforce JWT with a request filter
curl -X POST http://devapim.t1t.be:8001/apis/devapigateway.v1/plugins \
    --data "name=jwt" \
    --data "config.claims_to_verify=exp"

#create marketplace consumer, username should be unique
curl -X POST http://apim.t1t.be:8001/consumers \
    --data "username=marketplace.v1"
curl -X POST http://apim.t1t.be:8001/consumers \
    --data "username=int.marketplace.v1"
curl -X POST http://apim.t1t.be:8001/consumers \
    --data "username=ext.marketplace.v1"

#enable keyauth for marketplace and return API key, result should be captured and is the API key for the given consumer
curl -X POST http://apim.t1t.be:8001/consumers/all.marketplace.v1/key-auth \
    --data "key=6b8406cc81fe4ca3cc9cd4a0abfb97p2"
curl -X POST http://apim.t1t.be:8001/consumers/int.marketplace.v1/key-auth \
    --data "key=6b8406cc81fe4ca3cc9cd4a0abfb97p1"
curl -X POST http://apim.t1t.be:8001/consumers/ext.marketplace.v1/key-auth \
    --data "key=6b8406cc81fe4ca3cc9cd4a0abfb97p3"

#create publisher consumer
curl -X POST http://apim.t1t.be:8001/consumers \
    --data "username=pub.publisher.v1"

#enable keyauth for publisher and return API key, result should be captured and is the API key for the given consumer
curl -X POST http://apim.t1t.be:8001/consumers/pub.publisher.v1/key-auth \
    --data "key=***REMOVED***"

#Analytics
curl -X POST http://apim.t1t.be:8001/apis/apigateway.v1/plugins/ \
    --data "name=mashape-analytics" \
    --data "config.service_token=558a95f80f7a734609de5c04" \
    --data "config.environment=t1t-prod"




#register api gateway authorization endpoints
curl -i -X POST \
  --url http://apim.t1t.be:8001/apis/ \
  --data 'request_path=/apigatewayauth/v1' \
  --data 'name=apigatewayauth.v1' \
  --data 'upstream_url=http://api.t1t.be/t1g-auth/v1/' \
  --data 'strip_request_path=true'

#enable CORS
curl -X POST http://apim.t1t.be:8001/apis/apigatewayauth.v1/plugins \
    --data "name=cors" \
    --data "config.origin=*" \
    --data "config.methods=GET,HEAD,PUT,PATCH,POST,DELETE" \
    --data "config.headers=Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, apikey, Authorization" \
    --data "config.credentials=true" \
    --data "config.max_age=3600"

#enable Keyauth
curl -X POST http://apim.t1t.be:8001/apis/apigatewayauth.v1/plugins \
    --data "name=key-auth" \
    --data "config.key_names=apikey"

#Analytics
curl -X POST http://apim.t1t.be:8001/apis/apigatewayauth.v1/plugins/ \
    --data "name=mashape-analytics" \
    --data "config.service_token=558a95f80f7a734609de5c04" \
    --data "config.environment=t1t-prod"

#register api gateway contextless endpoints
curl -i -X POST \
  --url http://apim.t1t.be:8001/apis/ \
  --data 'request_path=/keys/' \
  --data 'name=gatewaykeys' \
  --data 'upstream_url=http://api.t1t.be/t1g-auth/v1/gtw/tokens/' \
  --data 'strip_request_path=true'

#enable CORS
curl -X POST http://apim.t1t.be:8001/apis/gatewaykeys/plugins \
    --data "name=cors" \
    --data "config.origin=*" \
    --data "config.methods=GET,HEAD,PUT,PATCH,POST,DELETE" \
    --data "config.headers=Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, apikey, Authorization" \
    --data "config.credentials=true" \
    --data "config.max_age=3600"
