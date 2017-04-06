--------- UPGRADE TO 1.0.0 STARTS HERE ----------

CREATE TABLE idps (id VARCHAR(255) NOT NULL, server_url VARCHAR(255) NOT NULL, master_realm VARCHAR(255) NOT NULL, client_id VARCHAR(255) NOT NULL, encrypted_client_secret VARCHAR(255) NOT NULL, default_login_theme_id VARCHAR(255) DEFAULT NULL, default_realm VARCHAR(255) NOT NULL, default_client VARCHAR(255) NOT NULL, default_idp BOOLEAN DEFAULT FALSE);
ALTER TABLE idps ADD PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_idps_1 ON idps (default_idp) WHERE default_idp = true;

-- Replace the client secret with actual encrypted value
INSERT INTO idps (id, server_url, master_realm, client_id, encrypted_client_secret, default_login_theme_id, default_realm, default_client, default_idp) VALUES ('Keycloak','https://devidp.t1t.be/auth', 'master', 'admin-cli', 'INSERT_ENCRYPTED_SECRET_HERE', 't1g', 'DefaultRealm','DefaultClient', TRUE);

-- Store the IDP ids
ALTER TABLE application_versions ADD COLUMN idp_client_id VARCHAR(255) DEFAULT NULL;

CREATE TABLE keystores (kid VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NULL, path VARCHAR(255) NOT NULL, encrypted_keystore_password VARCHAR(255) NOT NULL, encrypted_key_password VARCHAR(255) NOT NULL, private_key_alias VARCHAR(255) NOT NULL, priority BIGINT NOT NULL DEFAULT 150, default_keystore BOOLEAN DEFAULT FALSE);
ALTER TABLE keystores ADD PRIMARY KEY (kid);
CREATE UNIQUE INDEX uk_keystores_1 ON keystores (default_keystore) WHERE default_keystore = true;

CREATE TABLE mail_providers (id BIGINT NOT NULL, host VARCHAR(255) NOT NULL, port BIGINT NOT NULL, auth BOOLEAN DEFAULT TRUE, mail_from VARCHAR(255) NOT NULL, username VARCHAR(255) NOT NULL, encrypted_password VARCHAR(255) NOT NULL, default_mail_provider BOOLEAN DEFAULT FALSE);
ALTER TABLE mail_providers ADD PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_mail_providers_1 ON mail_providers (default_mail_provider) WHERE default_mail_provider = true;

-- Replace the passwords with their actual encrypted values before inserting

INSERT INTO keystores (kid, name, path, encrypted_keystore_password, encrypted_key_password, private_key_alias, default_keystore) VALUES ('INSERT_KEYSTORE_KID_FROM_IDP_HERE', 'T1T-IDP-JKS', '/usr/local/keystores/idp_kc_store.jks', 'INSERT_ENCRYPTED_KEYSTORE_PASSWORD_HERE', 'INSERT_ENCRYPTED_PRIVATE_KEY_PASSWORD_HERE', 'idp.t1t.be', TRUE);
INSERT INTO mail_providers (id, host, port, auth, mail_from, username, encrypted_password, default_mail_provider) VALUES (700, 'smtp.mailgun.org', 2525, TRUE, 'postmaster@saas.t1t.be', 'postmaster@saas.t1t.be', 'INSERT_ENCRYPTED_PASSWORD_HERE', TRUE);

ALTER TABLE organizations ADD COLUMN mail_provider_id BIGINT NULL;
ALTER TABLE organizations ADD COLUMN keystore_kid VARCHAR(255) NULL;

ALTER TABLE organizations ADD CONSTRAINT fk_organizations_1 FOREIGN KEY (mail_provider_id) REFERENCES mail_providers (id);
ALTER TABLE organizations ADD CONSTRAINT fk_organizations_2 FOREIGN KEY (keystore_kid) REFERENCES keystores (kid);

-- Kong 0.10.1 Upgrade

