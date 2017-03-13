--------- UPGRADE TO 0.10.0 STARTS HERE ----------

CREATE TABLE idps (id VARCHAR(255) NOT NULL, server_url VARCHAR(255) NOT NULL, master_realm VARCHAR(255) NOT NULL, client_id VARCHAR(255) NOT NULL, encrypted_client_secret VARCHAR(255) NOT NULL, default_idp BOOLEAN DEFAULT FALSE);
ALTER TABLE idps ADD PRIMARY KEY (id);

-- Replace the client secret with actual value

-- Store the IDP ids
ALTER TABLE application_versions ADD COLUMN idp_client_id VARCHAR(255) DEFAULT NULL;

CREATE TABLE keystores (id BIGINT NOT NULL, name VARCHAR(255) NOT NULL, encrypted_keystore_password VARCHAR(255) NOT NULL, encrypted_key_password VARCHAR(255) NOT NULL, default_keystore BOOLEAN DEFAULT FALSE);
ALTER TABLE keystores ADD PRIMARY KEY (id);

CREATE TABLE mail_providers (id BIGINT NOT NULL, host VARCHAR(255) NOT NULL, port BIGINT NOT NULL, auth BOOLEAN DEFAULT TRUE, mail_from VARCHAR(255) NOT NULL, username VARCHAR(255) NOT NULL, encrypted_password VARCHAR(255) NOT NULL, default_mail_provider BOOLEAN DEFAULT FALSE);
ALTER TABLE mail_providers ADD PRIMARY KEY (id);

