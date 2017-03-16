--------- UPGRADE TO 0.10.0 STARTS HERE ----------

CREATE TABLE idps (id VARCHAR(255) NOT NULL, server_url VARCHAR(255) NOT NULL, master_realm VARCHAR(255) NOT NULL, client_id VARCHAR(255) NOT NULL, encrypted_client_secret VARCHAR(255) NOT NULL, default_login_theme_id VARCHAR(255) DEFAULT NULL, default_client VARCHAR(255) DEFAULT NULL, default_idp BOOLEAN DEFAULT FALSE);
ALTER TABLE idps ADD PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_idps_1 ON idps (default_idp) WHERE default_idp = true;

-- Replace the client secret with actual value
INSERT INTO idps (id, server_url, master_realm, client_id, encrypted_client_secret, default_login_theme_id, default_client, default_idp) VALUES ('Keycloak','https://idp.t1t.be/auth', 'master', 'admin-cli', '$CRYPT::79mlpuQOXd0UQd++6UQdb85ocB2d8a7I+piu5tFFSPs3Btpll2vhf0Xf5DsqBRzc', 't1g', 'DefaultClient', TRUE);

-- Store the IDP ids
ALTER TABLE application_versions ADD COLUMN idp_client_id VARCHAR(255) DEFAULT NULL;

CREATE TABLE keystores (kid VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NULL, path VARCHAR(255) NOT NULL, encrypted_keystore_password VARCHAR(255) NOT NULL, encrypted_key_password VARCHAR(255) NOT NULL, private_key_alias VARCHAR(255) NOT NULL, priority BIGINT NOT NULL DEFAULT 150, default_keystore BOOLEAN DEFAULT FALSE);
ALTER TABLE keystores ADD PRIMARY KEY (kid);
CREATE UNIQUE INDEX uk_keystores_1 ON keystores (default_keystore) WHERE default_keystore = true;

CREATE TABLE mail_providers (id BIGINT NOT NULL, host VARCHAR(255) NOT NULL, port BIGINT NOT NULL, auth BOOLEAN DEFAULT TRUE, mail_from VARCHAR(255) NOT NULL, username VARCHAR(255) NOT NULL, encrypted_password VARCHAR(255) NOT NULL, default_mail_provider BOOLEAN DEFAULT FALSE);
ALTER TABLE mail_providers ADD PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_mail_providers_1 ON mail_providers (default_mail_provider) WHERE default_mail_provider = true;

INSERT INTO keystores (kid, name, path, encrypted_keystore_password, encrypted_key_password, private_key_alias, default_keystore) VALUES ('MrPfITJBMyIjkb5Fj8FK2uv6zgzBp9kIR7K2EzD_Iuo', 'T1T-IDP-JKS', '/usr/local/keystores/idp_kc_store.jks', '$CRYPT::Z5BlLMsBRYlNTfCWoE+59A==', '$CRYPT::Z5BlLMsBRYlNTfCWoE+59A==', 'idp.t1t.be', TRUE);
INSERT INTO mail_providers (id, host, port, auth, mail_from, username, encrypted_password, default_mail_provider) VALUES (700, 'smtp.mailgun.org', 2525, TRUE, 'postmaster@saas.t1t.be', 'postmaster@saas.t1t.be', '$CRYPT::hOiPp+FgNRs/ssMS6F4MPBL53jh7AYVweqjYfN0vbmWrilgr70Ww9MDQV415WD11', TRUE);

ALTER TABLE organizations ADD COLUMN mail_provider_id BIGINT NULL;
ALTER TABLE organizations ADD COLUMN keystore_kid VARCHAR(255) NULL;

ALTER TABLE organizations ADD CONSTRAINT fk_organizations_1 FOREIGN KEY (mail_provider_id) REFERENCES mail_providers (id);
ALTER TABLE organizations ADD CONSTRAINT fk_organizations_2 FOREIGN KEY (keystore_kid) REFERENCES keystores (kid);

DROP TABLE oauth_apps;

--------- UPGRADE TO 0.10.1 STARTS HERE ---------

-- Drop the oauth client id info from app versions, backwards compatability no longer required

ALTER TABLE application_versions DROP COLUMN oauth_client_id;