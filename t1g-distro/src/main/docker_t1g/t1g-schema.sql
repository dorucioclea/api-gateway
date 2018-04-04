CREATE SEQUENCE hibernate_sequence START WITH 999;

-- TABLES
CREATE TABLE announcements
(
  id              BIGINT       NOT NULL
    CONSTRAINT announcements_pkey
    PRIMARY KEY,
  organization_id VARCHAR(255) NOT NULL,
  service_id      VARCHAR(255) NOT NULL,
  title           VARCHAR(255) NOT NULL,
  status          VARCHAR(255),
  description     TEXT,
  created_on      TIMESTAMP    NOT NULL,
  created_by      VARCHAR(255) NOT NULL
);

CREATE INDEX idx_announcements_2
  ON announcements (organization_id, service_id);

CREATE INDEX idx_announcements_1
  ON announcements (created_by);

CREATE TABLE app_oauth_redirect_uris
(
  application_version_id BIGINT,
  oauth_client_redirect  VARCHAR(255),
  CONSTRAINT uk_app_oauth_redirect_uris
  UNIQUE (application_version_id, oauth_client_redirect)
);

CREATE INDEX idx_app_oauth_redirect_uris_1
  ON app_oauth_redirect_uris (application_version_id);

CREATE TABLE application_versions
(
  id                  BIGINT       NOT NULL
    CONSTRAINT application_versions_pkey
    PRIMARY KEY,
  created_by          VARCHAR(255) NOT NULL,
  created_on          TIMESTAMP    NOT NULL,
  modified_by         VARCHAR(255) NOT NULL,
  modified_on         TIMESTAMP    NOT NULL,
  published_on        TIMESTAMP,
  retired_on          TIMESTAMP,
  status              VARCHAR(255) NOT NULL,
  version             VARCHAR(255) NOT NULL,
  app_id              VARCHAR(255),
  app_org_id          VARCHAR(255),
  oauth_client_secret VARCHAR(255),
  apikey              VARCHAR(255) DEFAULT NULL ::CHARACTER VARYING,
  oauth_credential_id VARCHAR(255) DEFAULT NULL ::CHARACTER VARYING,
  jwt_key             VARCHAR(255) DEFAULT NULL ::CHARACTER VARYING,
  jwt_secret          VARCHAR(255) DEFAULT NULL ::CHARACTER VARYING,
  CONSTRAINT uk_app_versions_1
  UNIQUE (app_id, app_org_id, version)
);

ALTER TABLE app_oauth_redirect_uris
  ADD CONSTRAINT fk_app_oauth_redirect_uris_1
FOREIGN KEY (application_version_id) REFERENCES application_versions
ON UPDATE CASCADE;

CREATE TABLE applications
(
  id              VARCHAR(255) NOT NULL,
  created_by      VARCHAR(255) NOT NULL,
  created_on      TIMESTAMP    NOT NULL,
  description     VARCHAR(512),
  context         VARCHAR(255) DEFAULT '' ::CHARACTER VARYING NOT NULL,
  name            VARCHAR(255) NOT NULL,
  organization_id VARCHAR(255) NOT NULL,
  logo            oid,
  email           VARCHAR(255) DEFAULT NULL ::CHARACTER VARYING,
  CONSTRAINT applications_pkey
  PRIMARY KEY (id, organization_id)
);

CREATE INDEX idx_fk_applications_1
  ON applications (organization_id);

ALTER TABLE application_versions
  ADD CONSTRAINT fk_8epnoby31bt7xakegakigpikp
FOREIGN KEY (app_id, app_org_id) REFERENCES applications
ON UPDATE CASCADE;

ALTER TABLE application_versions
  ADD CONSTRAINT fk_application_versions_1
FOREIGN KEY (app_id, app_org_id) REFERENCES applications
ON UPDATE CASCADE;

