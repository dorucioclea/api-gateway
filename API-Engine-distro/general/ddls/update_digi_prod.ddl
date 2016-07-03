-- delete all records
DELETE FROM managed_applications WHERE true;

-- reinit
INSERT INTO managed_applications (id, name, version, type, prefix, api_key, activated, restricted, app_id) VALUES (901, 'External Marketplace', 'v1', 'ExternalMarketplace', 'ext', '229e2ea08ba94919c9d221cdf3be1f73',TRUE,FALSE,'marketplace');
INSERT INTO managed_applications (id, name, version, type, prefix, api_key, activated, restricted, app_id) VALUES (902, 'Internal Marketplace', 'v1', 'InternalMarketplace', 'int', '229e2ea08ba94919c9d221cdf3be1f71',TRUE,FALSE,'marketplace');
INSERT INTO managed_applications (id, name, version, type, prefix, api_key, activated, restricted, app_id) VALUES (903, 'Publisher', 'v1', 'Publisher', 'pub', '***REMOVED***',TRUE,TRUE,'publisher');
INSERT INTO managed_applications (id, name, version, type, prefix, api_key, activated, restricted, app_id) VALUES (904, 'consent_app_nodejs', 'v1', 'Consent', 'acpaas', '229e2ea08ba94919c9d221cdf3be1124',TRUE,FALSE,'consent_app_nodejs');

-- INSERT optional SAML to JWT mapping claims
INSERT INTO key_mapping(from_spec_type, to_spec_type, from_spec_claim, to_spec_claim) VALUES ('SAML2', 'JWT', 'profielId', 'profielId');
INSERT INTO key_mapping(from_spec_type, to_spec_type, from_spec_claim, to_spec_claim) VALUES ('SAML2', 'JWT', 'profielType', 'profielType');