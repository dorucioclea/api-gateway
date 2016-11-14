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