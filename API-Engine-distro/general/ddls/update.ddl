--------- UPGRADE TO 0.10.0 STARTS HERE ----------

CREATE TABLE idps (id VARCHAR(255) NOT NULL, server_url VARCHAR(255) NOT NULL, master_realm VARCHAR(255) NOT NULL, client_id VARCHAR(255) NOT NULL, client_secret VARCHAR(255) NOT NULL, default_idp BOOLEAN DEFAULT FALSE);
ALTER TABLE idps ADD PRIMARY KEY (id);
CREATE INDEX idx_idps ON idps(id);

-- Replace the client secret with actual value
INSERT INTO idps (id, server_url, master_realm, client_id, client_secret, default_idp) VALUES ('Keycloak','https://idp.t1t.be/auth', 'master', 'admin-cli', 'secret', TRUE);

-- Store the IDP ids
ALTER TABLE application_versions ADD COLUMN idp_client_id VARCHAR(255) DEFAULT NULL;
