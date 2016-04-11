--  ***********...
--  Update Data...
--  ***********...
--  ***********...

INSERT INTO gateways (id, configuration,endpoint, created_by, created_on, description, modified_by, modified_on, name, type, oauth_authorize, oauth_token, oauth_context,jwt_exp_time) VALUES ('KongGatewayLocal', '{"endpoint":"http://devapim.t1t.be:8001","username":"","password":""}','https://devapim.t1t.be', '', CURRENT_DATE, 'This is the gateway.', '', CURRENT_DATE, 'Default Kong Gateway', 'REST', '/oauth2/authorize', '/oauth2/token','/secured',7200);

INSERT INTO users (username, email, full_name, joined_on,admin,pic) VALUES ('admin', 'admin@example.org', 'Admin', CURRENT_DATE,TRUE ,NULL );

INSERT INTO organizations (id,description,name,created_by,created_on,modified_by,modified_on) VALUES ('Digipolis','Digipolis','Digipolis','admin',CURRENT_DATE,'admin',CURRENT_DATE);

INSERT INTO memberships (id,created_on, org_id, role_id, user_id) VALUES (999,CURRENT_DATE,'Digipolis','Owner','admin');

INSERT INTO users (username, email, full_name, joined_on,admin,pic) VALUES ('runscope', 'michallis@trust1team.com', 'runscope', CURRENT_DATE,TRUE ,NULL );

-- default to int??? for migration
-- ALTER TABLE applications ADD COLUMN context VARCHAR(255) NOT NULL DEFAULT '';

/*insert statements*/
INSERT INTO availabilities(name, code) VALUES ('external', 'ext');
INSERT INTO availabilities(name, code) VALUES ('internal', 'int');
INSERT INTO white_ip_restriction(netw_value) VALUES ('127.0.0.0/24');
INSERT INTO black_ip_restriction(netw_value) VALUES ('32.0.0.0/8');

INSERT INTO users(username, kong_username, email, full_name, joined_on, admin)VALUES ('guillaume@trust1team.com', '','guillaume@trust1team.com', 'Guillaume Vandecasteele', CURRENT_DATE, TRUE);
INSERT INTO users(username, kong_username, email, full_name, joined_on, admin)VALUES ('michallis@trust1team.com', '','michallis@trust1team.com', 'Michallis Pashidis', CURRENT_DATE, TRUE);
INSERT INTO users(username, kong_username, email, full_name, joined_on, admin)VALUES ('maarten.somers@trust1team.com', '','maarten.somers@trust1team.com', 'Maarten Somers', CURRENT_DATE, TRUE);

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

INSERT INTO managed_applications (id, name, version, type, availability, api_key) VALUES
  (1001, 'marketplace', 'v1', 'Marketplace', 'ext', '***REMOVED***'),
  (1002, 'marketplace', 'v1', 'Marketplace', 'int', '***REMOVED***'),
  (1003, 'dev.publisher', 'v1', 'Publisher', NULL, '***REMOVED***');

UPDATE gateways SET oauth_token='/oauth2/token', oauth_authorize='/oauth2/authorize', oauth_context='/secure', jwt_exp_time=60 WHERE id='KongGatewayLocal';