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
  jwt_secret VARCHAR(255) DEFAULT NULL
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

CREATE TABLE key_mapping
(
  from_spec_type VARCHAR(25) NOT NULL,
  to_spec_type VARCHAR(25) NOT NULL,
  from_spec_claim VARCHAR(255) NOT NULL,
  to_spec_claim VARCHAR(255)
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
  gateway_username VARCHAR(255),
  activated BOOLEAN DEFAULT true,
  restricted BOOLEAN DEFAULT false
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

CREATE TABLE oauth_apps
(
  id BIGINT NOT NULL,
  oauth_svc_orgid VARCHAR(255) NOT NULL,
  oauth_svc_id VARCHAR(255) NOT NULL,
  oauth_svc_version VARCHAR(255) NOT NULL,
  oauth_client_id VARCHAR(255) NOT NULL,
  oauth_client_secret VARCHAR(255) NOT NULL,
  oauth_client_redirect VARCHAR(255),
  app_id BIGINT NOT NULL
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
  context VARCHAR(255) DEFAULT 'pub' NOT NULL
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
  form VARCHAR(4096),
  form_type VARCHAR(255),
  icon VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  plugin_id BIGINT,
  scope_service BOOLEAN,
  scope_plan BOOLEAN,
  scope_auto BOOLEAN,
  form_override VARCHAR(4096) DEFAULT NULL,
  default_config VARCHAR(4096) DEFAULT NULL
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
  readme TEXT
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

ALTER TABLE key_mapping ADD PRIMARY KEY (from_spec_type, to_spec_type, from_spec_claim);

ALTER TABLE mail_templates ADD PRIMARY KEY (topic);

ALTER TABLE managed_applications ADD PRIMARY KEY (id);

ALTER TABLE memberships ADD PRIMARY KEY (id);

ALTER TABLE oauth_apps ADD PRIMARY KEY (id);

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

CREATE INDEX idx_oauth_defs_1 ON oauth_apps (oauth_svc_orgid, oauth_svc_id, oauth_svc_version);

CREATE INDEX idx_fk_oauth_scopes_1 ON oauth_scopes (serviceversionbean_id);

CREATE INDEX idx_organizations_1 ON organizations (name);

CREATE INDEX idx_fk_permissions_1 ON permissions (role_id);

CREATE INDEX idx_fk_plans_1 ON plans (organization_id);

CREATE INDEX idx_policies_1 ON policies (organization_id, entity_id, entity_version);

CREATE INDEX idx_policies_2 ON policies (order_index);

CREATE INDEX idx_fk_policies_1 ON policies (definition_id);

CREATE INDEX idx_service_brandings_1 ON service_brandings (organization_id, service_id);

CREATE INDEX idx_service_brandings_2 ON service_brandings (branding_id);

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

ALTER TABLE service_brandings ADD CONSTRAINT uk_service_brandings_1 UNIQUE (service_id, branding_id);

ALTER TABLE service_defs ADD CONSTRAINT uk_service_defs_1 UNIQUE (service_version_id);

ALTER TABLE service_versions ADD CONSTRAINT uk_service_versions_1 UNIQUE (service_id, service_org_id, version);

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

ALTER TABLE oauth_apps ADD CONSTRAINT fk_oauth_apps_1 FOREIGN KEY (app_id) REFERENCES application_versions (id);

ALTER TABLE oauth_scopes ADD CONSTRAINT fk_oauth_scopes_1 FOREIGN KEY (serviceversionbean_id) REFERENCES service_versions (id);

ALTER TABLE permissions ADD CONSTRAINT fk_permissions_1 FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE plan_versions ADD CONSTRAINT fk_plan_versions_1 FOREIGN KEY (plan_id, plan_org_id) REFERENCES plans (id, organization_id);

ALTER TABLE plans ADD CONSTRAINT fk_plans_1 FOREIGN KEY (organization_id) REFERENCES organizations (id);

ALTER TABLE policies ADD CONSTRAINT fk_policies_1 FOREIGN KEY (definition_id) REFERENCES policydefs (id);

ALTER TABLE service_brandings ADD CONSTRAINT fk_service_brandings_1 FOREIGN KEY (service_id, organization_id) REFERENCES services (id, organization_id);

ALTER TABLE service_brandings ADD CONSTRAINT fk_service_brandings_2 FOREIGN KEY (branding_id) REFERENCES brandings (id);

ALTER TABLE service_defs ADD CONSTRAINT fk_service_defs_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE service_versions ADD CONSTRAINT fk_service_versions_1 FOREIGN KEY (service_id, service_org_id) REFERENCES services (id, organization_id);

ALTER TABLE services ADD CONSTRAINT fk_services_1 FOREIGN KEY (organization_id) REFERENCES organizations (id);

ALTER TABLE svc_gateways ADD CONSTRAINT fk_svc_gateways_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE svc_plans ADD CONSTRAINT fk_scv_plans_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE svc_visibility ADD CONSTRAINT fk_svc_visibility_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

-- POPULATE TABLES

-- ROLES

INSERT INTO roles (id, auto_grant, created_by, created_on, description, name) VALUES ('Owner', true, 'admin', CURRENT_DATE, 'Automatically granted to the user who creates an Organization.  Grants all privileges.', 'Owner');

INSERT INTO roles (id, auto_grant, created_by, created_on, description, name) VALUES ('Developer', null, 'admin', CURRENT_DATE, 'Users responsible for creating and managing applications and services should be granted this role within an Organization.', 'Developer');

INSERT INTO roles (id, auto_grant, created_by, created_on, description, name) VALUES ('Watcher', null, 'admin', CURRENT_DATE, 'Users who only need read access can be granted this role. They can view all information within an Organization, but cannot make changes.', 'Watcher');

-- PERMISSIONS

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 0);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 1);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 2);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 3);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 4);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 5);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 6);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 7);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 8);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 9);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 10);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 11);

