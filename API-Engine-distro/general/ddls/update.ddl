-- db updates for next releas should come here
ALTER TABLE service_versions ADD COLUMN auto_accept_contracts BOOL DEFAULT TRUE;

ALTER TABLE organizations ADD COLUMN private BOOL DEFAULT TRUE;

CREATE TABLE events (id BIGINT NOT NULL, origin_id VARCHAR(255) NOT NULL, destination_id VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, body VARCHAR(4096));

ALTER TABLE events ADD PRIMARY KEY (id);

ALTER TABLE events ADD CONSTRAINT UK_events_1 UNIQUE (origin_id, destination_id, type);


-- mail templates
CREATE TABLE public.mail_templates (topic VARCHAR(255) NOT NULL,content TEXT NULL, subject TEXT NULL, created_on TIMESTAMP NULL,updated_on TIMESTAMP NULL) WITHOUT OIDS;
ALTER TABLE public.mail_templates ADD CONSTRAINT pksb_mail_templates PRIMARY KEY (topic);




-- INSERT default templates
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_REQUEST', 'API Engine ({environment}) - request membership for {orgFriendlyName} ({orgName})','The following user requests membership for your organization: {orgFriendlyName} ({orgName})\n- Username: {userId}\n- Email   : {userMail}\n\nYou can add the user in the Members-tab of your organization./n',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_APPROVE', 'API Engine ({environment}) - approve membership for {orgFriendlyName} ({orgName})', 'Your membership request for {orgFriendlyName} ({orgName}) has been granted.\n.',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_REJECT', 'API Engine ({environment}) - reject membership for {orgFriendlyName} ({orgName})', 'Your membership request for {orgFriendlyName} ({orgName}) has been rejected.\n.',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_GRANTED', 'API Engine ({environment}) - granted membership for {orgFriendlyName} ({orgName})', 'You have been granted for {orgFriendlyName} (orgName)',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_NEW', 'API Engine ({environment}) - Welcome to {orgFriendlyName} ({orgName})', 'You have been invited to {orgFriendlyName} (orgName)',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_DELETED', 'API Engine ({environment}) - Membership deleted for {orgFriendlyName} ({orgName})', 'Your membership has been deleted for {orgFriendlyName} (orgName)',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_UPDATE_ROLE', 'API Engine ({environment}) - Updated role ({role}) for {orgFriendlyName} ({orgName})', 'Your role for {orgFriendlyName} (orgName) is updated:\n- {role}',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_ADMIN_NEW', 'API Engine ({environment}) - Admin privileges assigned', 'You have been added as administrator for the API Engine',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_ADMIN_DELETED', 'API Engine ({environment}) - Admin privileges revoked', 'Your administrator privileges have been revoked',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('ORGANIZATION_TRANSFER', 'API Engine ({environment}) - Transfer ownership for {orgFriendlyName} ({orgName})', 'You have been assigned owner for {orgFriendlyName} (orgName).',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('CONTRACT_REQUEST', 'API Engine ({environment}) - Contract request for {serviceName}', 'The contract request for the application: {appName} with service:{serviceName} has been requested.\nYou will be notified with the result of your request.',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('CONTRACT_APPROVE', 'API Engine ({environment}) - Contract approved for {serviceName}', 'The contract request for the application: {appName} with service:{serviceName} has been approved.\nYour application keys are available on the API Store.',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('CONTRACT_REJECT', 'API Engine ({environment}) - Contract rejected for {serviceName}', 'The contract request for the application: {appName} with service:{serviceName} has been rejected.',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('STATUS', 'API Engine - status mail ({environment})','Not yet implemented.',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('TEST', 'API Engine server restart ({environment})','This is a test message - no further action required.',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('FOOTER', '', '\n\n\nGreetings from the APIe Team.\n\n\n  /~\\\n C oo\n _( ^)\n/   ~\\',CURRENT_DATE ,CURRENT_DATE );