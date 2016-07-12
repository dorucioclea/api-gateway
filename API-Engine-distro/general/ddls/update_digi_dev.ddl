-- remove foreign key to availabilities and gateways
ALTER TABLE public.managed_applications DROP CONSTRAINT fk_67jdhkwjqd78t8kcsil9c3dk5;
ALTER TABLE public.managed_applications DROP CONSTRAINT fk_67jdhkwjqd78t8kcsil9c3dk6;

-- Update marketplace
UPDATE managed_applications SET name='Internal Marketplace' WHERE availability='int';
UPDATE managed_applications SET name='External Marketplace' WHERE availability='ext';
UPDATE managed_applications SET availability='pub' WHERE type='Publisher';

-- Add extra column context - PostgreSQL does not support adding a column with a default value in a single command line.
ALTER TABLE public.organizations ADD COLUMN context VARCHAR(255);
UPDATE public.organizations SET context = 'pub';
ALTER TABLE public.organizations ALTER COLUMN context SET DEFAULT 'pub';
ALTER TABLE public.organizations ALTER COLUMN context SET NOT NULL;

-- drop availabilities
DROP TABLE public.availabilities;

-- update managed apps
ALTER TABLE public.managed_applications ALTER COLUMN type TYPE VARCHAR(255);
ALTER TABLE public.managed_applications ALTER COLUMN type SET NOT NULL;

ALTER TABLE public.managed_applications ALTER COLUMN api_key TYPE VARCHAR(255);
ALTER TABLE public.managed_applications ALTER COLUMN api_key SET NOT NULL;

ALTER TABLE public.managed_applications RENAME availability TO prefix;
ALTER TABLE public.managed_applications ALTER COLUMN prefix TYPE VARCHAR(255);
ALTER TABLE public.managed_applications ALTER COLUMN prefix SET NOT NULL;

-- add activated
ALTER TABLE public.managed_applications ADD COLUMN activated BOOL;
UPDATE public.managed_applications SET activated = true;
ALTER TABLE public.managed_applications ALTER COLUMN activated SET DEFAULT true;
ALTER TABLE public.managed_applications ALTER COLUMN activated SET NOT NULL;

-- add restricted
ALTER TABLE public.managed_applications ADD COLUMN restricted BOOL;
UPDATE public.managed_applications SET restricted = false;
ALTER TABLE public.managed_applications ALTER COLUMN restricted SET DEFAULT false;
ALTER TABLE public.managed_applications ALTER COLUMN restricted SET NOT NULL;

-- add app_id to managed application bean
ALTER TABLE public.managed_applications ADD COLUMN app_id VARCHAR;

-- make prefix unique because will be used in org names
ALTER TABLE managed_applications ADD CONSTRAINT UK_managedapp_1 UNIQUE (prefix);

-- create generic key mapping table
CREATE TABLE key_mapping (from_spec_type VARCHAR(25) NOT NULL,to_spec_type VARCHAR(25) NOT NULL,from_spec_claim VARCHAR(255) NOT NULL,to_spec_claim VARCHAR(255) NULL);
ALTER TABLE key_mapping ADD CONSTRAINT pkkey_mapping PRIMARY KEY (from_spec_type, to_spec_type, from_spec_claim);

-- INSERT optional SAML to JWT mapping claims - we apply it in each environment in order to test - for Keycloak we'll add default values
INSERT INTO key_mapping(from_spec_type, to_spec_type, from_spec_claim, to_spec_claim) VALUES ('SAML2', 'JWT', 'profielId', 'profielId');
INSERT INTO key_mapping(from_spec_type, to_spec_type, from_spec_claim, to_spec_claim) VALUES ('SAML2', 'JWT', 'profielType', 'profielType');

-- Add contracts to registered applications: need to be able to tell on which gateway the policy and its plugin apply
ALTER TABLE public.policies ADD COLUMN gateway_id VARCHAR(255);
UPDATE policies SET type = 'Contract' WHERE type = 'Application';

-- delete all records
DELETE FROM managed_applications WHERE true;

-- reinit
INSERT INTO managed_applications (id, name, version, type, prefix, api_key, activated, restricted, app_id) VALUES (901, 'External Marketplace', 'v1', 'ExternalMarketplace', 'ext', '229e2ea08ba94919c9d221cdf3be1f73',TRUE,FALSE,'marketplace');
INSERT INTO managed_applications (id, name, version, type, prefix, api_key, activated, restricted, app_id) VALUES (902, 'Internal Marketplace', 'v1', 'InternalMarketplace', 'int', '229e2ea08ba94919c9d221cdf3be1f71',TRUE,FALSE,'marketplace');
INSERT INTO managed_applications (id, name, version, type, prefix, api_key, activated, restricted, app_id) VALUES (903, 'Publisher', 'v1', 'Publisher', 'pub', '***REMOVED***',TRUE,FALSE,'publisher');
INSERT INTO managed_applications (id, name, version, type, prefix, api_key, activated, restricted, app_id) VALUES (904, 'consent_app_nodejs', 'v1', 'Consent', 'acpaas', '229e2ea08ba94919c9d221cdf3be1124',TRUE,FALSE,'consent_app_nodejs');

-- update the policies to set the gateway id... because we only have one so far, set the same id for all policies
UPDATE public.policies SET gateway_id = 'KongGateway';

-- fix for wrong configurations
UPDATE policies SET configuration = '{"mandatory_scope":false,"token_expiration":7200,"enable_authorization_code":true,"enable_implicit_grant":true,"enable_client_credentials":true,"hide_credentials":false,"scopes":[]}' WHERE id = 1125 OR id = 1096 OR id = 1112;

----- v0.8.1-SNAPSHOT -----
INSERT INTO defaults (id, service_terms) VALUES ('DEV', 'This is a placeholder for the default terms & conditions\n========================================================\n');