INSERT INTO permissions (role_id, permissions) VALUES ('Watcher', 0);

INSERT INTO permissions (role_id, permissions) VALUES ('Watcher', 3);

INSERT INTO permissions (role_id, permissions) VALUES ('Watcher', 6);

INSERT INTO permissions (role_id, permissions) VALUES ('Watcher', 9);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 0);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 1);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 3);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 4);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 6);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 7);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 9);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 10);

-- POLICY DEFINITIONS

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('BasicAuthentication', 'Add Basic Authentication to your APIs', '{
  "type": "object",
  "title": "Basic Authentication",
  "properties": {
    "hide_credentials": {
      "title": "Hide credentials",
      "description": "An optional boolean value telling the plugin to hide the credential to the upstream API server. It will be removed by the gateway before proxying the request.",
      "type": "boolean",
      "default": false
    }
  }
}', 'JsonSchema', 'fa-user', 'Basic Authentication Policy', null, false, false, false, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('SSL', 'Add an SSL certificate for an underlying service', '{
  "type": "object",
  "title": "SSL",
  "properties": {
    "cert": {
      "title": "Certificate",
      "description": "Specify the path of the certificate file to upload.",
      "type": "string"
    },
    "only_https": {
      "title": "Only HTTPS allowed",
      "description": "Specify if the service should only be available through an https protocol.",
      "type": "boolean",
      "default": false
    },
    "key": {
      "title": "Key",
      "type": "string",
      "description": "Specify the path of the certificate key file to upload"
    }
  },
  "required": [
    "cert",
    "key"
  ]
}', 'JsonSchema', 'fa-lock', 'SSL Policy', null, false, false, false, null, null);
INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('RequestTransformer', 'Modify the request before hitting the upstream sever', '{
  "type": "object",
  "title": "Request Transformer",
  "properties": {
    "remove": {
      "title": "Remove from request",
      "type": "object",
      "properties": {
          "querystring": {
              "type": "array",
              "items": {
                "title": "Querystring",
                "type": "string",
                "description": "Parameter name to remove from the request querystring."
              }
          },
          "body": {
              "type": "array",
              "items": {
                "title": "Form",
                "type": "string",
                "description": "Parameter names to remove from the request body."
              }
          },
          "headers": {
              "type": "array",
              "items": {
                "title": "Header",
                "type": "string",
                "description": "Header names to remove from the request."
              }
          }
      }
    },
    "add": {
      "title": "Add to request",
      "type": "object",
      "properties": {
          "querystring": {
              "type": "array",
              "items": {
                "title": "Query",
                "type": "string",
                "description": "Paramname:value to add to the request querystring."
              }
          },
          "body": {
              "type": "array",
              "items": {
                "title": "Form",
                "type": "string",
                "description": "Paramname:value to add to the request body in urlencoded format."
              }
          },
          "headers": {
              "type": "array",
              "items": {
                "title": "Header",
                "type": "string",
                "description": "Headername:value to add to the request headers."
              }
          }
      }
    }
  }
}', 'JsonSchema', 'fa-chevron-circle-right', 'Request Transformer Policy', null, true, false, false, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('RequestSizeLimiting', 'Block requests with bodies greater than a specific size', '{
  "type": "object",
  "title": "Request Size Limiting",
  "properties": {
    "allowed_payload_size": {
      "title": "Allowed payload size",
      "description": "Allowed request payload size in megabytes, default is 128 (128 000 000 Bytes)",
      "type": "number",
      "default": 128,
      "minimum": 0
    }
  },
  "required": [
    "allowed_payload_size"
  ]
}', 'JsonSchema', 'fa-compress', 'Request Size Limiting Policy', null, true, true, false, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('ResponseTransformer', 'Modify the upstream response before returning it to the client', '{
  "type": "object",
  "title": "Response Transformer",
  "properties": {
    "remove": {
      "title": "Remove from request",
      "type": "object",
      "properties": {
          "headers": {
              "type": "array",
              "items": {
                    "title": "Header",
                    "type": "string",
                    "description": "Header name to remove from the response headers."
              }
          },
          "json": {
              "type": "array",
              "items": {
                "title": "JSON",
                "type": "string",
                "description": "JSON key name to remove from a JSON response body."
              }
          }
      }
    },
    "add": {
      "title": "Add to request",
      "type": "object",
      "properties": {
          "headers": {
              "type": "array",
              "items": {
                "title": "Header",
                "type": "string",
                "description": "Headername:value to add to the response headers."
              }
          },
          "json": {
              "type": "array",
              "items": {
                "title": "JSON",
                "type": "string",
                "description": "Jsonkey:value to add to a JSON response body."
              }
          }
      }
    }
  }
}', 'JsonSchema', 'fa-chevron-circle-left', 'Response Transformer Policy', null, true, false, false, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('RateLimiting', 'Rate-limit how many HTTP requests a consumer can make', '{
  "type": "object",
  "title": "Rate Limiting",
  "properties": {
    "day": {
      "title": "Day(s)",
      "description": "The amount of HTTP requests the developer can make per day. At least one limit must exist.",
      "type": "integer"
    },
    "minute": {
      "title": "Minute(s)",
      "description": "The amount of HTTP requests the developer can make per minute. At least one limit must exist.",
      "type": "integer"
    },
    "second": {
      "title": "Second(s)",
      "description": "The amount of HTTP requests the developer can make per second. At least one limit must exist.",
      "type": "integer"
    },
    "hour": {
      "title": "Hour(s)",
      "description": "The amount of HTTP requests the developer can make per hour. At least one limit must exist.",
      "type": "integer"
    },
    "month": {
      "title": "Month(s)",
      "description": "The amount of HTTP requests the developer can make per month. At least one limit must exist.",
      "type": "integer"
    },
    "year": {
      "title": "Year(s)",
      "description": "The amount of HTTP requests the developer can make per year. At least one limit must exist.",
      "type": "integer"
    }
  }
}', 'JsonSchema', 'fa-tachometer', 'Rate Limiting Policy', null, true, true, false, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('TCPLog', 'Send request and response logs to a TCP server', '{
  "type": "object",
  "title": "TCP Log",
  "properties": {
    "host": {
      "title": "Host",
      "description": "The IP address or host name to send data to.",
      "type": "string"
    },
    "keepalive": {
      "title": "Keep alive",
      "description": "Default 60000. An optional value in milliseconds that defines for how long an idle connection will live before being closed.",
      "type": "number",
      "default": 60000
    },
    "timeout": {
      "title": "Time-out",
      "description": "Default 10000. An optional timeout in milliseconds when sending data to the upstream server.",
      "type": "number",
      "default": 10000
    },
    "port": {
      "title": "Port",
      "description": "The port to send data to on the upstream server.",
      "type": "number"
    }
  },
  "required": [
    "host","port"
  ]
}', 'JsonSchema', 'fa-random', 'TCP Log Policy', null, true, false, false, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('UDPLog', 'Send request and response logs to a UDP server', '{
  "type": "object",
  "title": "UDP Log",
  "properties": {
    "host": {
      "title": "Host",
      "description": "The IP address or host name to send data to.",
      "type": "string"
    },
    "timeout": {
      "title": "Time-out",
      "description": "Default 10000. An optional timeout in milliseconds when sending data to the upstream server.",
      "type": "number",
      "default": 10000
    },
    "port": {
      "title": "Port",
      "description": "The port to send data to on the upstream server.",
      "type": "number"
    }
  },
  "required": [
    "host","port"
  ]
}', 'JsonSchema', 'fa-crosshairs', 'UDP Log Policy', null, true, false, false, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('HTTPLog', 'Send request and response logs to a HTTP server', '{
  "type": "object",
  "title": "HTTP Log",
  "properties": {
    "http_endpoint": {
      "title": "HTTP Endpoint",
      "description": "The HTTP endpoint (including the protocol to use) where to send the data to.",
      "type": "string"
    },
    "method": {
      "title": "HTTP Method",
      "description": "Default POST. An optional method used to send data to the http server, other supported values are PUT, PATCH.",
      "type": "string",
      "default": "POST"
    },
    "keepalive": {
      "title": "Keep alive",
      "description": "Default 60000. An optional value in milliseconds that defines for how long an idle connection will live before being closed.",
      "type": "number",
      "default": 60000
    },
    "timeout": {
      "title": "Time-out",
      "description": "Default 10000. An optional timeout in milliseconds when sending data to the upstream server.",
      "type": "number",
      "default": 10000
    }
  },
  "required": [
    "http_endpoint"
  ]
}', 'JsonSchema', 'fa-exchange', 'HTTP Log Policy', null, false, false, true, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('FileLog', 'Append request and response data to a log file on disk', '{
  "type": "object",
  "title": "File Log",
  "properties": {
    "path": {
      "title": "Path",
      "description": "The file path of the output log file. The plugin will create the file if it doesn''t exist yet. Make sure Kong has write permissions to this file.",
      "type": "string"
    }
  },
  "required": [
    "path"
  ]
}', 'JsonSchema', 'fa-file-text-o', 'File Log Policy', null, false, false, false, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('Analytics', 'View API analytics in Mashape analytics - retention 1 day', '{
  "type": "object",
  "title": "File Log",
  "properties": {
    "service_token": {
      "title": "Service token (API key)",
      "type": "string",
      "required": true
    }
  },
  "required": [
    "service_token"
  ]
}', 'JsonSchema', 'fa-line-chart', 'Mashape Analytics Policy', null, true, false, false, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('IPRestriction', 'Whitelist or Blacklist IPs that can make requests', '{
  "type": "object",
  "title": "IP Restriction",
  "properties": {
    "blacklist": {
        "type": "array",
        "items":{
               "type": "string",
               "pattern": "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/(\\d|[1-2]\\d|3[0-2]))?$",
                "description": "List of IPs or CIDR ranges to blacklist. You cannot set blacklist values if you have already whitelist values specified!",
                "validationMessage":"IP or CIDR required"
        }
    },
    "whitelist": {
        "type": "array",
        "items":{
            "type": "string",
            "pattern": "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/(\\d|[1-2]\\d|3[0-2]))?$",
            "description": "List of IPs or CIDR ranges to whitelist. You cannot set whitelist values if you have already blacklist values specified.",
            "validationMessage":"IP or CIDR required"
        }
    }
  }
}', 'JsonSchema', 'fa-table', 'IP Restriction Policy', null, true, true, false, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('JWTUp', 'Transforms authentication credentials to upstream certificated signed JWT. When policy is added in combination with JWT policy, JWT will be ignored.', '{
  "type": "object",
  "title": "JWT-Upstream",
  "properties": {
    "placeholder" :{}
  },
  "required": []
}', 'JsonSchema', 'fa-certificate', 'JWT-Up Policy', null, true, false, false, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('JWT', 'Enable the service to accept and validate Json Web Tokens towards the upstream API.', '{
  "type": "object",
  "title": "JWT Token",
  "properties": {
    "placeholder" :{}
  },
  "required": []
}', 'JsonSchema', 'fa-certificate', 'JWT Policy', null, true, false, false, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('JSONThreatProtection', 'Protect your API from JSON content-level attack attempts that use structures that overwhelm JSON Parsers.', '{
  "type": "object",
  "title": "JSON Threat Protection",
  "properties": {
    "source": {
      "title": "Source",
      "type": "string",
      "pattern": "^request$|^response$|^message$",
      "validationMessage": "Should be one of: request,response,message",
      "description": "The sources that should be protected"
    },
    "container_depth": {
      "title": "Container Depth",
      "description": "Specifies the maximum allowed containment depth, where the containers are objects or arrays. For example, an array containing an object which contains an object would result in a containment depth of 3. If you do not specify this element, or if you specify a negative integer, the system does not enforce any limit.",
      "type": "number",
      "default": 0
    },
    "string_value_length": {
      "title": "String Value Length",
      "description": "Specifies the maximum length allowed for a string value. If you do not specify this element, or if you specify a negative integer, the system does not enforce a limit.",
      "type": "number",
      "default": 0
    },
    "array_element_count": {
      "title": "Array Element Count",
      "description": "Specifies the maximum number of elements allowed in an array. If you do not specify this element, or if you specify a negative integer, the system does not enforce a limit.",
      "type": "number",
      "default": 0
    },
    "object_entry_count": {
      "title": "Object Entry Count",
      "description": "Specifies the maximum number of entries allowed in an object. If you do not specify this element, or if you specify a negative integer, the system does not enforce any limit.",
      "type": "number",
      "default": 0
    },
    "object_entry_name_length": {
      "title": "Object Entry Name Length",
      "description": "Specifies the maximum string length allowed for a property name within an object. If you do not specify this element, or if you specify a negative integer, the system does not enforce any limit.",
      "type": "number",
      "default": 0
    }
  }
}', 'JsonSchema', 'fa-shield', 'JSON Threat Protection', null, false, false, false, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('LDAPAuthentication', 'Add LDAP Bind Authentication to your APIs, with username and password protection.', '{
  "type": "object",
  "title": "LDAP Authentication",
  "properties": {
    "ldap_host": {
      "title": "LDAP Host",
      "description": "Host on which the LDAP server is running.",
      "type": "string"
    },
    "ldap_port": {
      "title": "LDAP Port",
      "description": "TCP port where the LDAP server is listening.",
      "type": "number",
      "default": 636
    },
    "base_dn": {
      "title": "Base DN",
      "description": "Base DN as the starting point for the search.",
      "type": "string"
    },
    "attribute": {
      "title": "Attribute",
      "description": "Attribute to be used to search the user.",
      "type": "string"
    },
    "cache_ttl": {
      "title": "Cache TTL",
      "description": "Cache expiry time",
      "type": "number",
      "default": 60
    },
    "timeout": {
      "title": "Timeout",
      "description": "An optional timeout in milliseconds when waiting for connection with LDAP server.",
      "type": "number",
      "default": 10000
    },
    "keepalive": {
      "title": "Keep Alive",
      "description": "An optional value in milliseconds that defines for how long an idle connection to LDAP server will live before being closed.",
      "type": "number",
      "default": 60000
    },
    "verify_ldap_host": {
      "title": "Verify LDAP Host",
      "description": "Set it to true to authenticate LDAP server.",
      "type": "boolean",
      "default": false
    },
    "start_tls": {
      "title": "Start TLS",
      "description": "Set it to true to issue StartTLS (Transport Layer Security) extended operation over ldap connection.",
      "type": "boolean",
      "default": false
    },
    "hide_credentials": {
      "title": "Hide credentials",
      "description": "An optional boolean value telling the plugin to hide the credential to the upstream API server. It will be removed by the gateway before proxying the request.",
      "type": "boolean",
      "default": false
    }
  },
  "required": [
    "ldap_host",
    "cache_ttl",
    "ldap_port",
    "base_dn"
  ]
}', 'JsonSchema', 'fa-database', 'LDAP Authentication Policy', null, true, false, false, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('OAuth2', 'Add an OAuth2 Authentication to your APIs', '{
  "type": "object",
  "title": "OAuth2",
  "properties": {
    "scopes": {
      "type": "array",
      "items": {
          "type": "object",
          "properties":{
            "scope":{
                "title": "Scope",
                "type": "string",
                "pattern": "^[a-z,A-Z]+$",
                "description": "Provide the scope identifier that will be available to the end user (use only lowercase characters and no special characters)."
            },
            "scope_desc":{
                "title": "Scope Description",
                "type": "string",
                "description": "Describes the scope that will be available to the end user."
            }
          }
      }
    },
    "mandatory_scope": {
      "title": "Mandatory scope",
      "type": "boolean",
      "func": "function",
      "default": false,
      "description": "An optional boolean value telling the plugin to require at least one scope to be authorized by the end user."
    },
    "token_expiration": {
      "title": "Token expiration",
      "type": "number",
      "minimum": 0,
      "description": "An optional integer value telling the plugin how long should a token last, after which the client will need to refresh the token. Set to 0 to disable the expiration."
    },
    "enable_authorization_code": {
      "title": "Enable Authorization Code Grant",
      "type": "boolean",
      "default": true,
      "description": "An optional boolean value to enable the three-legged Authorization Code flow (RFC 6742 Section 4.1)."
    },
    "enable_implicit_grant": {
      "title": "Enable Implicit Grant",
      "type": "boolean",
      "default": false,
      "description": "An optional boolean value to enable the Implicit Grant flow which allows to provision a token as a result of the authorization process (RFC 6742 Section 4.2)."
    },
    "enable_client_credentials": {
      "title": "Enable Client Credentials Grant",
      "type": "boolean",
      "default": false,
      "description": "An optional boolean value to enable the Client Credentials Grant flow (RFC 6742 Section 4.4)."
    },
    "hide_credentials": {
      "title": "Hide credentials",
      "type": "boolean",
      "default": false,
      "description": "An optional boolean value telling the plugin to hide the credential to the upstream API server. It will be removed by Kong before proxying the request."
    }
  }
}', 'JsonSchema', 'fa-sign-in', 'OAuth2 Policy', null, true, false, false, '[
  "scopes",
  "mandatory_scope",
  "token_expiration",
  "enable_authorization_code",
  "enable_implicit_grant",
  {
    "type":"help",
    "helpvalue":"<p class=\"text-justified text-warning xsmall\">WARNING: The Implicit Grant flow is necessary for try-out functionality in the marketplace, but also prohibits the usage of refresh tokens.</p>"
  },
  "enable_client_credentials",
  "hide_credentials"
]', null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('HAL', 'The HAL policy rewrites currie-values from hal/json bodies.', '{
  "type": "object",
  "title": "HAL Authentication",
  "properties": {
    "placeholder" :{}
  },
  "required": []
}', 'JsonSchema', 'fa-paw', 'HAL Policy', null, true, false, false, null, null);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('KeyAuthentication', 'Add Key Authentication to your APIs', '{
  "type": "object",
  "title": "Key Authentication",
  "properties": {
    "key_names": {
      "title":"Key names",
      "type": "array",
      "items": {
        "type": "string",
        "description":"Describes a name where the plugin will look for a valid credential. The client must send the authentication key in one of the specified key names, and the plugin will try to read the credential from a header, the querystring, a form parameter (in this order).",
        "default": "apikey"
      }
    },
    "hide_credentials": {
      "title": "Hide credentials",
      "description":"An optional boolean value telling the plugin to hide the credential to the upstream API server. It will be removed by the gateway before proxying the request.",
      "type": "boolean",
      "default": false
    }
  },
  "required": [
    "key_names"
  ]
}', 'JsonSchema', 'fa-key', 'Key Authentication Policy', null, true, false, true, null, '{"key_names":["apikey"],"hide_credentials":false}');

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('CORS', 'Allow consumers to make requests from browsers to your APIs', '{
  "type": "object",
  "title": "CORS",
  "properties": {
    "methods": {
      "type": "array",
      "items": {
        "title": "Methods",
        "type": "string",
        "pattern": "^POST$|^GET$|^HEAD$|^PUT$|^PATCH$|^DELETE$",
        "validationMessage": "Should be one of: GET,HEAD,PUT,PATCH,POST,DELETE",
        "description": "Value for the Access-Control-Allow-Methods header, expects a string (e.g. GET or POST). Defaults to the values GET,HEAD,PUT,PATCH,POST,DELETE."
      }
    },
    "credentials": {
      "title": "Credentials",
      "description": "Flag to determine whether the Access-Control-Allow-Credentials header should be sent with true as the value.",
      "type": "boolean",
      "default": false
    },
      "headers": {
        "type": "array",
        "items": {
          "title": "Headers",
          "type": "string",
          "description": "Value for the Access-Control-Allow-Headers header (e.g. Origin, Authorization). Defaults to the value of the Access-Control-Request-Headers header."
        }
    },
    "exposed_headers": {
                "type": "array",
        "items": {
          "title": "Exposed headers",
          "type": "string",
          "description": "Value for the Access-Control-Expose-Headers header (e.g. Origin, Authorization). If not specified, no custom headers are exposed."
        }
    },
    "origin": {
      "title": "Origin",
      "type": "string",
      "default": "*",
      "description": "Value for the Access-Control-Allow-Origin header, expects a String. Defaults to *."
    },
    "max_age": {
      "title": "Max age",
      "type": "number",
      "description": "Indicated how long the results of the preflight request can be cached, in seconds.",
      "default": 3600,
      "minimum": 1
    },
    "preflight_continue": {
      "title": "Preflight continue",
      "type": "boolean",
      "description": "A boolean value that instructs the plugin to proxy the OPTIONS preflight request to the upstream API. Defaults to false.",
      "default": false
    }
  }
}', 'JsonSchema', 'fa-code', 'CORS Policy', null, true, false, true, '[
  "methods",
  "credentials",
  "headers",
  {
    "type": "help",
    "helpvalue": "<p class=\"text-justified text-warning xsmall\">WARNING: When implementing a custom CORS policy we STRONGLY recommend including the following headers in order to ensure correct functioning of the API Gateway: \"Accept\",\"Accept-Version\",\"Content-Length\",\"Content-MD5\",\"Content-Type\",\"Date\",\"apikey\",\"Authorization\".</p>"
  },
  "exposed_headers",
  "origin",
  "max_age",
  "preflight_continue"
]', '{"methods":["HEAD","DELETE","GET","POST","PUT","PATCH"],"credentials":false,"exposed_headers":[],"max_age":3600.0,"preflight_continue":false,"headers":["Accept","Accept-Version","Content-Length","Content-MD5","Content-Type","Date","apikey","Authorization"]}');

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config) VALUES ('ACL', 'Enable the service to work with an Access Control List', '{
  "type": "object",
  "title": "ACL",
  "properties": {
    "placeholder": {}
  }
}', 'JsonSchema', 'fa-users', 'ACL Policy', null, true, false, true, null, null);

-- CONFIG

INSERT INTO config(id, config_path) VALUES (7, '/opt/wildfly/standalone/configuration/application.conf');