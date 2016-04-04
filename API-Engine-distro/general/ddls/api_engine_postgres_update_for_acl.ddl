-- **********************************************
-- * Makes adjustments to tables for ACL policy *
-- **********************************************

ALTER TABLE policies ADD COLUMN kong_plugin_id VARCHAR(255) NULL;
ALTER TABLE policies ADD COLUMN contract_id BIGINT NULL;
INSERT INTO policydefs (id, description, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('ACL', 'Enable the service to work with an Access Control List', 'fa-acl', 'ACL Policy', NULL ,TRUE ,TRUE ,FALSE );