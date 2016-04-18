--  ***********...
--  Update Data...
--  ***********...
--  ***********...

INSERT INTO gateways (id, configuration,endpoint, created_by, created_on, description, modified_by, modified_on, name, type, oauth_authorize, oauth_token, oauth_context,jwt_exp_time) VALUES ('KongGateway', '{"endpoint":"https://api-gwadmin-o.antwerpen.be","username":"","password":""}','https://api-gw-o.antwerpen.be', '', CURRENT_DATE, 'This is the gateway.', '', CURRENT_DATE, 'Default Kong Gateway', 'REST', '/oauth2/authorize', '/oauth2/token','/secure',7200);
INSERT INTO users (username, email, full_name, joined_on,admin,pic) VALUES ('admin', 'admin@example.org', 'Admin', CURRENT_DATE,TRUE ,NULL );
INSERT INTO organizations (id,description,name,created_by,created_on,modified_by,modified_on) VALUES ('Digipolis','Digipolis','Digipolis','admin',CURRENT_DATE,'admin',CURRENT_DATE);
INSERT INTO memberships (id,created_on, org_id, role_id, user_id) VALUES (999,CURRENT_DATE,'Digipolis','Owner','admin');

/*insert statements*/
ALTER TABLE availabilities ALTER COLUMN code TYPE VARCHAR(25);

INSERT INTO availabilities (code, name) VALUES ('acpaas', 'acpaas');
INSERT INTO availabilities(name, code) VALUES ('external', 'ext');
INSERT INTO availabilities(name, code) VALUES ('internal', 'int');

INSERT INTO white_ip_restriction(netw_value) VALUES ('192.168.0.0/16');
INSERT INTO white_ip_restriction(netw_value) VALUES ('10.0.0.0/8');
INSERT INTO white_ip_restriction(netw_value) VALUES ('172.0.0.0/8');

INSERT INTO users(username, kong_username, email, full_name, joined_on, admin)VALUES ('rc00115@digant.antwerpen.local', '','peter.claes@digipolis.be', 'Peter Claes', CURRENT_DATE, TRUE);
INSERT INTO users(username, kong_username, email, full_name, joined_on, admin)VALUES ('rc00260@digant.antwerpen.local', '','chris.jacobs@digipolis.be', 'Chris Jacobs', CURRENT_DATE, TRUE);
INSERT INTO users(username, kong_username, email, full_name, joined_on, admin)VALUES ('ex02393@digant.antwerpen.local', '','michallis.pashidis@digipolis.be', 'Michallis Pashidis', CURRENT_DATE, TRUE);
INSERT INTO users(username, kong_username, email, full_name, joined_on, admin)VALUES ('ex02394@digant.antwerpen.local', '','maarten.somers@digipolis.be', 'Maarten Somers', CURRENT_DATE, TRUE);
INSERT INTO users(username, kong_username, email, full_name, joined_on, admin)VALUES ('ex02607@digant.antwerpen.local', '','guillaume.vandecasteele@digipolis.be', 'Guillaume Vandecasteele', CURRENT_DATE, TRUE);

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
  (901, 'marketplace', 'v1', 'Marketplace', 'ext', '229e2ea08ba94919c9d221cdf3be1f73'),
  (902, 'marketplace', 'v1', 'Marketplace', 'int', '229e2ea08ba94919c9d221cdf3be1f71'),
  (903, 'dev.publisher', 'v1', 'Publisher', NULL, '***REMOVED***'),
  (904, 'consent_app_nodejs', 'v1', 'Consent', 'acpaas', '229e2ea08ba94919c9d221cdf3be1124');

UPDATE gateways SET oauth_token='/oauth2/token', oauth_authorize='/oauth2/authorize', oauth_context='/secure', jwt_exp_time=60 WHERE id='KongGateway';

ALTER TABLE users ADD CONSTRAINT UK_users_unique_email UNIQUE (email);
