-- TABLES

CREATE TABLE announcements
(
  id BIGINT NOT NULL,
  organization_id VARCHAR(255) NOT NULL,
  service_id VARCHAR(255) NOT NULL,
  title VARCHAR(255) NOT NULL,
  status VARCHAR(255),
  description TEXT,
  created_on TIMESTAMP NOT NULL,
  created_by VARCHAR(255) NOT NULL
);

CREATE TABLE app_oauth_redirect_uris
(
  application_version_id BIGINT,
  oauth_client_redirect VARCHAR(255)
);


CREATE TABLE application_versions
(
  id BIGINT NOT NULL,
  created_by VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL,
  modified_by VARCHAR(255) NOT NULL,
  modified_on TIMESTAMP NOT NULL,
  published_on TIMESTAMP,
  retired_on TIMESTAMP,
  status VARCHAR(255) NOT NULL,
  version VARCHAR(255) NOT NULL,
  app_id VARCHAR(255),
  app_org_id VARCHAR(255),
  oauth_client_id VARCHAR(255),
  oauth_client_secret VARCHAR(255),
  apikey VARCHAR(255) DEFAULT NULL,
  oauth_credential_id VARCHAR(255) DEFAULT NULL,
  jwt_key VARCHAR(255) DEFAULT NULL,
  jwt_secret VARCHAR(255) DEFAULT NULL,
  idp_client_id VARCHAR(255) DEFAULT NULL
);

CREATE TABLE applications
(
  id VARCHAR(255) NOT NULL,
  created_by VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL,
  description VARCHAR(512),
  context VARCHAR(255) DEFAULT '' NOT NULL,
  name VARCHAR(255) NOT NULL,
  organization_id VARCHAR(255) NOT NULL,
  logo OID,
  email VARCHAR(255) DEFAULT NULL
);

CREATE TABLE auditlog
(
  id BIGINT NOT NULL,
  created_on TIMESTAMP NOT NULL,
  data TEXT,
  entity_id VARCHAR(255),
  entity_type VARCHAR(255) NOT NULL,
  entity_version VARCHAR(255),
  organization_id VARCHAR(255) NOT NULL,
  what VARCHAR(255) NOT NULL,
  who VARCHAR(255) NOT NULL
);

CREATE TABLE black_ip_restriction
(
  netw_value VARCHAR(255) NOT NULL
);

