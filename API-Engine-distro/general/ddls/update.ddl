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
UPDATE gateways SET jwt_pub_key_endpoint='/keys/pub';

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

------------------------------------------------------------------------------------------------------------------------
------------------------------------ UPDATE FROM KONG 0.8.1 -> 0.8.3 STARTS HERE ---------------------------------------
------------------------------------------------------------------------------------------------------------------------
--Multiple callback URI's for kong 0.8.3

CREATE TABLE app_oauth_redirect_uris AS SELECT application_versions.id, application_versions.oauth_client_redirect FROM application_versions WHERE oauth_client_redirect NOTNULL AND oauth_client_redirect <> '';
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
}', 'JsonSchema', 'fa-shield', 'JSON Threat Protection', NULL, FALSE, FALSE, FALSE);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto) VALUES ('LDAPAuthentication', 'Add LDAP Bind Authentication to your APIs, with username and password protection.', '{
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
}', 'JsonSchema', 'fa-database', 'LDAP Authentication Policy', NULL, TRUE, FALSE, FALSE);

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('HAL', 'The HAL policy rewrites currie-values from hal/json bodies.', '{
  "type": "object",
  "title": "HAL",
  "properties": {},
  "required": []
}', 'JsonSchema', 'fa-paw', 'HAL Policy', NULL ,TRUE ,FALSE ,FALSE );


-- from 0.8.1 to 0.9.0
ALTER TABLE public.gateways ADD jwt_priv_key TEXT NULL;

UPDATE gateways SET jwt_priv_key='-----BEGIN RSA PRIVATE KEY-----
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
-----END RSA PRIVATE KEY-----';

UPDATE gateways SET jwt_pub_key_endpoint = '/keys/pub';

UPDATE policydefs SET form = '{
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
}' WHERE id = 'RequestTransformer';

UPDATE policydefs set form = '{
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
}' WHERE id = 'RequestSizeLimiting';

-- Extra claim for rijksregisternummer DV
INSERT INTO key_mapping(from_spec_type, to_spec_type, from_spec_claim, to_spec_claim) VALUES ('SAML2', 'JWT', 'rrnr', 'rrnr');


------------------------------------------------------------------------------------------
--------------------------------------v0.8.2-SNAPSHOT-------------------------------------
------------------------------------------------------------------------------------------

ALTER TABLE services ADD COLUMN admin BOOL DEFAULT FALSE;
CREATE TABLE managed_application_keys AS SELECT managed_applications.id, managed_applications.api_key FROM managed_applications;
ALTER TABLE managed_application_keys ADD CONSTRAINT UK_managed_app_keys_1 UNIQUE (id, api_key);
ALTER TABLE managed_application_keys RENAME COLUMN id TO managed_app_id;
ALTER TABLE managed_application_keys ADD CONSTRAINT FK_managed_app_keys_1 FOREIGN KEY (managed_app_id) REFERENCES managed_applications(id);
CREATE INDEX IDX_managed_app_keys_1 ON managed_application_keys(managed_app_id);

INSERT INTO managed_applications(id, name, version, app_id, type, prefix, activated, restricted) VALUES (905, 'Admin Application', 'v1', 'adminapp', 'Admin', 'admin', TRUE, FALSE);

UPDATE policydefs SET form = '{
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
}' WHERE id = 'OAuth2';

ALTER TABLE policydefs ADD COLUMN form_override VARCHAR(4096) DEFAULT NULL;

UPDATE policydefs SET form_override = '[
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
]' WHERE id = 'OAuth2';

-- Fix for previous policy schemas persisting in view when current schema has empty properties
UPDATE policydefs SET form = '{
  "type": "object",
  "title": "JWT-Upstream",
  "properties": {
    "placeholder" :{}
  },
  "required": []
}' WHERE id = 'JWTUp';