CREATE TABLE auditlog
(
  id              BIGINT       NOT NULL
    CONSTRAINT auditlog_pkey
    PRIMARY KEY,
  created_on      TIMESTAMP    NOT NULL,
  data            TEXT,
  entity_id       VARCHAR(255),
  entity_type     VARCHAR(255) NOT NULL,
  entity_version  VARCHAR(255),
  organization_id VARCHAR(255) NOT NULL,
  what            VARCHAR(255) NOT NULL,
  who             VARCHAR(255) NOT NULL
);

CREATE INDEX idx_auditlog_2
  ON auditlog (organization_id, entity_id, entity_version, entity_type);

CREATE INDEX idx_auditlog_1
  ON auditlog (who);

CREATE TABLE black_ip_restriction
(
  netw_value VARCHAR(255) NOT NULL
    CONSTRAINT black_ip_restriction_pkey
    PRIMARY KEY
);

CREATE TABLE brandings
(
  id   VARCHAR(255) NOT NULL
    CONSTRAINT brandings_pkey
    PRIMARY KEY,
  name VARCHAR(255) NOT NULL
    CONSTRAINT uk_brandings_1
    UNIQUE
);

CREATE INDEX idx_brandings_1
  ON brandings (id);

CREATE TABLE categories
(
  servicebean_id              VARCHAR(255) NOT NULL,
  servicebean_organization_id VARCHAR(255) NOT NULL,
  category                    VARCHAR(255)
);

CREATE INDEX fk_huasdtal54l0isoauy6mrtmpx
  ON categories (servicebean_id, servicebean_organization_id);

CREATE TABLE config
(
  id          BIGINT       NOT NULL
    CONSTRAINT config_pkey
    PRIMARY KEY,
  config_path VARCHAR(255) NOT NULL
);

CREATE TABLE contracts
(
  id           BIGINT       NOT NULL
    CONSTRAINT contracts_pkey
    PRIMARY KEY,
  created_by   VARCHAR(255) NOT NULL,
  created_on   TIMESTAMP    NOT NULL,
  appv_id      BIGINT
    CONSTRAINT fk_6h06sgs4dudh1wehmk0us973g
    REFERENCES application_versions
    ON UPDATE CASCADE
    CONSTRAINT fk_contracts_1
    REFERENCES application_versions
    ON UPDATE CASCADE,
  planv_id     BIGINT,
  svcv_id      BIGINT,
  terms_agreed BOOLEAN DEFAULT FALSE,
  CONSTRAINT uk_contracts_1
  UNIQUE (appv_id, svcv_id, planv_id)
);

CREATE INDEX idx_fk_contracts_a
  ON contracts (appv_id);

CREATE INDEX idx_fk_contracts_p
  ON contracts (planv_id);

CREATE INDEX idx_fk_contracts_s
  ON contracts (svcv_id);

CREATE TABLE defaults
(
  id            VARCHAR(255) NOT NULL
    CONSTRAINT defaults_pkey
    PRIMARY KEY,
  service_terms TEXT
);

CREATE TABLE endpoint_properties
(
  service_version_id BIGINT       NOT NULL,
  value              VARCHAR(255),
  name               VARCHAR(255) NOT NULL,
  CONSTRAINT endpoint_properties_pkey
  PRIMARY KEY (service_version_id, name)
);

CREATE TABLE events
(
  id             BIGINT       NOT NULL
    CONSTRAINT events_pkey
    PRIMARY KEY,
  origin_id      VARCHAR(255) NOT NULL,
  destination_id VARCHAR(255) NOT NULL,
  type           VARCHAR(255) NOT NULL,
  created_on     TIMESTAMP    NOT NULL,
  body           VARCHAR(4096),
  CONSTRAINT uk_events_1
  UNIQUE (origin_id, destination_id, type)
);

CREATE TABLE followers
(
  servicebean_id              VARCHAR(255) NOT NULL,
  servicebean_organization_id VARCHAR(255) NOT NULL,
  user_id                     VARCHAR(255) NOT NULL,
  CONSTRAINT followers_pkey
  PRIMARY KEY (servicebean_id, servicebean_organization_id, user_id)
);

