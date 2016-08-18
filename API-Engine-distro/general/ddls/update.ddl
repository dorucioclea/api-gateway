-- update for v0.8.1
-- fix missing contraints from 0.8.0
ALTER TABLE mail_templates ADD CONSTRAINT pksb_mail_templates PRIMARY KEY (topic);
ALTER TABLE events ADD PRIMARY KEY (id);
ALTER TABLE followers ADD PRIMARY KEY (ServiceBean_id,ServiceBean_organization_id,user_id);
ALTER TABLE endpoint_properties ADD PRIMARY KEY (service_version_id, name);
ALTER TABLE svc_gateways ADD PRIMARY KEY (service_version_id, gateway_id);
ALTER TABLE white_ip_restriction ADD PRIMARY KEY (netw_value);
ALTER TABLE black_ip_restriction ADD PRIMARY KEY (netw_value);
ALTER TABLE svc_plans ADD PRIMARY KEY (service_version_id, plan_id, version);
ALTER TABLE svc_visibility ADD PRIMARY KEY (service_version_id, code);
ALTER TABLE application_versions ADD PRIMARY KEY (id);
ALTER TABLE applications ADD PRIMARY KEY (id, organization_id);
ALTER TABLE auditlog ADD PRIMARY KEY (id);
ALTER TABLE announcements ADD PRIMARY KEY (id);
ALTER TABLE contracts ADD PRIMARY KEY (id);
ALTER TABLE gateways ADD PRIMARY KEY (id);
ALTER TABLE memberships ADD PRIMARY KEY (id);
ALTER TABLE organizations ADD PRIMARY KEY (id);
ALTER TABLE plan_versions ADD PRIMARY KEY (id);
ALTER TABLE plans ADD PRIMARY KEY (id, organization_id);
ALTER TABLE plugins ADD PRIMARY KEY (id);
ALTER TABLE policies ADD PRIMARY KEY (id);
ALTER TABLE oauth_apps ADD PRIMARY KEY (id);
ALTER TABLE policydefs ADD PRIMARY KEY (id);
ALTER TABLE roles ADD PRIMARY KEY (id);
ALTER TABLE service_defs ADD PRIMARY KEY (id);
ALTER TABLE service_versions ADD PRIMARY KEY (id);
ALTER TABLE services ADD PRIMARY KEY (id, organization_id);
ALTER TABLE users ADD PRIMARY KEY (username);
ALTER TABLE support ADD PRIMARY KEY (id);
ALTER TABLE support_comments ADD PRIMARY KEY (id);
ALTER TABLE managed_applications ADD PRIMARY KEY (id);