UPDATE policydefs SET form = '{
  "type": "object",
  "title": "JWT Token",
  "properties": {
    "placeholder" :{}
  },
  "required": []
}' WHERE id = 'JWT';

UPDATE policydefs SET form = '{
  "type": "object",
  "title": "HAL Authentication",
  "properties": {
    "placeholder" :{}
  },
  "required": []
}' WHERE id = 'HAL';

ALTER TABLE policies ADD COLUMN enabled BOOL DEFAULT TRUE;

-- Small script in case we want to already support multiple gateways

--CREATE TABLE gateway_policies AS SELECT policies.id AS policy_id, policies.gateway_id, policies.kong_plugin_id, policies.enabled FROM policies;
--ALTER TABLE policies DROP COLUMN gateway_id;
--ALTER TABLE policies DROP COLUMN kong_plugin_id;
--ALTER TABLE policies DROP COLUMN enabled;
--ALTER TABLE gateway_policies ADD CONSTRAINT FK_gateway_policies_1 FOREIGN KEY (gateway_id) REFERENCES policies(id) ON UPDATE CASCADE;
--ALTER TABLE gateway_policies ADD CONSTRAINT UK_gateway_policies_1 UNIQUE (gateway_id, kong_plugin_id);
--CREATE INDEX IDX_gateway_policies_1 ON gateway_policies(gateway_id);

ALTER TABLE policydefs ADD COLUMN default_config VARCHAR(4096) DEFAULT NULL;

UPDATE policydefs SET default_config = '{"key_names":["apikey"],"hide_credentials":false}', form = '{
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
}' WHERE id = 'KeyAuthentication';

UPDATE policydefs SET default_config = '{"methods":["HEAD","DELETE","GET","POST","PUT","PATCH"],"credentials":false,"exposed_headers":[],"max_age":3600.0,"preflight_continue":false,"headers":["Accept","Accept-Version","Content-Length","Content-MD5","Content-Type","Date","apikey","Authorization"]}', form = '{
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
}', form_override = '[
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
]' WHERE id = 'CORS';

UPDATE policydefs SET form = '{
  "type": "object",
  "title": "ACL",
  "properties": {
    "placeholder": {}
  }
}', scope_auto = TRUE, scope_service = TRUE, icon = 'fa-users' WHERE id = 'ACL';

ALTER TABLE application_versions ADD COLUMN apikey VARCHAR(255) DEFAULT NULL;

UPDATE application_versions
SET apikey = contracts.apikey
FROM contracts
WHERE contracts.appv_id = application_versions.id;

-- Branding gateway URL's feature

CREATE TABLE brandings (id VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NUll);
ALTER TABLE brandings ADD PRIMARY KEY (id);
ALTER TABLE brandings ADD CONSTRAINT UK_brandings_1 UNIQUE (name);
CREATE INDEX IDX_brandings_1 ON brandings(id);

CREATE TABLE service_brandings (organization_id VARCHAR(255) NOT NULL, service_id VARCHAR(255) NOT NULL, branding_id VARCHAR(255) NOT NULL);
ALTER TABLE service_brandings ADD CONSTRAINT FK_service_brandings_1 FOREIGN KEY (service_id, organization_id) REFERENCES services(id, organization_id) ON UPDATE CASCADE;
ALTER TABLE service_brandings ADD CONSTRAINT FK_service_brandings_2 FOREIGN KEY (branding_id) REFERENCES brandings(id) ON UPDATE CASCADE;
ALTER TABLE service_brandings ADD CONSTRAINT UK_service_brandings_1 UNIQUE (service_id, branding_id);
CREATE INDEX IDX_service_brandings_1 ON service_brandings(organization_id, service_id);
CREATE INDEX IDX_service_brandings_2 ON service_brandings(branding_id);

-- UPDATE FROM 0.8.2 -> 0.9.3 --

ALTER TABLE managed_applications DROP COLUMN api_key;
ALTER TABLE contracts DROP COLUMN apikey;