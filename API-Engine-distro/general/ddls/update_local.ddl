-- delete all records
DELETE FROM managed_applications WHERE true;

-- reinit
INSERT INTO managed_applications (id, name, version, type, prefix, api_key, activated, restricted, app_id) VALUES (901, 'External Marketplace', 'v1', 'ExternalMarketplace', 'ext', '***REMOVED***',TRUE,FALSE,'marketplace');
INSERT INTO managed_applications (id, name, version, type, prefix, api_key, activated, restricted, app_id) VALUES (902, 'Internal Marketplace', 'v1', 'InternalMarketplace', 'int', '***REMOVED***',TRUE,FALSE,'marketplace');
INSERT INTO managed_applications (id, name, version, type, prefix, api_key, activated, restricted, app_id) VALUES (903, 'Publisher', 'v1', 'Publisher', 'pub', '***REMOVED***',TRUE,FALSE,'publisher');

-- update the policies to set the gateway id... because we only have one so far, set the same id for all policies
UPDATE public.policies SET gateway_id = 'KongGatewayLocal';