CREATE TABLE service_basepaths AS SELECT services.organization_id AS servicebean_organization_id, services.id AS servicebean_id, services.basepath FROM services;
ALTER TABLE service_basepaths ADD CONSTRAINT fk_service_basepaths_1 FOREIGN KEY (servicebean_organization_id, servicebean_id) REFERENCES services (organization_id, id);
ALTER TABLE service_basepaths ADD CONSTRAINT uk_service_basepaths_1 UNIQUE (servicebean_organization_id, servicebean_id, basepath);
CREATE INDEX idx_service_basepaths_1 ON service_basepaths (servicebean_organization_id, servicebean_id) ;

CREATE TABLE service_hosts (service_version_id BIGINT NOT NULL, hostname VARCHAR(255) NOT NULL);
ALTER TABLE service_hosts ADD CONSTRAINT fk_service_hosts_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id);
ALTER TABLE service_hosts ADD CONSTRAINT uk_service_hosts_1 UNIQUE (service_version_id, hostname);
CREATE INDEX idx_service_hosts_1 ON service_hosts (service_version_id);

ALTER TABLE service_versions ADD COLUMN upstream_connect_timeout BIGINT DEFAULT 60000;
ALTER TABLE service_versions ADD COLUMN upstream_send_timeout BIGINT DEFAULT 60000;
ALTER TABLE service_versions ADD COLUMN upstream_read_timeout BIGINT DEFAULT 60000;

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('AWSLambda', 'Invoke an AWS Lambda function from Kong. It can be used in combination with other request plugins to secure, manage or extend the function.', '{
  "type": "object",
  "properties": {
    "aws_key": {
      "title": "AWS Key",
      "description": "The AWS key credential to be used when invoking the function.",
      "type": "string",
      "required": true
    },
    "aws_secret": {
      "title": "AWS Secret",
      "description": "The AWS secret credential to be used when invoking the function.",
      "type": "string",
      "required": true
    },
    "function_name": {
      "title": "Function Name",
      "description": "The AWS Lambda function name to invoke",
      "type": "string",
      "required": true
    },
    "aws_region": {
      "title": "AWS Region",
      "description": "The AWS region where the Lambda function is located. Regions supported are: us-east-1, us-east-2, ap-northeast-1, ap-northeast-2, ap-southeast-1, ap-southeast-2, eu-central-1, eu-west-1.",
      "type": "string",
      "required": true,
      "enum": [
        "us-east-1",
        "us-east-2",
        "ap-northeast-1",
        "ap-northeast-2",
        "us-west-2",
        "ap-southeast-1",
        "ap-southeast-2",
        "eu-central-1",
        "eu-west-1"
      ]
    },
    "qualifier": {
      "title": "Qualifier",
      "description": "The Qualifier to use when invoking the function.",
      "type": "string"
    },
    "invocation_type": {
      "title": "Invocation Type",
      "description": "The InvocationType to use when invoking the function. Available types are RequestResponse, Event, DryRun.",
      "type": "string",
      "required": true,
      "enum": [
        "RequestResponse",
        "Event",
        "DryRun"
      ],
      "default": "RequestResponse"
    },
    "log_type": {
      "title": "Log Type",
      "description": "The LogType to use when invoking the function. By default None and Tail are supported.",
      "type": "string",
      "required": true,
      "enum": [
        "Tail",
        "None"
      ],
      "default": "Tail"
    },
    "timeout": {
      "title": "Timeout",
      "description": "The timeout value in milliseconds.",
      "type": "number",
      "required": true,
      "default": 60000
    },
    "keepalive": {
      "title": "Keepalive",
      "description": "The keepalive value in millisconds.",
      "type": "number",
      "required": true,
      "default": 60000
    }
  }
}', 'JsonSchema', 'fa-amazon', 'AWS Lambda Policy', null, TRUE, FALSE, FALSE, null, null);

-- These sections are for breaking changes. We attempt to always be able to roll back one version/release

--------- UPGRADE TO 1.0.1 STARTS HERE ---------

DROP TABLE oauth_apps;

ALTER TABLE application_versions DROP COLUMN oauth_client_id;

ALTER TABLE services DROP COLUMN basepath;