CREATE TABLE brandings
(
  id VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE categories
(
  servicebean_id VARCHAR(255) NOT NULL,
  servicebean_organization_id VARCHAR(255) NOT NULL,
  category VARCHAR(255)
);

CREATE TABLE config
(
  id BIGINT NOT NULL,
  config_path VARCHAR(255) NOT NULL
);


CREATE TABLE contracts
(
  id BIGINT NOT NULL,
  created_by VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL,
  appv_id BIGINT,
  planv_id BIGINT,
  svcv_id BIGINT,
  terms_agreed BOOLEAN DEFAULT false
);

CREATE TABLE defaults
(
  id VARCHAR(255) NOT NULL,
  service_terms TEXT
);

CREATE TABLE endpoint_properties
(
  service_version_id BIGINT NOT NULL,
  value VARCHAR(255),
  name VARCHAR(255) NOT NULL
);

CREATE TABLE events
(
  id BIGINT NOT NULL,
  origin_id VARCHAR(255) NOT NULL,
  destination_id VARCHAR(255) NOT NULL,
  type VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL,
  body VARCHAR(4096)
);

CREATE TABLE followers
(
  servicebean_id VARCHAR(255) NOT NULL,
  servicebean_organization_id VARCHAR(255) NOT NULL,
  user_id VARCHAR(255) NOT NULL
);

CREATE TABLE gateways
(
  id VARCHAR(255) NOT NULL,
  configuration TEXT NOT NULL,
  endpoint VARCHAR(255) NOT NULL,
  created_by VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL,
  description VARCHAR(512),
  modified_by VARCHAR(255) NOT NULL,
  modified_on TIMESTAMP NOT NULL,
  name VARCHAR(255) NOT NULL,
  type VARCHAR(255) NOT NULL,
  jwt_exp_time INTEGER DEFAULT 7200,
  oauth_exp_time INTEGER DEFAULT 7200,
  jwt_pub_key TEXT DEFAULT '',
  jwt_pub_key_endpoint VARCHAR(255) DEFAULT '/keys/pub',
  jwt_priv_key TEXT
);

CREATE TABLE idps
(
  id VARCHAR(255) NOT NULL,
  server_url VARCHAR(255) NOT NULL,
  master_realm VARCHAR(255) NOT NULL,
  client_id VARCHAR(255) NOT NULL,
  encrypted_client_secret VARCHAR(255) NOT NULL,
  default_login_theme_id VARCHAR(255) DEFAULT NULL,
  default_realm VARCHAR(255) DEFAULT NULL,
  default_client VARCHAR(255) DEFAULT NULL,
  default_idp BOOLEAN DEFAULT FALSE
);

CREATE TABLE keystores
(
  kid VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  path VARCHAR(255) NOT NULL,
  encrypted_keystore_password VARCHAR(255) NOT NULL,
  encrypted_key_password VARCHAR(255) NOT NULL,
  private_key_alias VARCHAR(255) NOT NULL,
  priority BIGINT NOT NULL DEFAULT 150,
  default_keystore BOOLEAN DEFAULT FALSE
);

CREATE TABLE key_mapping
(
  from_spec_type VARCHAR(25) NOT NULL,
  to_spec_type VARCHAR(25) NOT NULL,
  from_spec_claim VARCHAR(255) NOT NULL,
  to_spec_claim VARCHAR(255)
);

CREATE TABLE mail_providers
(
  id BIGINT NOT NULL,
  host VARCHAR(255) NOT NULL,
  port BIGINT NOT NULL,
  auth BOOLEAN DEFAULT TRUE,
  mail_from VARCHAR(255) NOT NULL,
  username VARCHAR(255) NOT NULL,
  encrypted_password VARCHAR(255) NOT NULL,
  default_mail_provider BOOLEAN DEFAULT FALSE
);

CREATE TABLE mail_templates
(
  topic VARCHAR(255) NOT NULL,
  content TEXT,
  subject TEXT,
  created_on TIMESTAMP,
  updated_on TIMESTAMP
);

CREATE TABLE managed_application_keys
(
  managed_app_id BIGINT,
  api_key VARCHAR(255)
);

CREATE TABLE managed_applications
(
  id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  version VARCHAR(255) NOT NULL,
  gateway_id VARCHAR(255),
  app_id VARCHAR(255),
  type VARCHAR(255) NOT NULL,
  prefix VARCHAR(255) NOT NULL,
  activated BOOLEAN DEFAULT true,
  restricted BOOLEAN DEFAULT false,
  idp_client VARCHAR(255) DEFAULT NULL,
  redirect_uri VARCHAR(255) DEFAULT NULL
);

CREATE TABLE memberships
(
  id BIGINT NOT NULL,
  created_on TIMESTAMP,
  org_id VARCHAR(255),
  role_id VARCHAR(255),
  user_id VARCHAR(255)
);

CREATE TABLE oauth2_tokens
(
  id VARCHAR(255) NOT NULL,
  credential_id VARCHAR(255) NOT NULL,
  token_type VARCHAR(255) NOT NULL,
  access_token VARCHAR(255) NOT NULL,
  refresh_token VARCHAR(255) DEFAULT NULL,
  expires_in BIGINT NOT NULL,
  authenticated_userid VARCHAR(255) DEFAULT NULL,
  scope VARCHAR(4096) DEFAULT NULL,
  gateway_id VARCHAR(255) NOT NULL
);

CREATE TABLE oauth_scopes
(
  serviceversionbean_id BIGINT NOT NULL,
  oauth_scopes VARCHAR(255),
  oauth_scopes_desc VARCHAR(255)
);

CREATE TABLE operating_modes
(
  id VARCHAR(255) NOT NULL,
  enabled BOOLEAN DEFAULT false NOT NULL,
  message VARCHAR(255)
);

CREATE TABLE organizations
(
  id VARCHAR(255) NOT NULL,
  created_by VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL,
  description VARCHAR(512),
  modified_by VARCHAR(255) NOT NULL,
  modified_on TIMESTAMP NOT NULL,
  name VARCHAR(255) NOT NULL,
  friendly_name VARCHAR(255),
  private BOOLEAN DEFAULT true,
  context VARCHAR(255) DEFAULT 'pub' NOT NULL,
  mail_provider_id BIGINT NULL,
  keystore_kid VARCHAR(255) NULL
);

CREATE TABLE permissions
(
  role_id VARCHAR(255) NOT NULL,
  permissions INTEGER
);

CREATE TABLE plan_versions
(
  id BIGINT NOT NULL,
  created_by VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL,
  locked_on TIMESTAMP,
  modified_by VARCHAR(255) NOT NULL,
  modified_on TIMESTAMP NOT NULL,
  status VARCHAR(255) NOT NULL,
  version VARCHAR(255) NOT NULL,
  plan_id VARCHAR(255),
  plan_org_id VARCHAR(255)
);

CREATE TABLE plans
(
  id VARCHAR(255) NOT NULL,
  created_by VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL,
  description VARCHAR(512),
  name VARCHAR(255) NOT NULL,
  organization_id VARCHAR(255) NOT NULL
);

CREATE TABLE plugins
(
  id BIGINT NOT NULL,
  artifact_id VARCHAR(255) NOT NULL,
  classifier VARCHAR(255),
  created_by VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL,
  description VARCHAR(512),
  group_id VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  type VARCHAR(255),
  version VARCHAR(255) NOT NULL
);

CREATE TABLE policies
(
  id BIGINT NOT NULL,
  configuration TEXT,
  created_by VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL,
  entity_id VARCHAR(255) NOT NULL,
  entity_version VARCHAR(255) NOT NULL,
  modified_by VARCHAR(255) NOT NULL,
  modified_on TIMESTAMP NOT NULL,
  name VARCHAR(255) NOT NULL,
  order_index INTEGER NOT NULL,
  organization_id VARCHAR(255) NOT NULL,
  type VARCHAR(255) NOT NULL,
  definition_id VARCHAR(255) NOT NULL,
  kong_plugin_id VARCHAR(255),
  contract_id BIGINT,
  gateway_id VARCHAR(255),
  enabled BOOLEAN DEFAULT true
);

CREATE TABLE policydefs
(
  id VARCHAR(255) NOT NULL,
  description VARCHAR(512) NOT NULL,
  form TEXT DEFAULT NULL,
  form_type VARCHAR(255),
  icon VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  plugin_id BIGINT,
  scope_service BOOLEAN,
  scope_plan BOOLEAN,
  scope_auto BOOLEAN,
  form_override VARCHAR(4096) DEFAULT NULL,
  default_config VARCHAR(4096) DEFAULT NULL,
  logo TEXT DEFAULT NULL,
  marketplace_description VARCHAR(4096) DEFAULT NULL,
  popover_template VARCHAR(4096) DEFAULT NULL
);

CREATE TABLE roles
(
  id VARCHAR(255) NOT NULL,
  auto_grant BOOLEAN,
  created_by VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL,
  description VARCHAR(512),
  name VARCHAR(255)
);

CREATE TABLE service_basepaths
(
  servicebean_organization_id VARCHAR(255) NOT NULL,
  servicebean_id VARCHAR(255) NOT NULL,
  basepath VARCHAR (255) NOT NULL
);

CREATE TABLE service_brandings
(
  organization_id VARCHAR(255) NOT NULL,
  service_id VARCHAR(255) NOT NULL,
  branding_id VARCHAR(255) NOT NULL
);

CREATE TABLE service_defs
(
  id BIGINT NOT NULL,
  data OID,
  service_version_id BIGINT
);

create table service_hosts
(
  service_version_id BIGINT NOT NULL,
  hostname VARCHAR(255) NOT NULL
);

CREATE TABLE service_versions
(
  id BIGINT NOT NULL,
  created_by VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL,
  definition_type VARCHAR(255),
  endpoint VARCHAR(255),
  endpoint_type VARCHAR(255),
  modified_by VARCHAR(255) NOT NULL,
  modified_on TIMESTAMP NOT NULL,
  public_service BOOLEAN NOT NULL,
  published_on TIMESTAMP,
  retired_on TIMESTAMP,
  deprecated_on TIMESTAMP,
  status VARCHAR(255) NOT NULL,
  version VARCHAR(255),
  service_id VARCHAR(255),
  service_org_id VARCHAR(255),
  provision_key VARCHAR(255),
  onlinedoc VARCHAR(255),
  auto_accept_contracts BOOLEAN DEFAULT true,
  terms_agreement_required BOOLEAN DEFAULT false,
  readme TEXT,
  upstream_connect_timeout BIGINT DEFAULT 60000,
  upstream_send_timeout BIGINT DEFAULT 60000,
  upstream_read_timeout BIGINT DEFAULT 60000
);

CREATE TABLE services
(
  id VARCHAR(255) NOT NULL,
  created_by VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL,
  description VARCHAR(512),
  name VARCHAR(255) NOT NULL,
  basepath VARCHAR(255) NOT NULL,
  organization_id VARCHAR(255) NOT NULL,
  terms TEXT,
  logo OID,
  admin BOOLEAN DEFAULT false
);

CREATE TABLE support
(
  id BIGINT NOT NULL,
  organization_id VARCHAR(255) NOT NULL,
  service_id VARCHAR(255) NOT NULL,
  title VARCHAR(255) NOT NULL,
  status VARCHAR(255) NOT NULL,
  description TEXT,
  created_on TIMESTAMP NOT NULL,
  created_by VARCHAR(255) NOT NULL,
  total_comments INTEGER
);

CREATE TABLE support_comments
(
  id BIGINT NOT NULL,
  support_id BIGINT NOT NULL,
  comment TEXT,
  created_on TIMESTAMP NOT NULL,
  created_by VARCHAR(255) NOT NULL
);

CREATE TABLE svc_gateways
(
  service_version_id BIGINT NOT NULL,
  gateway_id VARCHAR(255) NOT NULL
);

CREATE TABLE svc_plans
(
  service_version_id BIGINT NOT NULL,
  plan_id VARCHAR(255) NOT NULL,
  version VARCHAR(255) NOT NULL
);

CREATE TABLE svc_visibility
(
  service_version_id BIGINT NOT NULL,
  code VARCHAR(255) NOT NULL,
  show BOOLEAN DEFAULT true
);

CREATE TABLE users
(
  username VARCHAR(255) NOT NULL,
  kong_username VARCHAR(255),
  email VARCHAR(255),
  full_name VARCHAR(255),
  joined_on TIMESTAMP,
  admin BOOLEAN DEFAULT false,
  company VARCHAR(255),
  location VARCHAR(255),
  website VARCHAR(255),
  bio TEXT,
  pic OID,
  jwt_key VARCHAR(255) DEFAULT NULL,
  jwt_secret VARCHAR(255) DEFAULT NULL
);

CREATE TABLE white_ip_restriction
(
  netw_value VARCHAR(255) NOT NULL
);

-- PRIMARY KEYS

ALTER TABLE applications ADD PRIMARY KEY (id, organization_id);

ALTER TABLE announcements ADD PRIMARY KEY (id);

ALTER TABLE application_versions ADD PRIMARY KEY (id);

ALTER TABLE auditlog ADD PRIMARY KEY (id);

ALTER TABLE black_ip_restriction ADD PRIMARY KEY (netw_value);

ALTER TABLE brandings ADD PRIMARY KEY (id);

ALTER TABLE config ADD PRIMARY KEY (id);

ALTER TABLE contracts ADD PRIMARY KEY (id);

ALTER TABLE defaults ADD PRIMARY KEY (id);

ALTER TABLE endpoint_properties ADD PRIMARY KEY (service_version_id, name);

ALTER TABLE events ADD PRIMARY KEY (id);

ALTER TABLE followers ADD PRIMARY KEY (servicebean_id, servicebean_organization_id, user_id);

ALTER TABLE gateways ADD PRIMARY KEY (id);

ALTER TABLE idps ADD PRIMARY KEY (id);

ALTER TABLE keystores ADD PRIMARY KEY (kid);

ALTER TABLE key_mapping ADD PRIMARY KEY (from_spec_type, to_spec_type, from_spec_claim);

ALTER TABLE mail_providers ADD PRIMARY KEY (id);

ALTER TABLE mail_templates ADD PRIMARY KEY (topic);

ALTER TABLE managed_applications ADD PRIMARY KEY (id);

ALTER TABLE memberships ADD PRIMARY KEY (id);

ALTER TABLE operating_modes ADD PRIMARY KEY (id);

ALTER TABLE organizations ADD PRIMARY KEY (id);

ALTER TABLE plan_versions ADD PRIMARY KEY (id);

ALTER TABLE plans ADD PRIMARY KEY (id, organization_id);

ALTER TABLE plugins ADD PRIMARY KEY (id);

ALTER TABLE policies ADD PRIMARY KEY (id);

ALTER TABLE policydefs ADD PRIMARY KEY (id);

ALTER TABLE roles ADD PRIMARY KEY (id);

ALTER TABLE service_defs ADD PRIMARY KEY (id);

ALTER TABLE service_versions ADD PRIMARY KEY (id);

ALTER TABLE services ADD PRIMARY KEY (id, organization_id);

ALTER TABLE support ADD PRIMARY KEY (id);

ALTER TABLE support_comments ADD PRIMARY KEY (id);

ALTER TABLE svc_gateways ADD PRIMARY KEY (service_version_id, gateway_id);

ALTER TABLE svc_plans ADD PRIMARY KEY (service_version_id, plan_id, version);

ALTER TABLE svc_visibility ADD PRIMARY KEY (service_version_id, code);

ALTER TABLE users ADD PRIMARY KEY (username);

ALTER TABLE white_ip_restriction ADD PRIMARY KEY (netw_value);

-- INDEXES

CREATE INDEX idx_announcements_2 ON announcements (organization_id, service_id);

CREATE INDEX idx_announcements_1 ON announcements (created_by);

CREATE INDEX idx_app_oauth_redirect_uris_1 ON app_oauth_redirect_uris (application_version_id);

CREATE INDEX idx_fk_applications_1 ON applications (organization_id);

CREATE INDEX idx_auditlog_1 ON auditlog (who);

CREATE INDEX idx_auditlog_2 ON auditlog (organization_id, entity_id, entity_version, entity_type);

CREATE INDEX idx_brandings_1 ON brandings (id);

CREATE INDEX idx_fk_categories_1 ON categories (servicebean_id, servicebean_organization_id);

CREATE INDEX idx_fk_contracts_1 ON contracts (appv_id);

CREATE INDEX idx_fk_contracts_2 ON contracts (planv_id);

CREATE INDEX idx_fk_contracts_3 ON contracts (svcv_id);

CREATE INDEX idx_fk_followers_1 ON followers (servicebean_id, servicebean_organization_id);

CREATE INDEX idx_managed_app_keys_1 ON managed_application_keys (managed_app_id);

CREATE INDEX idx_memberships_1 ON memberships (user_id);

CREATE INDEX idx_oauth2_tokens_1 ON oauth2_tokens (credential_id);

CREATE INDEX idx_fk_oauth_scopes_1 ON oauth_scopes (serviceversionbean_id);

CREATE INDEX idx_organizations_1 ON organizations (name);

CREATE INDEX idx_fk_permissions_1 ON permissions (role_id);

CREATE INDEX idx_fk_plans_1 ON plans (organization_id);

CREATE INDEX idx_policies_1 ON policies (organization_id, entity_id, entity_version);

CREATE INDEX idx_policies_2 ON policies (order_index);

CREATE INDEX idx_fk_policies_1 ON policies (definition_id);

CREATE INDEX idx_service_basepaths_1 ON service_basepaths (servicebean_organization_id, servicebean_id);

CREATE INDEX idx_service_brandings_1 ON service_brandings (organization_id, service_id);

CREATE INDEX idx_service_brandings_2 ON service_brandings (branding_id);

CREATE INDEX idx_service_hosts_1 ON service_hosts (service_version_id);

CREATE INDEX idx_services_1 ON services (name);

CREATE INDEX idx_fk_services_1 ON services (organization_id);

CREATE INDEX idx_support_1 ON support (organization_id, service_id);

CREATE INDEX idx_users_1 ON users (full_name);

CREATE INDEX idx_users_2 ON users (email);

-- UNIQUE INDEXES

ALTER TABLE app_oauth_redirect_uris ADD CONSTRAINT uk_app_oauth_redirect_uris UNIQUE (application_version_id, oauth_client_redirect);

ALTER TABLE application_versions ADD CONSTRAINT uk_app_versions_1 UNIQUE (app_id, app_org_id, version);

ALTER TABLE brandings ADD CONSTRAINT uk_brandings_1 UNIQUE (name);

ALTER TABLE contracts ADD CONSTRAINT uk_contracts_1 UNIQUE (appv_id, svcv_id, planv_id);

ALTER TABLE events ADD CONSTRAINT uk_events_1 UNIQUE (origin_id, destination_id, type);

ALTER TABLE managed_application_keys ADD CONSTRAINT uk_managed_app_keys_1 UNIQUE (managed_app_id, api_key);

ALTER TABLE managed_applications ADD CONSTRAINT uk_managedapp_1 UNIQUE (prefix);

ALTER TABLE memberships ADD CONSTRAINT uk_memberships_1 UNIQUE (user_id, role_id, org_id);

ALTER TABLE plan_versions ADD CONSTRAINT uk_plan_versions_1 UNIQUE (plan_id, plan_org_id, version);

ALTER TABLE plugins ADD CONSTRAINT uk_plugins_1 UNIQUE (group_id, artifact_id);

ALTER TABLE service_basepaths ADD CONSTRAINT uk_service_basepaths_1 UNIQUE (servicebean_organization_id, servicebean_id, basepath);

ALTER TABLE service_brandings ADD CONSTRAINT uk_service_brandings_1 UNIQUE (service_id, branding_id);

ALTER TABLE service_defs ADD CONSTRAINT uk_service_defs_1 UNIQUE (service_version_id);

ALTER TABLE service_hosts ADD CONSTRAINT uk_service_hosts_1 UNIQUE (service_version_id, hostname);

ALTER TABLE service_versions ADD CONSTRAINT uk_service_versions_1 UNIQUE (service_id, service_org_id, version);

CREATE UNIQUE INDEX uk_idps_1 ON idps (default_idp) WHERE default_idp = true;

CREATE UNIQUE INDEX uk_keystores_1 ON keystores (default_keystore) WHERE default_keystore = true;

CREATE UNIQUE INDEX uk_mail_providers_1 ON mail_providers (default_mail_provider) WHERE default_mail_provider = true;

-- FOREIGN KEYS

ALTER TABLE application_versions ADD CONSTRAINT fk_application_versions_1 FOREIGN KEY (app_id, app_org_id) REFERENCES applications (id, organization_id);

ALTER TABLE app_oauth_redirect_uris ADD CONSTRAINT fk_app_oauth_redirect_uris_1 FOREIGN KEY (application_version_id) REFERENCES application_versions (id);

ALTER TABLE applications ADD CONSTRAINT fk_applications_1 FOREIGN KEY (organization_id) REFERENCES organizations (id);

ALTER TABLE categories ADD CONSTRAINT fk_categories_1 FOREIGN KEY (servicebean_id, servicebean_organization_id) REFERENCES services (id, organization_id);

ALTER TABLE contracts ADD CONSTRAINT fk_contracts_1 FOREIGN KEY (appv_id) REFERENCES application_versions (id);

ALTER TABLE contracts ADD CONSTRAINT fk_contracts_2 FOREIGN KEY (planv_id) REFERENCES plan_versions (id);

ALTER TABLE contracts ADD CONSTRAINT fk_contracts_3 FOREIGN KEY (svcv_id) REFERENCES service_versions (id);

ALTER TABLE endpoint_properties ADD CONSTRAINT fk_endpoint_properties_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE followers ADD CONSTRAINT fk_followers_1 FOREIGN KEY (servicebean_id, servicebean_organization_id) REFERENCES services (id, organization_id);

ALTER TABLE managed_application_keys ADD CONSTRAINT fk_managed_app_keys_1 FOREIGN KEY (managed_app_id) REFERENCES managed_applications (id);

ALTER TABLE managed_applications ADD CONSTRAINT fk_managed_applications_1 FOREIGN KEY (gateway_id) REFERENCES gateways (id);

ALTER TABLE oauth_scopes ADD CONSTRAINT fk_oauth_scopes_1 FOREIGN KEY (serviceversionbean_id) REFERENCES service_versions (id);

ALTER TABLE organizations ADD CONSTRAINT fk_organization_1 FOREIGN KEY (mail_provider_id) REFERENCES mail_providers (id);

ALTER TABLE organizations ADD CONSTRAINT fk_organizations_2 FOREIGN KEY (keystore_id) REFERENCES keystores (id);

ALTER TABLE permissions ADD CONSTRAINT fk_permissions_1 FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE plan_versions ADD CONSTRAINT fk_plan_versions_1 FOREIGN KEY (plan_id, plan_org_id) REFERENCES plans (id, organization_id);

ALTER TABLE plans ADD CONSTRAINT fk_plans_1 FOREIGN KEY (organization_id) REFERENCES organizations (id);

ALTER TABLE policies ADD CONSTRAINT fk_policies_1 FOREIGN KEY (definition_id) REFERENCES policydefs (id);

ALTER TABLE service_basepaths ADD CONSTRAINT fk_service_basepaths_1 FOREIGN KEY (servicebean_organization_id, servicebean_id) references services (organization_id, id);

ALTER TABLE service_brandings ADD CONSTRAINT fk_service_brandings_1 FOREIGN KEY (service_id, organization_id) REFERENCES services (id, organization_id);

ALTER TABLE service_brandings ADD CONSTRAINT fk_service_brandings_2 FOREIGN KEY (branding_id) REFERENCES brandings (id);

ALTER TABLE service_defs ADD CONSTRAINT fk_service_defs_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE service_hosts ADD CONSTRAINT fk_service_hosts_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE service_versions ADD CONSTRAINT fk_service_versions_1 FOREIGN KEY (service_id, service_org_id) REFERENCES services (id, organization_id);

ALTER TABLE services ADD CONSTRAINT fk_services_1 FOREIGN KEY (organization_id) REFERENCES organizations (id);

ALTER TABLE svc_gateways ADD CONSTRAINT fk_svc_gateways_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE svc_plans ADD CONSTRAINT fk_scv_plans_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE svc_visibility ADD CONSTRAINT fk_svc_visibility_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

-- Sequences

CREATE SEQUENCE hibernate_sequence;