CREATE TABLE svc_visibility (service_version_id BIGINT NOT NULL, code VARCHAR(255) NOT NULL, show BOOLEAN NOT NULL);

ALTER TABLE svc_visibility ADD PRIMARY KEY (service_version_id, code);

ALTER TABLE svc_visibility ADD CONSTRAINT FK_svc_version_visibility FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE applications ADD context VARCHAR(255) NULL;

CREATE TABLE availabilities (code VARCHAR(3) NOT NULL, name VARCHAR(255) NOT NULL);

CREATE TABLE white_ip_restriction (netw_value VARCHAR(255));

CREATE TABLE black_ip_restriction (netw_value VARCHAR(255));

ALTER TABLE availabilities ADD PRIMARY KEY (code);

ALTER TABLE white_ip_restriction ADD PRIMARY KEY (netw_value);

ALTER TABLE black_ip_restriction ADD PRIMARY KEY (netw_value);


/*insert statements*/
INSERT INTO availabilities(name, code) VALUES ('external', 'ext');

INSERT INTO availabilities(name, code) VALUES ('internal', 'int');

INSERT INTO white_ip_restriction(netw_value) VALUES ('127.0.0.0/24');

INSERT INTO black_ip_restriction(netw_value) VALUES ('32.0.0.0/8');

/*Update existing services to be available on the internal marketplace*/


/*Update OAuth2 policy*/
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