CREATE INDEX idx_fk_followers_a
  ON followers (servicebean_id, servicebean_organization_id);

CREATE TABLE gateways
(
  id                   VARCHAR(255) NOT NULL
    CONSTRAINT gateways_pkey
    PRIMARY KEY,
  configuration        TEXT         NOT NULL,
  endpoint             VARCHAR(255) NOT NULL,
  created_by           VARCHAR(255) NOT NULL,
  created_on           TIMESTAMP    NOT NULL,
  description          VARCHAR(512),
  modified_by          VARCHAR(255) NOT NULL,
  modified_on          TIMESTAMP    NOT NULL,
  name                 VARCHAR(255) NOT NULL,
  type                 VARCHAR(255) NOT NULL,
  jwt_exp_time         INTEGER      DEFAULT 7200,
  oauth_exp_time       INTEGER      DEFAULT 7200,
  jwt_pub_key          TEXT         DEFAULT '' ::TEXT,
  jwt_pub_key_endpoint VARCHAR(255) DEFAULT '' ::CHARACTER VARYING,
  jwt_priv_key         TEXT
);

CREATE TABLE mail_templates
(
  topic      VARCHAR(255) NOT NULL
    CONSTRAINT pksb_mail_templates
    PRIMARY KEY,
  content    TEXT,
  subject    TEXT,
  created_on TIMESTAMP,
  updated_on TIMESTAMP
);

CREATE TABLE managed_application_keys
(
  managed_app_id BIGINT,
  api_key        VARCHAR(255),
  CONSTRAINT uk_managed_app_keys_1
  UNIQUE (managed_app_id, api_key)
);

CREATE INDEX idx_managed_app_keys_1
  ON managed_application_keys (managed_app_id);

CREATE TABLE managed_applications
(
  id         BIGINT       NOT NULL
    CONSTRAINT managed_applications_pkey
    PRIMARY KEY,
  name       VARCHAR(255) NOT NULL,
  version    VARCHAR(255) NOT NULL,
  app_id     VARCHAR(255),
  type       VARCHAR(255) NOT NULL,
  prefix     VARCHAR(255) NOT NULL
    CONSTRAINT uk_managedapp_1
    UNIQUE,
  activated  BOOLEAN DEFAULT TRUE,
  restricted BOOLEAN DEFAULT FALSE
);

ALTER TABLE managed_application_keys
  ADD CONSTRAINT fk_managed_app_keys_1
FOREIGN KEY (managed_app_id) REFERENCES managed_applications;

CREATE TABLE memberships
(
  id         BIGINT NOT NULL
    CONSTRAINT memberships_pkey
    PRIMARY KEY,
  created_on TIMESTAMP,
  org_id     VARCHAR(255),
  role_id    VARCHAR(255),
  user_id    VARCHAR(255),
  CONSTRAINT uk_memberships_1
  UNIQUE (user_id, role_id, org_id)
);

CREATE INDEX idx_memberships_1
  ON memberships (user_id);

CREATE TABLE oauth2_tokens
(
  id                   VARCHAR(255) NOT NULL
    CONSTRAINT oauth2_tokens_pkey
    PRIMARY KEY,
  credential_id        VARCHAR(255) NOT NULL,
  token_type           VARCHAR(255) NOT NULL,
  access_token         VARCHAR(255) NOT NULL,
  refresh_token        VARCHAR(255)  DEFAULT NULL ::CHARACTER VARYING,
  expires_in           BIGINT       NOT NULL,
  authenticated_userid VARCHAR(255)  DEFAULT NULL ::CHARACTER VARYING,
  scope                VARCHAR(4096) DEFAULT NULL ::CHARACTER VARYING,
  gateway_id           VARCHAR(255) NOT NULL
);

