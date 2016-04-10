CREATE TABLE svc_visibility (service_version_id BIGINT NOT NULL, code VARCHAR(255) NOT NULL, show BOOLEAN NOT NULL);

ALTER TABLE svc_visibility ADD PRIMARY KEY (service_version_id, code);

ALTER TABLE svc_visibility ADD CONSTRAINT FK_svc_version_visibility FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE applications ADD context VARCHAR(255) NOT NULL DEFAULT 'int';

CREATE TABLE availabilities (code VARCHAR(3) NOT NULL, name VARCHAR(255) NOT NULL);

CREATE TABLE white_ip_restriction (netw_value VARCHAR(255));

CREATE TABLE black_ip_restriction (netw_value VARCHAR(255));

ALTER TABLE availabilities ADD PRIMARY KEY (code);

ALTER TABLE white_ip_restriction ADD PRIMARY KEY (netw_value);

ALTER TABLE black_ip_restriction ADD PRIMARY KEY (netw_value);


/*insert statements*/
INSERT INTO availabilities(name, code) VALUES ('external', 'ext');

INSERT INTO availabilities(name, code) VALUES ('internal', 'int');

INSERT INTO white_ip_restriction(netw_value) VALUES ('192.168.0.0/16');

INSERT INTO white_ip_restriction(netw_value) VALUES ('10.0.0.0/8');

INSERT INTO white_ip_restriction(netw_value) VALUES ('172.0.0.0/8');

/*Update existing services to be available on the internal marketplace*/


/*Update OAuth2 policy to avoid required fields*/
UPDATE policydefs set form='{
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
      "default": 7200,
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

--update iprestriction to work exlusive
UPDATE policydefs set form='{
  "type": "object",
  "title": "IP Restriction",
  "properties": {
    "blacklist": {
        "type": "array",
        "items":{
               "type": "string",
               "pattern": "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\\/(\\d|[1-2]\\d|3[0-2]))?$",
                "description": "List of IPs or CIDR ranges to blacklist. You cannot set blacklist values if you have already whitelist values specified!",
                "validationMessage":"IP or CIDR required"
        }
    },
    "whitelist": {
        "type": "array",
        "items":{
            "type": "string",
            "pattern": "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\\/(\\d|[1-2]\\d|3[0-2]))?$",
            "description": "List of IPs or CIDR ranges to whitelist. You cannot set whitelist values if you have already blacklist values specified.",
            "validationMessage":"IP or CIDR required"
        }
    }
  }
}' WHERE id = 'IPRestriction';

--get ids of services select id from service_versions;
--update for existing services INSERT INTO svc_visibility(service_version_id, code, show) VALUES (1173, 'INT', true);

-- Updates necessary for deprecation/friendly name/ACL features

ALTER TABLE organizations ADD COLUMN friendly_name VARCHAR(255) NULL;

ALTER TABLE service_versions ADD COLUMN deprecated_on TIMESTAMP WITHOUT TIME ZONE NULL;

CREATE TABLE managed_applications (id BIGINT NOT NULL, name VARCHAR(255) NOT NULL, version VARCHAR(255) NOT NULL, gateway_id VARCHAR(255) NULL, type VARCHAR(255) NULL, availability VARCHAR(255) NULL, gateway_username VARCHAR(255) NULL, api_key VARCHAR(255) NULL);

ALTER TABLE policies ADD COLUMN kong_plugin_id VARCHAR(255) NULL;

ALTER TABLE policies ADD COLUMN contract_id BIGINT NULL;

ALTER TABLE managed_applications ADD PRIMARY KEY (id);

ALTER TABLE gateways ADD COLUMN aouth_token VARCHAR(255) NULL;
ALTER TABLE gateways ADD COLUMN oauth_authorize VARCHAR(255) NULL;
ALTER TABLE gateways ADD COLUMN oauth_context VARCHAR(255) NULL;
ALTER TABLE gateways ADD COLUMN jwt_exp_time BIGINT NULL DEFAULT 7200;

ALTER TABLE managed_applications ADD CONSTRAINT FK_67jdhkwjqd78t8kcsil9c3dk1 FOREIGN KEY (gateway_id) REFERENCES gateways (id);

ALTER TABLE managed_applications ADD CONSTRAINT FK_67jdhkwjqd78t8kcsil9c3dk2 FOREIGN KEY (availability) REFERENCES availabilities (code) ;

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('ACL', 'Enable the service to work with an Access Control List', '{
  "type": "object",
  "title": "ACL",
  "properties": {
    "group": {
      "title": "ACL group name",
      "description":"Name of the ACL group belonging to the service",
      "type": "string",
      "required": true
    }
  },
  "required": [
    "group"
  ]
}', 'JsonSchema', 'fa-acl', 'ACL Policy', NULL ,FALSE ,FALSE ,FALSE );