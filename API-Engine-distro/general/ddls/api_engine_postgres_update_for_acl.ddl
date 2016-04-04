-- **********************************************
-- * Makes adjustments to tables for ACL policy *
-- **********************************************
CREATE TABLE managed_applications (id BIGINT NOT NULL, name VARCHAR(255) NOT NULL, version VARCHAR(255) NOT NULL, gateway_id VARCHAR(255) NULL, type VARCHAR(255) NULL, availability VARCHAR(255) NULL, gateway_username VARCHAR(255) NULL, api_key VARCHAR(255) NULL);

ALTER TABLE policies ADD COLUMN kong_plugin_id VARCHAR(255) NULL;

ALTER TABLE policies ADD COLUMN contract_id BIGINT NULL;

ALTER TABLE policies ADD COLUMN marketplace_id VARCHAR(255) NULL;

ALTER TABLE managed_applications ADD PRIMARY KEY (id);

ALTER TABLE managed_applications ADD CONSTRAINT FK_67jdhkwjqd78t8kcsil9c3dk1 FOREIGN KEY (gateway_id) REFERENCES gateways (id);

ALTER TABLE managed_applications ADD CONSTRAINT FK_67jdhkwjqd78t8kcsil9c3dk2 FOREIGN KEY (availability) REFERENCES availabilities (code) ;

INSERT INTO policydefs (id, description, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('ACL', 'Enable the service to work with an Access Control List', 'fa-acl', 'ACL Policy', NULL ,TRUE ,TRUE ,FALSE );

INSERT INTO managed_applications (id, name, version, type, availability, api_key) VALUES
  (1000, 'marketplace', 'v1', 'Marketplace', NULL, '6b8406cc81fe4ca3cc9cd4a0abfb97c2'),
  (1001, 'marketplace', 'v1', 'Marketplace', 'ext', '***REMOVED***'),
  (1002, 'marketplace', 'v1', 'Marketplace', 'int', '***REMOVED***'),
  (1003, 'dev.publisher', 'v1', 'Publisher', NULL, '***REMOVED***');
