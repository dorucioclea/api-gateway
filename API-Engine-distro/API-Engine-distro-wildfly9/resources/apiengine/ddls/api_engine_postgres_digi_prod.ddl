--  ***********...
--  Update Data...
--  ***********...
--  ***********...

INSERT INTO gateways (id, configuration,endpoint, created_by, created_on, description, modified_by, modified_on, name, type) VALUES ('KongGateway', '{"endpoint":"http://api-gw-p.antwerpen.be:8001","username":"","password":""}','https://api-gw-p.antwerpen.be', '', CURRENT_DATE, 'This is the gateway.', '', CURRENT_DATE, 'Default Kong Gateway', 'REST');

INSERT INTO users (username, email, full_name, joined_on,admin,pic) VALUES ('admin', 'admin@example.org', 'Admin', CURRENT_DATE,TRUE ,NULL );

INSERT INTO organizations (id,description,name,created_by,created_on,modified_by,modified_on) VALUES ('Digipolis','Digipolis','Digipolis','admin',CURRENT_DATE,'admin',CURRENT_DATE);

INSERT INTO memberships (id,created_on, org_id, role_id, user_id) VALUES (999,CURRENT_DATE,'Digipolis','Owner','admin');