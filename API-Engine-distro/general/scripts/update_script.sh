#!/usr/bin/env bash
dpkg-reconfigure tzdata

###IMPORTANT UPDATE FOR CASSANDRA###
#update all jwt_secrets values for column algorithm to contain HS256
#select id from jwt_secrets;
#copy paste in st - replace all /n (regex) with ,
#update jwt_secrets set algorithm = 'HS256' where id IN (90a5336f-a798-4799-b15a-9784a8bd2e06,...,522ffee5-79b3-4baa-b436-f257ef75e27c);#list from st
