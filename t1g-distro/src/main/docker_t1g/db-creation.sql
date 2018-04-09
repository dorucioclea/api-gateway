ALTER USER postgres
PASSWORD 'postgres';
CREATE DATABASE kong OWNER postgres;
CREATE DATABASE t1gengine OWNER postgres;
CREATE DATABASE keycloak OWNER postgres;