#register api
curl -X POST http://apim.t1t.be:8001/apis \
    --data "name=api.engine"
    --data "path="
    --data "strip_path=true"
    --data "target_url=http://api.t1t.be/API-Engine-web/v1/"

#enable CORS
curl -X POST http://apim.t1t.be:8001/apis/{api}/plugins \
    --data "name=cors" \
    --data "value.origin=*" \
    --data "value.methods=GET,HEAD,PUT,PATCH,POST,DELETE" \
    --data "value.headers=Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, apikey" \
    --data "value.exposed_headers=X-Auth-Token" \
    --data "value.credentials=true" \
    --data "value.max_age=3600"

#enable Keyauth

#create marketplace consumer
#enable keyauth for marketplace and return API key
#create publisher consumer
#enable keyauth for publisher and return API key