-- delete all records
DELETE FROM managed_applications WHERE true;

-- reinit
INSERT INTO managed_applications (id, name, version, type, prefix, api_key, activated, restricted) VALUES (901, 'Internal Marketplace', 'v1', 'Marketplace', 'ext', '***REMOVED***',TRUE,FALSE);
INSERT INTO managed_applications (id, name, version, type, prefix, api_key, activated, restricted) VALUES (902, 'External Marketplace', 'v1', 'Marketplace', 'int', '***REMOVED***',TRUE,FALSE);
INSERT INTO managed_applications (id, name, version, type, prefix, api_key, activated, restricted) VALUES (903, 'Publisher', 'v1', 'Publisher', 'pub', '***REMOVED***',TRUE,FALSE);
INSERT INTO managed_applications (id, name, version, type, prefix, api_key, activated, restricted) VALUES (904, 'consent_app_nodejs', 'v1', 'Consent', 'acpaas', '229e2ea08ba94919c9d221cdf3be1124',TRUE,FALSE);