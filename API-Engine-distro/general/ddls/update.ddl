-- db updates for next releas should come here
ALTER TABLE organizations ADD COLUMN private BOOL DEFAULT TRUE;

CREATE TABLE events (request_origin VARCHAR(255) NOT NULL, request_destination VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, status VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, modified_on TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE events ADD PRIMARY KEY (request_origin, request_destination, type);