CREATE INDEX idx_oauth2_tokens_1
  ON oauth2_tokens (credential_id);

CREATE TABLE oauth_scopes
(
  serviceversionbean_id BIGINT NOT NULL,
  oauth_scopes          VARCHAR(255),
  oauth_scopes_desc     VARCHAR(255)
);

CREATE INDEX fk_uhasdtal55l0isoauy6mrtmpx
  ON oauth_scopes (serviceversionbean_id);

CREATE TABLE operating_modes
(
  id      VARCHAR(255)          NOT NULL,
  enabled BOOLEAN DEFAULT FALSE NOT NULL,
  message VARCHAR(255)
);

CREATE TABLE organizations
(
  id            VARCHAR(255) NOT NULL
    CONSTRAINT organizations_pkey
    PRIMARY KEY,
  created_by    VARCHAR(255) NOT NULL,
  created_on    TIMESTAMP    NOT NULL,
  description   VARCHAR(512),
  modified_by   VARCHAR(255) NOT NULL,
  modified_on   TIMESTAMP    NOT NULL,
  name          VARCHAR(255) NOT NULL,
  friendly_name VARCHAR(255),
  private       BOOLEAN      DEFAULT TRUE,
  context       VARCHAR(255) DEFAULT 'pub' ::CHARACTER VARYING NOT NULL
);

CREATE INDEX idx_organizations_1
  ON organizations (name);

ALTER TABLE applications
  ADD CONSTRAINT fk_applications_1
FOREIGN KEY (organization_id) REFERENCES organizations
ON UPDATE CASCADE;

ALTER TABLE applications
  ADD CONSTRAINT fk_jenpu34rtuncsgvtw0sfo8qq9
FOREIGN KEY (organization_id) REFERENCES organizations
ON UPDATE CASCADE;

CREATE TABLE permissions
(
  role_id     VARCHAR(255) NOT NULL,
  permissions INTEGER
);

CREATE INDEX idx_fk_permissions_1
  ON permissions (role_id);

CREATE TABLE plan_versions
(
  id          BIGINT       NOT NULL
    CONSTRAINT plan_versions_pkey
    PRIMARY KEY,
  created_by  VARCHAR(255) NOT NULL,
  created_on  TIMESTAMP    NOT NULL,
  locked_on   TIMESTAMP,
  modified_by VARCHAR(255) NOT NULL,
  modified_on TIMESTAMP    NOT NULL,
  status      VARCHAR(255) NOT NULL,
  version     VARCHAR(255) NOT NULL,
  plan_id     VARCHAR(255),
  plan_org_id VARCHAR(255),
  CONSTRAINT uk_plan_versions_1
  UNIQUE (plan_id, plan_org_id, version)
);

ALTER TABLE contracts
  ADD CONSTRAINT fk_contracts_2
FOREIGN KEY (planv_id) REFERENCES plan_versions
ON UPDATE CASCADE;

ALTER TABLE contracts
  ADD CONSTRAINT fk_nyw8xu6m8cx4rwwbtrxbjneui
FOREIGN KEY (planv_id) REFERENCES plan_versions
ON UPDATE CASCADE;

CREATE TABLE plans
(
  id              VARCHAR(255) NOT NULL,
  created_by      VARCHAR(255) NOT NULL,
  created_on      TIMESTAMP    NOT NULL,
  description     VARCHAR(512),
  name            VARCHAR(255) NOT NULL,
  organization_id VARCHAR(255) NOT NULL
    CONSTRAINT fk_lwhc7xrdbsun1ak2uvfu0prj8
    REFERENCES organizations
    ON UPDATE CASCADE
    CONSTRAINT fk_plans_1
    REFERENCES organizations
    ON UPDATE CASCADE,
  CONSTRAINT plans_pkey
  PRIMARY KEY (id, organization_id)
);

CREATE INDEX idx_fk_plans_1
  ON plans (organization_id);

