-- db updates for next releas should come here
ALTER TABLE service_versions ADD COLUMN auto_accept_contracts BOOL DEFAULT TRUE;

ALTER TABLE organizations ADD COLUMN private BOOL DEFAULT TRUE;

CREATE TABLE events (id BIGINT NOT NULL, origin_id VARCHAR(255) NOT NULL, destination_id VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, body VARCHAR(4096));

ALTER TABLE events ADD PRIMARY KEY (id);

ALTER TABLE events ADD CONSTRAINT UK_events_1 UNIQUE (origin_id, destination_id, type);