CREATE TABLE svc_visibility (service_version_id BIGINT NOT NULL, code VARCHAR(255) NOT NULL, show BOOLEAN NOT NULL);

ALTER TABLE svc_visibility ADD PRIMARY KEY (service_version_id, code);

ALTER TABLE svc_visibility ADD CONSTRAINT FK_svc_version_visibility FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE applications ADD context VARCHAR(255) NULL;

/*update all applications with int*/