ALTER TABLE plan_versions
  ADD CONSTRAINT fk_plan_versions_1
FOREIGN KEY (plan_id, plan_org_id) REFERENCES plans
ON UPDATE CASCADE;

ALTER TABLE plan_versions
  ADD CONSTRAINT fk_tonylvm2ypnq3efxqr1g0m9fs
FOREIGN KEY (plan_id, plan_org_id) REFERENCES plans
ON UPDATE CASCADE;

CREATE TABLE policies
(
  id              BIGINT       NOT NULL
    CONSTRAINT policies_pkey
    PRIMARY KEY,
  configuration   TEXT,
  created_by      VARCHAR(255) NOT NULL,
  created_on      TIMESTAMP    NOT NULL,
  entity_id       VARCHAR(255) NOT NULL,
  entity_version  VARCHAR(255) NOT NULL,
  modified_by     VARCHAR(255) NOT NULL,
  modified_on     TIMESTAMP    NOT NULL,
  name            VARCHAR(255) NOT NULL,
  order_index     INTEGER      NOT NULL,
  organization_id VARCHAR(255) NOT NULL,
  type            VARCHAR(255) NOT NULL,
  definition_id   VARCHAR(255) NOT NULL,
  kong_plugin_id  VARCHAR(255),
  contract_id     BIGINT,
  gateway_id      VARCHAR(255),
  enabled         BOOLEAN DEFAULT TRUE
);

CREATE INDEX idx_policies_2
  ON policies (order_index);

CREATE INDEX idx_policies_1
  ON policies (organization_id, entity_id, entity_version);

CREATE INDEX idx_fk_policies_1
  ON policies (definition_id);

CREATE TABLE policydefs
(
  id                      VARCHAR(255) NOT NULL
    CONSTRAINT policydefs_pkey
    PRIMARY KEY,
  description             VARCHAR(512) NOT NULL,
  form                    TEXT,
  form_type               VARCHAR(255),
  icon                    VARCHAR(255) NOT NULL,
  name                    VARCHAR(255) NOT NULL,
  plugin_id               BIGINT,
  scope_service           BOOLEAN,
  scope_plan              BOOLEAN,
  scope_auto              BOOLEAN,
  form_override           VARCHAR(4096) DEFAULT NULL ::CHARACTER VARYING,
  default_config          VARCHAR(4096) DEFAULT NULL ::CHARACTER VARYING,
  logo                    TEXT,
  marketplace_description VARCHAR(4096) DEFAULT NULL ::CHARACTER VARYING,
  popover_template        VARCHAR(4096) DEFAULT NULL ::CHARACTER VARYING
);

ALTER TABLE policies
  ADD CONSTRAINT fk_l4q6we1bos1yl9unmogei6aja
FOREIGN KEY (definition_id) REFERENCES policydefs
ON UPDATE CASCADE;

ALTER TABLE policies
  ADD CONSTRAINT fk_policies_1
FOREIGN KEY (definition_id) REFERENCES policydefs
ON UPDATE CASCADE;

CREATE TABLE roles
(
  id          VARCHAR(255) NOT NULL
    CONSTRAINT roles_pkey
    PRIMARY KEY,
  auto_grant  BOOLEAN,
  created_by  VARCHAR(255) NOT NULL,
  created_on  TIMESTAMP    NOT NULL,
  description VARCHAR(512),
  name        VARCHAR(255)
);

ALTER TABLE permissions
  ADD CONSTRAINT fk_permissions_1
FOREIGN KEY (role_id) REFERENCES roles
ON UPDATE CASCADE;

ALTER TABLE permissions
  ADD CONSTRAINT fk_sq51ihfrapwdr98uufenhcocg
FOREIGN KEY (role_id) REFERENCES roles
ON UPDATE CASCADE;

