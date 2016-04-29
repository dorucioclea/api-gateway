-- db updates for next releas should come here
ALTER TABLE service_versions ADD COLUMN auto_accept_contracts BOOL DEFAULT TRUE;

ALTER TABLE organizations ADD COLUMN private BOOL DEFAULT TRUE;

CREATE TABLE events (id BIGINT NOT NULL, origin_id VARCHAR(255) NOT NULL, destination_id VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, body VARCHAR(4096));

ALTER TABLE events ADD PRIMARY KEY (id);

ALTER TABLE events ADD CONSTRAINT UK_events_1 UNIQUE (origin_id, destination_id, type);


-- mail templates
CREATE TABLE public.mail_templates (topic VARCHAR(255) NOT NULL,content TEXT NULL, subject TEXT NULL, created_on TIMESTAMP NULL,updated_on TIMESTAMP NULL) WITHOUT OIDS;
ALTER TABLE public.mail_templates ADD CONSTRAINT pksb_mail_templates PRIMARY KEY (topic);




-- INSERT statements
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_REQUEST', 'API Engine server restart ({environment})','This is a test message - no further action required.',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_APPROVE', '', '',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_REJECT', '', '',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_UPDATE', '', '',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_ADMIN_UPDATE', '', '',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('CONTRACT_REQUEST', '', '',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('CONTRACT_APPROVE', '', '',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('CONTRACT_REJECT', '', '',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('STATUS', 'API Engine - status mail ({environment})','Not yet implemented.',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('TEST', 'API Engine server restart ({environment})','This is a test message - no further action required.',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('FOOTER', '', '\n\n\nGreetings from the APIe Team.\n\n\n  /~\\\n C oo\n _( ^)\n/   ~\\',CURRENT_DATE ,CURRENT_DATE );