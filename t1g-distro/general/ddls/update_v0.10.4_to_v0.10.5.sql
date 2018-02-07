CREATE TABLE application_version_domains (application_version_id BIGINT NOT NULL, domain VARCHAR(512) NOT NULL);
ALTER TABLE application_version_domains ADD CONSTRAINT fk_application_version_domains_1 FOREIGN KEY (application_version_id) REFERENCES application_versions (id);
ALTER TABLE application_version_domains ADD CONSTRAINT uk_application_version_domains_1 UNIQUE (domain);