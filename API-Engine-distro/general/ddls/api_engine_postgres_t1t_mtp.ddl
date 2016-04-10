--  ***********...
--  Update Data...
--  ***********...
--  ***********...

INSERT INTO gateways (id, configuration,endpoint, created_by, created_on, description, modified_by, modified_on, name, type, oauth_authorize, oauth_token, oauth_context,jwt_exp_time) VALUES ('KongGateway', '{"endpoint":"http://apim.t1t.be:8001","username":"","password":""}','https://apim.t1t.be:443', '', CURRENT_DATE, 'This is the gateway.', '', CURRENT_DATE, 'Default Kong Gateway', 'REST', '/oauth2/authorize', '/oauth2/token','/secure',7200);

INSERT INTO users (username, email, full_name, joined_on,admin,pic) VALUES ('admin', 'admin.be@trust1team.com', 'Admin', CURRENT_DATE,TRUE ,NULL );

INSERT INTO organizations (id,description,name,created_by,created_on,modified_by,modified_on) VALUES ('Trust1Team','Trust1Team bvba','Trust1Team','admin',CURRENT_DATE,'admin',CURRENT_DATE);

INSERT INTO memberships (id,created_on, org_id, role_id, user_id) VALUES (999,CURRENT_DATE,'Digipolis','Owner','admin');

INSERT INTO availabilities(name, code) VALUES ('external', 'ext');
INSERT INTO availabilities(name, code) VALUES ('internal', 'int');