-- update for v0.8.1
-- fix missing contraints from 0.8.0
ALTER TABLE mail_templates ADD CONSTRAINT pksb_mail_templates PRIMARY KEY (topic);
ALTER TABLE events ADD PRIMARY KEY (id);
ALTER TABLE followers ADD PRIMARY KEY (ServiceBean_id,ServiceBean_organization_id,user_id);
ALTER TABLE endpoint_properties ADD PRIMARY KEY (service_version_id, name);
ALTER TABLE svc_gateways ADD PRIMARY KEY (service_version_id, gateway_id);
ALTER TABLE white_ip_restriction ADD PRIMARY KEY (netw_value);
ALTER TABLE black_ip_restriction ADD PRIMARY KEY (netw_value);
ALTER TABLE svc_plans ADD PRIMARY KEY (service_version_id, plan_id, version);
ALTER TABLE svc_visibility ADD PRIMARY KEY (service_version_id, code);
ALTER TABLE application_versions ADD PRIMARY KEY (id);
ALTER TABLE applications ADD PRIMARY KEY (id, organization_id);
ALTER TABLE auditlog ADD PRIMARY KEY (id);
ALTER TABLE announcements ADD PRIMARY KEY (id);
ALTER TABLE contracts ADD PRIMARY KEY (id);
ALTER TABLE gateways ADD PRIMARY KEY (id);
ALTER TABLE memberships ADD PRIMARY KEY (id);
ALTER TABLE organizations ADD PRIMARY KEY (id);
ALTER TABLE plan_versions ADD PRIMARY KEY (id);
ALTER TABLE plans ADD PRIMARY KEY (id, organization_id);
ALTER TABLE plugins ADD PRIMARY KEY (id);
ALTER TABLE policies ADD PRIMARY KEY (id);
ALTER TABLE oauth_apps ADD PRIMARY KEY (id);
ALTER TABLE policydefs ADD PRIMARY KEY (id);
ALTER TABLE roles ADD PRIMARY KEY (id);
ALTER TABLE service_defs ADD PRIMARY KEY (id);
ALTER TABLE service_versions ADD PRIMARY KEY (id);
ALTER TABLE services ADD PRIMARY KEY (id, organization_id);
ALTER TABLE users ADD PRIMARY KEY (username);
ALTER TABLE support ADD PRIMARY KEY (id);
ALTER TABLE support_comments ADD PRIMARY KEY (id);
ALTER TABLE managed_applications ADD PRIMARY KEY (id);

