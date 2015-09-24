ALTER TABLE application_versions DROP COLUMN oauth_client_id;
ALTER TABLE application_versions DROP COLUMN oauth_client_secret;
ALTER TABLE application_versions DROP COLUMN oauth_client_redirect;

CREATE TABLE oauth_apps (id BIGINT NOT NULL, oauth_svc_orgid VARCHAR(255) NOT NULL, oauth_svc_id VARCHAR(255) NOT NULL,oauth_svc_version VARCHAR(255) NOT NULL,oauth_client_id VARCHAR(255) NOT NULL,oauth_client_secret VARCHAR(255) NOT NULL,oauth_client_redirect VARCHAR(255),app_id BIGINT NOT NULL);

ALTER TABLE oauth_apps ADD PRIMARY KEY (id);

ALTER TABLE oauth_apps ADD CONSTRAINT FK_l5q6we1bos1yl98nmogei7aja FOREIGN KEY (app_id) REFERENCES application_versions (id);

CREATE INDEX IDX_oauth_defs_1 ON oauth_apps(oauth_svc_orgid, oauth_svc_id, oauth_svc_version);