--  ***********...
--  Update Data...
--  ***********...
--  ***********...

INSERT INTO gateways (id, configuration,endpoint, created_by, created_on, description, modified_by, modified_on, name, type) VALUES ('KongGateway', '{"endpoint":"http://devapim.t1t.be:8001","username":"","password":""}','https://devapim.t1t.be', '', CURRENT_DATE, 'This is the gateway.', '', CURRENT_DATE, 'Default Kong Gateway', 'REST');

INSERT INTO users (username, email, full_name, joined_on,admin,pic) VALUES ('admin', 'admin@example.org', 'Admin', CURRENT_DATE,TRUE ,NULL );

INSERT INTO organizations (id,description,name,created_by,created_on,modified_by,modified_on) VALUES ('Digipolis','Digipolis','Digipolis','admin',CURRENT_DATE,'admin',CURRENT_DATE);

INSERT INTO memberships (id,created_on, org_id, role_id, user_id) VALUES (999,CURRENT_DATE,'Digipolis','Owner','admin');

INSERT INTO users (username, email, full_name, joined_on,admin,pic) VALUES ('runscope', 'michallis@trust1team.com', 'runscope', CURRENT_DATE,TRUE ,NULL );

-- default to int??? for migration
ALTER TABLE applications ADD COLUMN context VARCHAR(255) NOT NULL DEFAULT '';

/*insert statements*/
INSERT INTO availabilities(name, code) VALUES ('external', 'ext');

INSERT INTO availabilities(name, code) VALUES ('internal', 'int');

INSERT INTO white_ip_restriction(netw_value) VALUES ('127.0.0.0/24');

INSERT INTO black_ip_restriction(netw_value) VALUES ('32.0.0.0/8');

INSERT INTO managed_applications (id, name, version, type, availability, api_key) VALUES
  (1000, 'marketplace', 'v1', 'Marketplace', NULL, '6b8406cc81fe4ca3cc9cd4a0abfb97c2'),
  (1001, 'marketplace', 'v1', 'Marketplace', 'ext', '***REMOVED***'),
  (1002, 'marketplace', 'v1', 'Marketplace', 'int', '***REMOVED***'),
  (1003, 'dev.publisher', 'v1', 'Publisher', NULL, '***REMOVED***');