CREATE TABLE service_brandings
(
  organization_id VARCHAR(255) NOT NULL,
  service_id      VARCHAR(255) NOT NULL,
  branding_id     VARCHAR(255) NOT NULL
    CONSTRAINT fk_service_brandings_2
    REFERENCES brandings
    ON UPDATE CASCADE,
  CONSTRAINT uk_service_brandings_1
  UNIQUE (service_id, branding_id)
);

CREATE INDEX idx_service_brandings_1
  ON service_brandings (organization_id, service_id);

CREATE INDEX idx_service_brandings_2
  ON service_brandings (branding_id);

CREATE TABLE service_defs
(
  id                 BIGINT NOT NULL
    CONSTRAINT service_defs_pkey
    PRIMARY KEY,
  data               oid,
  service_version_id BIGINT
    CONSTRAINT uk_service_defs_1
    UNIQUE
);

CREATE TABLE service_versions
(
  id                       BIGINT       NOT NULL
    CONSTRAINT service_versions_pkey
    PRIMARY KEY,
  created_by               VARCHAR(255) NOT NULL,
  created_on               TIMESTAMP    NOT NULL,
  definition_type          VARCHAR(255),
  endpoint                 VARCHAR(255),
  endpoint_type            VARCHAR(255),
  modified_by              VARCHAR(255) NOT NULL,
  modified_on              TIMESTAMP    NOT NULL,
  public_service           BOOLEAN      NOT NULL,
  published_on             TIMESTAMP,
  retired_on               TIMESTAMP,
  deprecated_on            TIMESTAMP,
  status                   VARCHAR(255) NOT NULL,
  version                  VARCHAR(255),
  service_id               VARCHAR(255),
  service_org_id           VARCHAR(255),
  provision_key            VARCHAR(255),
  onlinedoc                VARCHAR(255),
  auto_accept_contracts    BOOLEAN      DEFAULT TRUE,
  terms_agreement_required BOOLEAN      DEFAULT FALSE,
  readme                   TEXT,
  upstream_connect_timeout BIGINT       DEFAULT 60000,
  upstream_send_timeout    BIGINT       DEFAULT 60000,
  upstream_read_timeout    BIGINT       DEFAULT 60000,
  custom_load_balancing    BOOLEAN      DEFAULT FALSE,
  upstream_path            VARCHAR(255) DEFAULT NULL ::CHARACTER VARYING,
  upstream_scheme          VARCHAR(255) DEFAULT NULL ::CHARACTER VARYING,
  CONSTRAINT uk_service_versions_1
  UNIQUE (service_id, service_org_id, version)
);

ALTER TABLE contracts
  ADD CONSTRAINT fk_8o6t1f3kg96rxy5uv51f6k9fy
FOREIGN KEY (svcv_id) REFERENCES service_versions
ON UPDATE CASCADE;

ALTER TABLE endpoint_properties
  ADD CONSTRAINT fk_endpoint_properties_1
FOREIGN KEY (service_version_id) REFERENCES service_versions
ON UPDATE CASCADE;

ALTER TABLE endpoint_properties
  ADD CONSTRAINT fk_gn0ydqur10sxuvpyw2jvv4xxb
FOREIGN KEY (service_version_id) REFERENCES service_versions
ON UPDATE CASCADE;

ALTER TABLE oauth_scopes
  ADD CONSTRAINT oauth_scopes_serviceversionbean_id_fkey
FOREIGN KEY (serviceversionbean_id) REFERENCES service_versions;

ALTER TABLE service_defs
  ADD CONSTRAINT fk_81fuw1n8afmvpw4buk7l4tyxk
FOREIGN KEY (service_version_id) REFERENCES service_versions
ON UPDATE CASCADE;

ALTER TABLE service_defs
  ADD CONSTRAINT fk_service_defs_1
FOREIGN KEY (service_version_id) REFERENCES service_versions
ON UPDATE CASCADE;

