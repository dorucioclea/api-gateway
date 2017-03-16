-- v0.9.3 -> 0.9.5

ALTER TABLE applications ADD COLUMN email VARCHAR(255) DEFAULT NULL;

CREATE TABLE operating_modes (id VARCHAR(255) NOT NULL, enabled BOOL NOT NULL DEFAULT FALSE, message VARCHAR(255));
INSERT INTO operating_modes VALUES ('MAINTENANCE', false);

ALTER TABLE application_versions ADD COLUMN oauth_credential_id VARCHAR(255) DEFAULT NULL;

CREATE TABLE oauth2_tokens (id VARCHAR(255) NOT NULL, credential_id VARCHAR(255) NOT NULL, token_type VARCHAR(255) NOT NULL, access_token VARCHAR(255) NOT NULL, refresh_token VARCHAR(255) DEFAULT NULL, expires_in BIGINT NOT NULL, authenticated_userid VARCHAR(255) DEFAULT NULL, scope VARCHAR(4096) DEFAULT NULL, gateway_id VARCHAR(255) NOT NULL);
ALTER TABLE oauth2_tokens ADD PRIMARY KEY (id);
CREATE INDEX idx_oauth2_tokens_1 ON oauth2_tokens(credential_id);

ALTER TABLE users ADD COLUMN jwt_key VARCHAR(255) DEFAULT NULL;
ALTER TABLE users ADD COLUMN jwt_secret VARCHAR(255) DEFAULT NULL;

ALTER TABLE application_versions ADD COLUMN jwt_key VARCHAR(255) DEFAULT NULL;
ALTER TABLE application_versions ADD COLUMN jwt_secret VARCHAR(255) DEFAULT NULL;