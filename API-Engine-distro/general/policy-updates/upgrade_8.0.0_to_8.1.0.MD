Upgrade notes: 0.8.0 -> 0.8.1
-----------------------------
The following actions should be executed in order to upgrade towards API Engine v0.8.1:
 - replace cors_handler.lua on all Kong running nodes
 - replace key-auth_handler.lua on all Kong running nodes
 - add JWT-up: 
    - install plugin on all Kong running nodes: $ luarocks install kong-plugin-jwt-up
    - update certificates on all Kong running nodes: followe instruction on https://trust1t.atlassian.net/wiki/pages/viewpage.action?pageId=74547210
    - update kong.yaml
 - execture migration endpoint:
    - provides jwt credentials for all consumers (application consumers and end-user consumers)