CREATE TABLE services
(
  id              VARCHAR(255) NOT NULL,
  created_by      VARCHAR(255) NOT NULL,
  created_on      TIMESTAMP    NOT NULL,
  description     VARCHAR(512),
  name            VARCHAR(255) NOT NULL,
  organization_id VARCHAR(255) NOT NULL
    CONSTRAINT fk_31hj3xmhp1wedxjh5bklnlg15
    REFERENCES organizations
    ON UPDATE CASCADE
    CONSTRAINT fk_services_1
    REFERENCES organizations
    ON UPDATE CASCADE,
  terms           TEXT,
  logo            oid,
  admin           BOOLEAN DEFAULT FALSE,
  CONSTRAINT services_pkey
  PRIMARY KEY (id, organization_id)
);

CREATE INDEX idx_services_1
  ON services (name);

CREATE INDEX idx_fk_services_1
  ON services (organization_id);

ALTER TABLE categories
  ADD CONSTRAINT categories_servicebean_id_fkey
FOREIGN KEY (servicebean_id, servicebean_organization_id) REFERENCES services;

ALTER TABLE followers
  ADD CONSTRAINT fk_29hj3xmhp1wedxjh1bklnlg15
FOREIGN KEY (servicebean_id, servicebean_organization_id) REFERENCES services
ON UPDATE CASCADE;

ALTER TABLE followers
  ADD CONSTRAINT fk_followers_1
FOREIGN KEY (servicebean_id, servicebean_organization_id) REFERENCES services
ON UPDATE CASCADE;

ALTER TABLE service_brandings
  ADD CONSTRAINT fk_service_brandings_1
FOREIGN KEY (service_id, organization_id) REFERENCES services
ON UPDATE CASCADE;

ALTER TABLE service_versions
  ADD CONSTRAINT fk_92erjg9k1lni97gd87nt6tq37
FOREIGN KEY (service_id, service_org_id) REFERENCES services
ON UPDATE CASCADE;

ALTER TABLE service_versions
  ADD CONSTRAINT fk_service_versions_1
FOREIGN KEY (service_id, service_org_id) REFERENCES services
ON UPDATE CASCADE;

CREATE TABLE support
(
  id              BIGINT       NOT NULL
    CONSTRAINT support_pkey
    PRIMARY KEY,
  organization_id VARCHAR(255) NOT NULL,
  service_id      VARCHAR(255) NOT NULL,
  title           VARCHAR(255) NOT NULL,
  status          VARCHAR(255) NOT NULL,
  description     TEXT,
  created_on      TIMESTAMP    NOT NULL,
  created_by      VARCHAR(255) NOT NULL,
  total_comments  INTEGER
);

CREATE INDEX idx_support_comments_1
  ON support (id);

CREATE INDEX idx_support_1
  ON support (organization_id, service_id);

CREATE TABLE support_comments
(
  id         BIGINT       NOT NULL
    CONSTRAINT support_comments_pkey
    PRIMARY KEY,
  support_id BIGINT       NOT NULL,
  comment    TEXT,
  created_on TIMESTAMP    NOT NULL,
  created_by VARCHAR(255) NOT NULL
);

CREATE TABLE svc_gateways
(
  service_version_id BIGINT       NOT NULL
    CONSTRAINT fk_p5dm3cngljt6yrsnvc7uc6a75
    REFERENCES service_versions
    ON UPDATE CASCADE
    CONSTRAINT fk_svc_gateways_1
    REFERENCES service_versions
    ON UPDATE CASCADE,
  gateway_id         VARCHAR(255) NOT NULL,
  CONSTRAINT svc_gateways_pkey
  PRIMARY KEY (service_version_id, gateway_id)
);

CREATE TABLE svc_plans
(
  service_version_id BIGINT       NOT NULL
    CONSTRAINT fk_scv_plans_1
    REFERENCES service_versions
    ON UPDATE CASCADE
    CONSTRAINT fk_t7uvfcsswopb9kh8wpa86blqr
    REFERENCES service_versions
    ON UPDATE CASCADE,
  plan_id            VARCHAR(255) NOT NULL,
  version            VARCHAR(255) NOT NULL,
  CONSTRAINT svc_plans_pkey
  PRIMARY KEY (service_version_id, plan_id, version)
);