ALTER TABLE managed_applications ADD CONSTRAINT FK_67jdhkwjqd78t8kcsil9c3dk5 FOREIGN KEY (gateway_id) REFERENCES gateways (id) ON UPDATE CASCADE;
ALTER TABLE services ADD CONSTRAINT FK_31hj3xmhp1wedxjh5bklnlg15 FOREIGN KEY (organization_id) REFERENCES organizations (id) ON UPDATE CASCADE;
ALTER TABLE contracts ADD CONSTRAINT FK_6h06sgs4dudh1wehmk0us973g FOREIGN KEY (appv_id) REFERENCES application_versions (id) ON UPDATE CASCADE;
ALTER TABLE service_defs ADD CONSTRAINT FK_81fuw1n8afmvpw4buk7l4tyxk FOREIGN KEY (service_version_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE application_versions ADD CONSTRAINT FK_8epnoby31bt7xakegakigpikp FOREIGN KEY (app_id, app_org_id) REFERENCES applications (id, organization_id) ON UPDATE CASCADE;
ALTER TABLE contracts ADD CONSTRAINT FK_8o6t1f3kg96rxy5uv51f6k9fy FOREIGN KEY (svcv_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE service_versions ADD CONSTRAINT FK_92erjg9k1lni97gd87nt6tq37 FOREIGN KEY (service_id, service_org_id) REFERENCES services (id, organization_id) ON UPDATE CASCADE;
ALTER TABLE endpoint_properties ADD CONSTRAINT FK_gn0ydqur10sxuvpyw2jvv4xxb FOREIGN KEY (service_version_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE applications ADD CONSTRAINT FK_jenpu34rtuncsgvtw0sfo8qq9 FOREIGN KEY (organization_id) REFERENCES organizations (id) ON UPDATE CASCADE;
ALTER TABLE policies ADD CONSTRAINT FK_l4q6we1bos1yl9unmogei6aja FOREIGN KEY (definition_id) REFERENCES policydefs (id) ON UPDATE CASCADE;
ALTER TABLE oauth_apps ADD CONSTRAINT FK_l5q6we1bos1yl98nmogei7aja FOREIGN KEY (app_id) REFERENCES application_versions (id) ON UPDATE CASCADE;
ALTER TABLE plans ADD CONSTRAINT FK_lwhc7xrdbsun1ak2uvfu0prj8 FOREIGN KEY (organization_id) REFERENCES organizations (id) ON UPDATE CASCADE;
ALTER TABLE contracts ADD CONSTRAINT FK_nyw8xu6m8cx4rwwbtrxbjneui FOREIGN KEY (planv_id) REFERENCES plan_versions (id) ON UPDATE CASCADE;
ALTER TABLE svc_gateways ADD CONSTRAINT FK_p5dm3cngljt6yrsnvc7uc6a75 FOREIGN KEY (service_version_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE permissions ADD CONSTRAINT FK_sq51ihfrapwdr98uufenhcocg FOREIGN KEY (role_id) REFERENCES roles (id) ON UPDATE CASCADE;
ALTER TABLE svc_plans ADD CONSTRAINT FK_t7uvfcsswopb9kh8wpa86blqr FOREIGN KEY (service_version_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE svc_visibility ADD CONSTRAINT FK_svc_version_visibility FOREIGN KEY (service_version_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE plan_versions ADD CONSTRAINT FK_tonylvm2ypnq3efxqr1g0m9fs FOREIGN KEY (plan_id, plan_org_id) REFERENCES plans (id, organization_id) ON UPDATE CASCADE;
ALTER TABLE followers ADD CONSTRAINT FK_29hj3xmhp1wedxjh1bklnlg15 FOREIGN KEY (ServiceBean_id,ServiceBean_organization_id) REFERENCES services (id,organization_id) ON UPDATE CASCADE;

-- fix missing config
ALTER TABLE policies ADD COLUMN gateway_id VARCHAR(255) NULL;

-- terms and agreement
ALTER TABLE service_versions ADD COLUMN terms_agreement_required BOOL DEFAULT FALSE;
ALTER TABLE service_versions ADD COLUMN readme TEXT NULL;
UPDATE service_versions SET readme = terms FROM services WHERE service_versions.service_org_id = services.organization_id AND service_versions.service_id = services.id AND services.terms NOTNULL;
UPDATE service_versions SET status = 'Created' WHERE terms_agreement_required = TRUE AND readme IS NULL AND status = 'Ready';
ALTER TABLE contracts ADD COLUMN terms_agreed BOOL DEFAULT FALSE;
CREATE TABLE defaults (id VARCHAR(255) NOT NULL, service_terms TEXT NULL);
ALTER TABLE defaults ADD PRIMARY KEY (id);

-- add config in db
CREATE TABLE config(id BIGINT NOT NULL, config_path VARCHAR(255) NOT NULL);
ALTER TABLE config ADD PRIMARY KEY (id);
INSERT INTO config(id,config_path) VALUES (7,'/opt/wildfly/standalone/configuration/application.conf');

-- update gateway (remove oauth2 endpoints, add jwt/oauth expiration time)
ALTER TABLE gateways DROP COLUMN oauth_authorize;
ALTER TABLE gateways DROP COLUMN oauth_token;
ALTER TABLE gateways DROP COLUMN oauth_context;

ALTER TABLE gateways ADD COLUMN oauth_exp_time INT NULL DEFAULT 7200;
ALTER TABLE gateways ADD COLUMN jwt_pub_key TEXT NULL DEFAULT '';

UPDATE gateways SET jwt_pub_key='-----BEGIN PUBLIC KEY-----
MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAjmrg7sFxRdobSZHI2Zjk
nrpFT/QrXDpYzUU8IMa4TOkgERtZ3OBlZdmcbyufpBn52fX9XEeH9TuB919cPxBE
zJ7CsReS+Wqpy9PSw+pmCiCfjHflud2uw50neX8eJxYtzHC7UN8+uA8oCKjw0I3P
+ECa7aW/DmMcI/5Osrixe7fPzv8CEzhTbw7A96nK2+VI/UqFWf1oDswlX8POhzLE
iuj7xBiubjl6N4DZnQyao8S2EgfPONJ4mrIn6TD071/tOMh1GYAwJpVCv3agRQWG
8MilaayrC4Z53k6dKWQS6IfU7w5bgB1+hgIzph+NMo7VY4NbJX96uoD7AoiB4o66
rS1jCKKyDqL0M90C1Hh7+R+yMhIkFdEGCKFGh3fl9UDGJ4FDTmo4du0CqnmwmjoV
fRdtyn+61ADxP6zd7n1LAqyPB4EkxukQ77K/ONLpRv2trft9oSUR1jWMq7w12WYr
hYVxOGSo5N4EGBJjHAyQgMCS8PXgm7N9XaNukes0YCyAL9XSBCE3n4T4BOWG9D2B
CD/zUvn5CFywJhug9Rw4LWt0o2GayiN3yH0pdXAsjSFTb7VivpOsW0/y6iGf0BjK
T8yXJEo8oPp4H2IuL4xL48mntBnVjPsItnziGCjqgHB7lqb7qyu/6+xtHgLlFoc2
0KBvSaDFYbbEtO4NFVrMuIECAwEAAQ==
-----END PUBLIC KEY-----';