ALTER TABLE managed_applications ADD CONSTRAINT FK_managed_applications_1 FOREIGN KEY (gateway_id) REFERENCES gateways (id) ON UPDATE CASCADE;
ALTER TABLE services ADD CONSTRAINT FK_services_1 FOREIGN KEY (organization_id) REFERENCES organizations (id) ON UPDATE CASCADE;
ALTER TABLE contracts ADD CONSTRAINT FK_contracts_1 FOREIGN KEY (appv_id) REFERENCES application_versions (id) ON UPDATE CASCADE;
ALTER TABLE service_defs ADD CONSTRAINT FK_service_defs_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE application_versions ADD CONSTRAINT FK_application_versions_1 FOREIGN KEY (app_id, app_org_id) REFERENCES applications (id, organization_id) ON UPDATE CASCADE;
ALTER TABLE contracts ADD CONSTRAINT FK_contracts_1 FOREIGN KEY (svcv_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE service_versions ADD CONSTRAINT FK_service_versions_1 FOREIGN KEY (service_id, service_org_id) REFERENCES services (id, organization_id) ON UPDATE CASCADE;
ALTER TABLE endpoint_properties ADD CONSTRAINT FK_endpoint_properties_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE applications ADD CONSTRAINT FK_applications_1 FOREIGN KEY (organization_id) REFERENCES organizations (id) ON UPDATE CASCADE;
ALTER TABLE policies ADD CONSTRAINT FK_policies_1 FOREIGN KEY (definition_id) REFERENCES policydefs (id) ON UPDATE CASCADE;
ALTER TABLE oauth_apps ADD CONSTRAINT FK_oauth_apps_1 FOREIGN KEY (app_id) REFERENCES application_versions (id) ON UPDATE CASCADE;
ALTER TABLE plans ADD CONSTRAINT FK_plans_1 FOREIGN KEY (organization_id) REFERENCES organizations (id) ON UPDATE CASCADE;
ALTER TABLE contracts ADD CONSTRAINT FK_contracts_2 FOREIGN KEY (planv_id) REFERENCES plan_versions (id) ON UPDATE CASCADE;
ALTER TABLE svc_gateways ADD CONSTRAINT FK_svc_gateways_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE permissions ADD CONSTRAINT FK_permissions_1 FOREIGN KEY (role_id) REFERENCES roles (id) ON UPDATE CASCADE;
ALTER TABLE svc_plans ADD CONSTRAINT FK_scv_plans_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE svc_visibility ADD CONSTRAINT FK_svc_version_visibility_1 FOREIGN KEY (service_version_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE plan_versions ADD CONSTRAINT FK_plan_versions_1 FOREIGN KEY (plan_id, plan_org_id) REFERENCES plans (id, organization_id) ON UPDATE CASCADE;
ALTER TABLE followers ADD CONSTRAINT FK_followers_1 FOREIGN KEY (ServiceBean_id,ServiceBean_organization_id) REFERENCES services (id,organization_id) ON UPDATE CASCADE;

-- fix missing config
ALTER TABLE policies ADD COLUMN gateway_id VARCHAR(255) NULL;

-- terms and agreement
ALTER TABLE service_versions ADD COLUMN terms_agreement_required BOOL DEFAULT FALSE;
ALTER TABLE service_versions ADD COLUMN readme TEXT NULL;
UPDATE service_versions SET readme = terms FROM services WHERE service_versions.service_org_id = services.organization_id AND service_versions.service_id = services.id AND services.terms NOTNULL;
UPDATE service_versions SET status = 'Created' WHERE terms_agreement_required = TRUE AND readme IS NULL AND status = 'Ready';
ALTER TABLE contracts ADD COLUMN terms_agreed BOOL DEFAULT FALSE;
CREATE TABLE defaults (id VARCHAR(255) NOT NULL, service_terms TEXT NULL);
ALTER TABLE defaults ADD PRIMARY KEY (id);

-- add config in db
CREATE TABLE config(id BIGINT NOT NULL, config_path VARCHAR(255) NOT NULL);
ALTER TABLE config ADD PRIMARY KEY (id);
INSERT INTO config(id,config_path) VALUES (7,'/opt/wildfly/standalone/configuration/application.conf');

-- update gateway (remove oauth2 endpoints, add jwt/oauth expiration time)
ALTER TABLE gateways DROP COLUMN oauth_authorize;
ALTER TABLE gateways DROP COLUMN oauth_token;
ALTER TABLE gateways DROP COLUMN oauth_context;

ALTER TABLE gateways ADD COLUMN oauth_exp_time INT NULL DEFAULT 7200;
ALTER TABLE gateways ADD COLUMN jwt_pub_key TEXT NULL DEFAULT '';
ALTER TABLE gateways ADD COLUMN jwt_pub_key_endpoint VARCHAR(255) NULL DEFAULT '';
UPDATE gateways SET jwt_pub_key_endpoint='/apiengineauth/v1/gtw/tokens/pub';

UPDATE gateways SET jwt_pub_key='-----BEGIN PUBLIC KEY-----
MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAjmrg7sFxRdobSZHI2Zjk
nrpFT/QrXDpYzUU8IMa4TOkgERtZ3OBlZdmcbyufpBn52fX9XEeH9TuB919cPxBE
zJ7CsReS+Wqpy9PSw+pmCiCfjHflud2uw50neX8eJxYtzHC7UN8+uA8oCKjw0I3P
+ECa7aW/DmMcI/5Osrixe7fPzv8CEzhTbw7A96nK2+VI/UqFWf1oDswlX8POhzLE
iuj7xBiubjl6N4DZnQyao8S2EgfPONJ4mrIn6TD071/tOMh1GYAwJpVCv3agRQWG
8MilaayrC4Z53k6dKWQS6IfU7w5bgB1+hgIzph+NMo7VY4NbJX96uoD7AoiB4o66
rS1jCKKyDqL0M90C1Hh7+R+yMhIkFdEGCKFGh3fl9UDGJ4FDTmo4du0CqnmwmjoV
fRdtyn+61ADxP6zd7n1LAqyPB4EkxukQ77K/ONLpRv2trft9oSUR1jWMq7w12WYr
hYVxOGSo5N4EGBJjHAyQgMCS8PXgm7N9XaNukes0YCyAL9XSBCE3n4T4BOWG9D2B
CD/zUvn5CFywJhug9Rw4LWt0o2GayiN3yH0pdXAsjSFTb7VivpOsW0/y6iGf0BjK
T8yXJEo8oPp4H2IuL4xL48mntBnVjPsItnziGCjqgHB7lqb7qyu/6+xtHgLlFoc2
0KBvSaDFYbbEtO4NFVrMuIECAwEAAQ==
-----END PUBLIC KEY-----';

-- update JWT: remove expiration claim option because by default applied.
UPDATE policydefs SET description = 'Enable the service to accept and validate Json Web Tokens towards the upstream API.', form='{
  "type": "object",
  "title": "JWT Token",
  "properties": {},
  "required": []
}' WHERE id = 'JWT';


-- add JWT-Up: config is implicitly set by the API Engine.
INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('JWTUp', 'Transforms authentication credentials to upstream certificated signed JWT. When policy is added in combination with JWT policy, JWT will be ignored.', '{
  "type": "object",
  "title": "JWT-Upstream",
  "properties": {},
  "required": []
}', 'JsonSchema', 'fa-certificate', 'JWT-Up Policy', NULL ,TRUE ,FALSE ,FALSE );

--Multiple callback URI's for kong 0.8.3

CREATE TABLE app_oauth_redirect_uris AS SELECT application_versions.id, application_versions.oauth_client_redirect FROM application_versions WHERE oauth_client_redirect NOTNULL;
ALTER TABLE app_oauth_redirect_uris RENAME COLUMN id TO application_version_id;
CREATE INDEX IDX_app_oauth_redirect_uris_1 ON app_oauth_redirect_uris(application_version_id);
ALTER TABLE app_oauth_redirect_uris ADD CONSTRAINT UK_app_oauth_redirect_uris UNIQUE (application_version_id, oauth_client_redirect);
ALTER TABLE app_oauth_redirect_uris ADD CONSTRAINT FK_app_oauth_redirect_uris_1 FOREIGN KEY (application_version_id) REFERENCES application_versions (id) ON UPDATE CASCADE;
ALTER TABLE application_versions DROP COLUMN oauth_client_redirect;

--Fix for missing escape character in IP restriction form

UPDATE policydefs SET form = '{
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
}' WHERE id = 'IPRestriction';

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto) VALUES ('JSONThreatProtection', 'Protect your API from JSON content-level attack attempts that use structures that overwhelm JSON Parsers.', '{
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
}', 'JsonSchema', 'fa-shield', 'JSON Threat Protection', NULL, TRUE, FALSE, FALSE);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto) VALUES ('LDAP', 'Add LDAP Bind Authentication to your APIs, with username and password protection.', '{
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
    "cache_ttl"
    "ldap_port",
    "base_dn"
  ]
}', 'JsonSchema', 'fa-database', 'LDAP Policy', NULL, TRUE, FALSE, FALSE);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('HAL', 'The HAL policy rewrites currie-values from hal/json bodies.', '{
  "type": "object",
  "title": "HAL",
  "properties": {},
  "required": []
}', 'JsonSchema', 'fa-paw', 'HAL Policy', NULL ,TRUE ,FALSE ,FALSE );