CREATE TABLE svc_visibility
(
  service_version_id BIGINT       NOT NULL
    CONSTRAINT fk_svc_version_visibility
    REFERENCES service_versions
    ON UPDATE CASCADE
    CONSTRAINT fk_svc_version_visibility_1
    REFERENCES service_versions
    ON UPDATE CASCADE,
  code               VARCHAR(255) NOT NULL,
  show               BOOLEAN DEFAULT TRUE,
  CONSTRAINT svc_visibility_pkey
  PRIMARY KEY (service_version_id, code)
);

CREATE TABLE users
(
  username      VARCHAR(255) NOT NULL
    CONSTRAINT users_pkey
    PRIMARY KEY,
  kong_username VARCHAR(255),
  email         VARCHAR(255),
  full_name     VARCHAR(255),
  joined_on     TIMESTAMP,
  admin         BOOLEAN      DEFAULT FALSE,
  company       VARCHAR(255),
  location      VARCHAR(255),
  website       VARCHAR(255),
  bio           TEXT,
  pic           oid,
  jwt_key       VARCHAR(255) DEFAULT NULL ::CHARACTER VARYING,
  jwt_secret    VARCHAR(255) DEFAULT NULL ::CHARACTER VARYING
);

CREATE INDEX idx_users_1
  ON users (username);

CREATE INDEX idx_users_2
  ON users (full_name);

CREATE TABLE white_ip_restriction
(
  netw_value VARCHAR(255) NOT NULL
    CONSTRAINT white_ip_restriction_pkey
    PRIMARY KEY
);

CREATE TABLE service_basepaths
(
  servicebean_organization_id VARCHAR(255),
  servicebean_id              VARCHAR(255),
  basepath                    VARCHAR(255),
  CONSTRAINT uk_service_basepaths_1
  UNIQUE (servicebean_organization_id, servicebean_id, basepath),
  CONSTRAINT fk_service_basepaths_1
  FOREIGN KEY (servicebean_organization_id, servicebean_id) REFERENCES services (organization_id, id)
);

CREATE INDEX idx_service_basepaths_1
  ON service_basepaths (servicebean_organization_id, servicebean_id);

CREATE TABLE service_hosts
(
  service_version_id BIGINT       NOT NULL
    CONSTRAINT fk_service_hosts_1
    REFERENCES service_versions,
  hostname           VARCHAR(255) NOT NULL,
  CONSTRAINT uk_service_hosts_1
  UNIQUE (service_version_id, hostname)
);

CREATE INDEX idx_service_hosts_1
  ON service_hosts (service_version_id);

CREATE TABLE service_upstream_targets
(
  service_version_id BIGINT              NOT NULL
    CONSTRAINT fk_service_upstream_targets_1
    REFERENCES service_versions
    ON UPDATE CASCADE,
  target             VARCHAR(255)        NOT NULL,
  port               BIGINT DEFAULT 8000 NOT NULL,
  weight             BIGINT DEFAULT 100
    CONSTRAINT service_upstream_targets_weight_check
    CHECK ((weight >= 0) AND (weight <= 1000))
);

CREATE INDEX idx_service_upstream_targets_1
  ON service_upstream_targets (service_version_id);

CREATE TABLE idp_issuers
(
  issuer   VARCHAR(255) NOT NULL
    CONSTRAINT idp_issuers_pkey
    PRIMARY KEY,
  jwks_uri VARCHAR(255) NOT NULL
);

CREATE TABLE application_version_domains
(
  application_version_id BIGINT       NOT NULL
    CONSTRAINT fk_application_version_domains_1
    REFERENCES application_versions,
  domain                 VARCHAR(512) NOT NULL
    CONSTRAINT uk_application_version_domains_1
    UNIQUE
);