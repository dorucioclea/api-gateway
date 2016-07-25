-- update for v0.8.1
ALTER TABLE service_versions ADD COLUMN terms_agreement_required BOOL DEFAULT FALSE;
ALTER TABLE service_versions ADD COLUMN readme TEXT NULL;
UPDATE service_versions SET readme = terms FROM services WHERE service_versions.service_org_id = services.organization_id AND service_versions.service_id = services.id AND services.terms NOTNULL;
UPDATE service_versions SET status = 'Created' WHERE terms_agreement_required = TRUE AND readme IS NULL AND status = 'Ready';
ALTER TABLE contracts ADD COLUMN terms_agreed BOOL DEFAULT FALSE;
CREATE TABLE defaults (id VARCHAR(255) NOT NULL, service_terms TEXT NULL);
ALTER TABLE defaults ADD PRIMARY KEY (id);