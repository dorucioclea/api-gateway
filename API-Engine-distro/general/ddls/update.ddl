CREATE TABLE svc_visibility (service_version_id BIGINT NOT NULL, code VARCHAR(255) NOT NULL, show BOOLEAN NOT NULL);

ALTER TABLE svc_visibility ADD PRIMARY KEY (service_version_id, code);

ALTER TABLE svc_visibility ADD CONSTRAINT FK_svc_version_visibility FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE applications ADD context VARCHAR(255) NULL;

CREATE TABLE availabilities (name VARCHAR(255) NOT NULL, code VARCHAR(3) NOT NULL);

CREATE TABLE white_ip_restriction (netw_value VARCHAR(255));

CREATE TABLE black_ip_restriction (netw_value VARCHAR(255));

ALTER TABLE availabilities ADD PRIMARY KEY (code);

ALTER TABLE white_ip_restriction ADD PRIMARY KEY (netw_value);

ALTER TABLE black_ip_restriction ADD PRIMARY KEY (netw_value);


/*insert statements*/
INSERT INTO availabilities(name, code) VALUES ('external', 'ext');

INSERT INTO availabilities(name, code) VALUES ('internal', 'int');

INSERT INTO white_ip_restriction(netw_value) VALUES ('127.0.0.0/24');

INSERT INTO black_ip_restriction(netw_value) VALUES ('32.0.0.0/8');


