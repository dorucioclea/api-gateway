ALTER TABLE oauth_scopes ADD COLUMN oauth_scopes_desc VARCHAR(255);

UPDATE policydefs SET form='{
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
                "description": "Provide the scope identifier that will be available to the end user."
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
    "enable_password_grant": {
      "title": "Enable Resource Owner Password Grant",
      "type": "boolean",
      "default": false,
      "description": "An optional boolean value to enable the Resource Owner Password Credentials Grant flow (RFC 6742 Section 4.3)."
    },
    "hide_credentials": {
      "title": "Hide credentials",
      "type": "boolean",
      "default": false,
      "description": "An optional boolean value telling the plugin to hide the credential to the upstream API server. It will be removed by Kong before proxying the request."
    }
  },
  "required": [
    "mandatory_scope",
    "token_expiration",
    "enable_implicit_grant"
  ]
}' WHERE id='OAuth2';

ALTER TABLE service_versions ADD COLUMN onlinedoc VARCHAR(255);



ALTER TABLE services ADD COLUMN terms TEXT;

CREATE TABLE followers (ServiceBean_id VARCHAR(255) NOT NULL, ServiceBean_organization_id VARCHAR(255) NOT NULL, user_id VARCHAR(255) NOT NULL);

ALTER TABLE followers ADD PRIMARY KEY (ServiceBean_id,ServiceBean_organization_id,user_id);

ALTER TABLE followers ADD CONSTRAINT FK_29hj3xmhp1wedxjh1bklnlg15 FOREIGN KEY (ServiceBean_id,ServiceBean_organization_id) REFERENCES services (id,organization_id);

CREATE INDEX IDX_FK_followers_a ON followers(ServiceBean_id,ServiceBean_organization_id);


CREATE TABLE announcements (id BIGINT NOT NULL,organization_id VARCHAR(255) NOT NULL, service_id VARCHAR(255) NOT NULL, title VARCHAR(255) NOT NULL, description TEXT,created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, created_by VARCHAR(255) NOT NULL);

ALTER TABLE announcements ADD PRIMARY KEY (id);

CREATE INDEX IDX_announcements_1 ON announcements(created_by);

CREATE INDEX IDX_announcements_2 ON announcements(organization_id,service_id);


CREATE TABLE support (id BIGINT NOT NULL,organization_id VARCHAR(255) NOT NULL, service_id VARCHAR(255) NOT NULL, title VARCHAR(255) NOT NULL,status VARCHAR(255) NOT NULL, description TEXT,created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, created_by VARCHAR(255) NOT NULL, total_comments INT);

CREATE TABLE support_comments (id BIGINT NOT NULL, support_id BIGINT NOT NULL, comment TEXT,created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, created_by VARCHAR(255) NOT NULL);

ALTER TABLE support ADD PRIMARY KEY (id);

ALTER TABLE support_comments ADD PRIMARY KEY (id);

CREATE INDEX IDX_support_1 ON support(organization_id,service_id);

CREATE INDEX IDX_support_comments_1 ON support(id);




INSERT INTO gateways (id, configuration,endpoint, created_by, created_on, description, modified_by, modified_on, name, type) VALUES ('KongGateway', '{"endpoint":"http://rasu076.rte.antwerpen.local:8001","username":"","password":""}','https://rasu076.rte.antwerpen.local:443', '', '2015-08-18 17:56:58.083', 'This is the gateway.', '', '2015-08-18 17:56:58.083', 'Default Kong Gateway', 'REST');

INSERT INTO gateways (id, configuration,endpoint, created_by, created_on, description, modified_by, modified_on, name, type) VALUES ('KongGateway', '{"endpoint":"http://apim.t1t.be:8001","username":"","password":""}','https://apim.t1t.be:8443', '', '2015-08-18 17:56:58.083', 'This is the gateway.', '', '2015-08-18 17:56:58.083', 'Default Kong Gateway', 'REST');

INSERT INTO gateways (id, configuration,endpoint, created_by, created_on, description, modified_by, modified_on, name, type) VALUES ('KongGateway', '{"endpoint":"http://devasu018.dev.digant.antwerpen.local:8001","username":"","password":""}','https://devasu018.dev.digant.antwerpen.local:443', '', '2015-08-18 17:56:58.083', 'This is the gateway.', '', '2015-08-18 17:56:58.083', 'Default Kong Gateway', 'REST');