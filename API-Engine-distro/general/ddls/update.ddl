-- db updates for next releas should come here
ALTER TABLE organizations ADD COLUMN private BOOL DEFAULT TRUE;

CREATE TABLE events (origin VARCHAR(255) NOT NULL, request_destination VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, status VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, modified_on TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE events ADD PRIMARY KEY (origin, destination, type);