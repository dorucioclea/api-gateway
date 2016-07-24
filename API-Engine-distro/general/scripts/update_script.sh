#!/usr/bin/env bash

# Update for all environment the publisher consumer name to have a prefix: 'pub'
WILDFLY_USER="wildfly"
API_GATEWAY_USR_DIR=/usr/local/api-gateway

mkdir $API_GATEWAY_USR_DIR
chown -R $WILDFLY_USER:$WILDFLY_USER $API_GATEWAY_USR_DIR
chown -R $WILDFLY_USER:$WILDFLY_USER $API_GATEWAY_USR_DIR/