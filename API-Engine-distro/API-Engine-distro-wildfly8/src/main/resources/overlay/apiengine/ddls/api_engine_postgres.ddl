--  ***********...
--  Update Data...
--  ***********...
--  ***********...


CREATE SEQUENCE hibernate_sequence START WITH 999;

CREATE TABLE application_versions (id BIGINT NOT NULL, created_by VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, modified_by VARCHAR(255) NOT NULL, modified_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, published_on TIMESTAMP WITHOUT TIME ZONE NULL, retired_on TIMESTAMP WITHOUT TIME ZONE NULL, status VARCHAR(255) NOT NULL, version VARCHAR(255) NOT NULL, app_id VARCHAR(255) NULL, app_org_id VARCHAR(255) NULL, oauth_client_id VARCHAR(255), oauth_client_secret VARCHAR(255), oauth_client_redirect VARCHAR(255));

CREATE TABLE applications (id VARCHAR(255) NOT NULL, created_by VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, description VARCHAR(512) NULL, name VARCHAR(255) NOT NULL, organization_id VARCHAR(255) NOT NULL, logo OID);

CREATE TABLE auditlog (id BIGINT NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, data TEXT NULL, entity_id VARCHAR(255) NULL, entity_type VARCHAR(255) NOT NULL, entity_version VARCHAR(255) NULL, organization_id VARCHAR(255) NOT NULL, what VARCHAR(255) NOT NULL, who VARCHAR(255) NOT NULL);

CREATE TABLE contracts (id BIGINT NOT NULL, apikey VARCHAR(255) NOT NULL, created_by VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, appv_id BIGINT NULL, planv_id BIGINT NULL, svcv_id BIGINT NULL);

CREATE TABLE endpoint_properties (service_version_id BIGINT NOT NULL, value VARCHAR(255) NULL, name VARCHAR(255) NOT NULL);

CREATE TABLE gateways (id VARCHAR(255) NOT NULL, configuration TEXT NOT NULL, endpoint VARCHAR(255) NOT NULL , created_by VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, description VARCHAR(512) NULL, modified_by VARCHAR(255) NOT NULL, modified_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, name VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL);

CREATE TABLE memberships (id BIGINT NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NULL, org_id VARCHAR(255) NULL, role_id VARCHAR(255) NULL, user_id VARCHAR(255) NULL);

CREATE TABLE organizations (id VARCHAR(255) NOT NULL, created_by VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, description VARCHAR(512) NULL, modified_by VARCHAR(255) NOT NULL, modified_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, name VARCHAR(255) NOT NULL);

CREATE TABLE permissions (role_id VARCHAR(255) NOT NULL, permissions INT NULL);

CREATE TABLE plan_versions (id BIGINT NOT NULL, created_by VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, locked_on TIMESTAMP WITHOUT TIME ZONE NULL, modified_by VARCHAR(255) NOT NULL, modified_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, status VARCHAR(255) NOT NULL, version VARCHAR(255) NOT NULL, plan_id VARCHAR(255) NULL, plan_org_id VARCHAR(255) NULL);

CREATE TABLE plans (id VARCHAR(255) NOT NULL, created_by VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, description VARCHAR(512) NULL, name VARCHAR(255) NOT NULL, organization_id VARCHAR(255) NOT NULL);

CREATE TABLE plugins (id BIGINT NOT NULL, artifact_id VARCHAR(255) NOT NULL, classifier VARCHAR(255) NULL, created_by VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, description VARCHAR(512) NULL, group_id VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NULL, type VARCHAR(255) NULL, version VARCHAR(255) NOT NULL);

CREATE TABLE policies (id BIGINT NOT NULL, configuration TEXT NULL, created_by VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, entity_id VARCHAR(255) NOT NULL, entity_version VARCHAR(255) NOT NULL, modified_by VARCHAR(255) NOT NULL, modified_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, name VARCHAR(255) NOT NULL, order_index INT NOT NULL, organization_id VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, definition_id VARCHAR(255) NOT NULL);

CREATE TABLE oauth_apps (id BIGINT NOT NULL, oauth_svc_orgid VARCHAR(255) NOT NULL, oauth_svc_id VARCHAR(255) NOT NULL,oauth_svc_version VARCHAR(255) NOT NULL,oauth_client_id VARCHAR(255) NOT NULL,oauth_client_secret VARCHAR(255) NOT NULL,oauth_client_redirect VARCHAR(255),app_id BIGINT NOT NULL);

CREATE TABLE policydefs (id VARCHAR(255) NOT NULL, description VARCHAR(512) NOT NULL, form VARCHAR(4096) NULL, form_type VARCHAR(255) NULL, icon VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NULL, plugin_id BIGINT NULL, scope_service BOOL, scope_plan BOOL, scope_auto BOOL);

CREATE TABLE roles (id VARCHAR(255) NOT NULL, auto_grant BOOLEAN NULL, created_by VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, description VARCHAR(512) NULL, name VARCHAR(255) NULL);

CREATE TABLE service_defs (id BIGINT NOT NULL, data OID, service_version_id BIGINT NULL);

CREATE TABLE service_versions (id BIGINT NOT NULL, created_by VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, definition_type VARCHAR(255) NULL, endpoint VARCHAR(255) NULL, endpoint_type VARCHAR(255) NULL, modified_by VARCHAR(255) NOT NULL, modified_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, public_service BOOLEAN NOT NULL, published_on TIMESTAMP WITHOUT TIME ZONE NULL, retired_on TIMESTAMP WITHOUT TIME ZONE NULL, status VARCHAR(255) NOT NULL, version VARCHAR(255) NULL, service_id VARCHAR(255) NULL, service_org_id VARCHAR(255) NULL, provision_key VARCHAR(255), onlinedoc VARCHAR(255));

CREATE TABLE services (id VARCHAR(255) NOT NULL, created_by VARCHAR(255) NOT NULL, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, description VARCHAR(512) NULL, name VARCHAR(255) NOT NULL, basepath VARCHAR(255) NOT NULL, organization_id VARCHAR(255) NOT NULL,terms TEXT , logo OID);

CREATE TABLE svc_gateways (service_version_id BIGINT NOT NULL, gateway_id VARCHAR(255) NOT NULL);

CREATE TABLE svc_plans (service_version_id BIGINT NOT NULL, plan_id VARCHAR(255) NOT NULL, version VARCHAR(255) NOT NULL);

CREATE TABLE users (username VARCHAR(255) NOT NULL, email VARCHAR(255) NULL, full_name VARCHAR(255) NULL, joined_on TIMESTAMP WITHOUT TIME ZONE NULL, admin BOOL DEFAULT FALSE,company VARCHAR(255),location VARCHAR(255),website VARCHAR(255),bio TEXT, pic OID );

ALTER TABLE endpoint_properties ADD PRIMARY KEY (service_version_id, name);

ALTER TABLE svc_gateways ADD PRIMARY KEY (service_version_id, gateway_id);

ALTER TABLE svc_plans ADD PRIMARY KEY (service_version_id, plan_id, version);

ALTER TABLE application_versions ADD PRIMARY KEY (id);

ALTER TABLE applications ADD PRIMARY KEY (id, organization_id);

ALTER TABLE auditlog ADD PRIMARY KEY (id);

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

ALTER TABLE services ADD CONSTRAINT FK_31hj3xmhp1wedxjh5bklnlg15 FOREIGN KEY (organization_id) REFERENCES organizations (id);

ALTER TABLE contracts ADD CONSTRAINT FK_6h06sgs4dudh1wehmk0us973g FOREIGN KEY (appv_id) REFERENCES application_versions (id);

ALTER TABLE service_defs ADD CONSTRAINT FK_81fuw1n8afmvpw4buk7l4tyxk FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE application_versions ADD CONSTRAINT FK_8epnoby31bt7xakegakigpikp FOREIGN KEY (app_id, app_org_id) REFERENCES applications (id, organization_id);

ALTER TABLE contracts ADD CONSTRAINT FK_8o6t1f3kg96rxy5uv51f6k9fy FOREIGN KEY (svcv_id) REFERENCES service_versions (id);

ALTER TABLE service_versions ADD CONSTRAINT FK_92erjg9k1lni97gd87nt6tq37 FOREIGN KEY (service_id, service_org_id) REFERENCES services (id, organization_id);

ALTER TABLE endpoint_properties ADD CONSTRAINT FK_gn0ydqur10sxuvpyw2jvv4xxb FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE applications ADD CONSTRAINT FK_jenpu34rtuncsgvtw0sfo8qq9 FOREIGN KEY (organization_id) REFERENCES organizations (id);

ALTER TABLE policies ADD CONSTRAINT FK_l4q6we1bos1yl9unmogei6aja FOREIGN KEY (definition_id) REFERENCES policydefs (id);

ALTER TABLE oauth_apps ADD CONSTRAINT FK_l5q6we1bos1yl98nmogei7aja FOREIGN KEY (app_id) REFERENCES application_versions (id);

ALTER TABLE plans ADD CONSTRAINT FK_lwhc7xrdbsun1ak2uvfu0prj8 FOREIGN KEY (organization_id) REFERENCES organizations (id);

ALTER TABLE contracts ADD CONSTRAINT FK_nyw8xu6m8cx4rwwbtrxbjneui FOREIGN KEY (planv_id) REFERENCES plan_versions (id);

ALTER TABLE svc_gateways ADD CONSTRAINT FK_p5dm3cngljt6yrsnvc7uc6a75 FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE permissions ADD CONSTRAINT FK_sq51ihfrapwdr98uufenhcocg FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE svc_plans ADD CONSTRAINT FK_t7uvfcsswopb9kh8wpa86blqr FOREIGN KEY (service_version_id) REFERENCES service_versions (id);

ALTER TABLE plan_versions ADD CONSTRAINT FK_tonylvm2ypnq3efxqr1g0m9fs FOREIGN KEY (plan_id, plan_org_id) REFERENCES plans (id, organization_id);

ALTER TABLE plugins ADD CONSTRAINT UK_plugins_1 UNIQUE (group_id, artifact_id);

ALTER TABLE memberships ADD CONSTRAINT UK_memberships_1 UNIQUE (user_id, role_id, org_id);

ALTER TABLE plan_versions ADD CONSTRAINT UK_plan_versions_1 UNIQUE (plan_id, plan_org_id, version);

ALTER TABLE application_versions ADD CONSTRAINT UK_app_versions_1 UNIQUE (app_id, app_org_id, version);

ALTER TABLE service_versions ADD CONSTRAINT UK_service_versions_1 UNIQUE (service_id, service_org_id, version);

ALTER TABLE service_defs ADD CONSTRAINT UK_service_defs_1 UNIQUE (service_version_id);

ALTER TABLE contracts ADD CONSTRAINT UK_contracts_1 UNIQUE (appv_id, svcv_id, planv_id);

CREATE INDEX IDX_auditlog_1 ON auditlog(who);

CREATE INDEX IDX_auditlog_2 ON auditlog(organization_id, entity_id, entity_version, entity_type);

CREATE INDEX IDX_users_1 ON users(username);

CREATE INDEX IDX_users_2 ON users(full_name);

CREATE INDEX IDX_FK_permissions_1 ON permissions(role_id);

CREATE INDEX IDX_memberships_1 ON memberships(user_id);

CREATE INDEX IDX_organizations_1 ON organizations(name);

CREATE INDEX IDX_FK_plans_1 ON plans(organization_id);

CREATE INDEX IDX_FK_applications_1 ON applications(organization_id);

CREATE INDEX IDX_services_1 ON services(name);

CREATE INDEX IDX_FK_services_1 ON services(organization_id);

CREATE INDEX IDX_policies_1 ON policies(organization_id, entity_id, entity_version);

CREATE INDEX IDX_policies_2 ON policies(order_index);

CREATE INDEX IDX_FK_policies_1 ON policies(definition_id);

CREATE INDEX IDX_oauth_defs_1 ON oauth_apps(oauth_svc_orgid, oauth_svc_id, oauth_svc_version);

CREATE INDEX IDX_FK_contracts_p ON contracts(planv_id);

CREATE INDEX IDX_FK_contracts_s ON contracts(svcv_id);

CREATE INDEX IDX_FK_contracts_a ON contracts(appv_id);

CREATE TABLE categories(ServiceBean_id VARCHAR(255) NOT NULL,ServiceBean_organization_id VARCHAR(255) NOT NULL,category VARCHAR(255),FOREIGN KEY (ServiceBean_id, ServiceBean_organization_id) REFERENCES services (id, organization_id));

CREATE INDEX FK_huasdtal54l0isoauy6mrtmpx ON categories (ServiceBean_id, ServiceBean_organization_id);

CREATE TABLE oauth_scopes(ServiceVersionBean_id BIGINT NOT NULL, oauth_scopes VARCHAR(255), oauth_scopes_desc VARCHAR(255) ,FOREIGN KEY (ServiceVersionBean_id) REFERENCES service_versions (id));

CREATE INDEX FK_uhasdtal55l0isoauy6mrtmpx ON oauth_scopes (ServiceVersionBean_id);


-- DATA POPULAT... *** SQLINES FOR EVALUATION USE ONLY *** 

INSERT INTO roles (id, auto_grant, created_by, created_on, description, name) VALUES ('OrganizationOwner', TRUE, 'admin', '2015-06-18 17:56:57.496', 'Automatically granted to the user who creates an Organization.  Grants all privileges.', 'Organization Owner');

INSERT INTO roles (id, auto_grant, created_by, created_on, description, name) VALUES ('ApplicationDeveloper', NULL, 'admin', '2015-06-18 17:56:57.632', 'Users responsible for creating and managing applications should be granted this role within an Organization.', 'Application Developer');

INSERT INTO roles (id, auto_grant, created_by, created_on, description, name) VALUES ('ServiceDeveloper', NULL, 'admin', '2015-06-18 17:56:57.641', 'Users responsible for creating and managing services should be granted this role within an Organization.', 'Service Developer');

INSERT INTO gateways (id, configuration,endpoint, created_by, created_on, description, modified_by, modified_on, name, type) VALUES ('KongGateway', '{"endpoint":"http://apim.t1t.be:8001","username":"","password":""}','http://apim.t1t.be:8000', '', '2015-08-18 17:56:58.083', 'This is the gateway.', '', '2015-08-18 17:56:58.083', 'Default Kong Gateway', 'REST');

INSERT INTO permissions (role_id, permissions) VALUES ('OrganizationOwner', 1);

INSERT INTO permissions (role_id, permissions) VALUES ('OrganizationOwner', 2);

INSERT INTO permissions (role_id, permissions) VALUES ('OrganizationOwner', 3);

INSERT INTO permissions (role_id, permissions) VALUES ('OrganizationOwner', 6);

INSERT INTO permissions (role_id, permissions) VALUES ('OrganizationOwner', 8);

INSERT INTO permissions (role_id, permissions) VALUES ('OrganizationOwner', 5);

INSERT INTO permissions (role_id, permissions) VALUES ('OrganizationOwner', 9);

INSERT INTO permissions (role_id, permissions) VALUES ('OrganizationOwner', 11);

INSERT INTO permissions (role_id, permissions) VALUES ('OrganizationOwner', 7);

INSERT INTO permissions (role_id, permissions) VALUES ('OrganizationOwner', 4);

INSERT INTO permissions (role_id, permissions) VALUES ('OrganizationOwner', 10);

INSERT INTO permissions (role_id, permissions) VALUES ('OrganizationOwner', 0);

INSERT INTO permissions (role_id, permissions) VALUES ('ApplicationDeveloper', 6);

INSERT INTO permissions (role_id, permissions) VALUES ('ApplicationDeveloper', 8);

INSERT INTO permissions (role_id, permissions) VALUES ('ApplicationDeveloper', 7);

INSERT INTO permissions (role_id, permissions) VALUES ('ServiceDeveloper', 3);

INSERT INTO permissions (role_id, permissions) VALUES ('ServiceDeveloper', 5);

INSERT INTO permissions (role_id, permissions) VALUES ('ServiceDeveloper', 9);

INSERT INTO permissions (role_id, permissions) VALUES ('ServiceDeveloper', 11);

INSERT INTO permissions (role_id, permissions) VALUES ('ServiceDeveloper', 4);

INSERT INTO permissions (role_id, permissions) VALUES ('ServiceDeveloper', 10);

-- In order to ... *** SQLINES FOR EVALUATION USE ONLY *** 
INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('BasicAuthentication', 'Add Basic Authentication to your APIs', '{
  "type": "object",
  "title": "Basic Authentication",
  "properties": {
    "hide_credentials": {
      "title": "Hide credentials",
      "description": "An optional boolean value telling the plugin to hide the credential to the upstream API server. It will be removed by the gateway before proxying the request.",
      "type": "boolean",
      "default": false
    }
  }
}', 'JsonSchema', 'fa-user', 'Basic Authentication Policy', NULL ,TRUE ,FALSE ,FALSE );

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('KeyAuthentication', 'Add Key Authentication to your APIs', '{
  "type": "object",
  "title": "Key Authentication",
  "properties": {
    "key_names": {
      "title":"Key names",
      "type": "array",
      "items": {
        "type": "string",
        "description":"Describes a name where the plugin will look for a valid credential. The client must send the authentication key in one of the specified key names, and the plugin will try to read the credential from a header, the querystring, a form parameter (in this order)."
      }
    },
    "hide_credentials": {
      "title": "Hide credentials",
      "description":"An optional boolean value telling the plugin to hide the credential to the upstream API server. It will be removed by the gateway before proxying the request.",
      "type": "boolean",
      "default": false
    }
  },
  "required": [
    "key_names"
  ]
}', 'JsonSchema', 'fa-key', 'Key Authentication Policy', NULL ,TRUE ,FALSE ,TRUE );

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('CORS', 'Allow consumers to make requests from browsers to your APIs', '{
  "type": "object",
  "title": "CORS",
  "properties": {
    "methods": {
      "type": "array",
      "items": {
        "title": "Methods",
        "type": "string",
        "pattern": "^POST$|^GET$|^HEAD$|^PUT$|^PATCH$|^DELETE$",
        "validationMessage": "Should be one of: GET,HEAD,PUT,PATCH,POST,DELETE",
        "description": "Value for the Access-Control-Allow-Methods header, expects a string (e.g. GET or POST). Defaults to the values GET,HEAD,PUT,PATCH,POST,DELETE."
      }
    },
    "credentials": {
      "title": "Credentials",
      "description": "Flag to determine whether the Access-Control-Allow-Credentials header should be sent with true as the value.",
      "type": "boolean",
      "default": false
    },
      "headers": {
        "type": "array",
        "items": {
          "title": "Headers",
          "type": "string",
          "description": "Value for the Access-Control-Allow-Headers header (e.g. Origin, Authorization). Defaults to the value of the Access-Control-Request-Headers header."
        }
    },
    "exposed_headers": {
                "type": "array",
        "items": {
          "title": "Exposed headers",
          "type": "string",
          "description": "Value for the Access-Control-Expose-Headers header (e.g. Origin, Authorization). If not specified, no custom headers are exposed."
        }
    },
    "origin": {
      "title": "Origin",
      "type": "string",
      "default": "*",
      "description": "Value for the Access-Control-Allow-Origin header, expects a String. Defaults to *."
    },
    "max_age": {
      "title": "Max age",
      "type": "number",
      "description": "Indicated how long the results of the preflight request can be cached, in seconds.",
      "default": 3600
    },
    "preflight_continue": {
      "title": "Preflight continue",
      "type": "boolean",
      "description": "A boolean value that instructs the plugin to proxy the OPTIONS preflight request to the upstream API. Defaults to false.",
      "default": false
    }
  }
}', 'JsonSchema', 'fa-code', 'CORS Policy', NULL ,TRUE ,FALSE ,TRUE );

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('SSL', 'Add an SSL certificate for an underlying service', '{
  "type": "object",
  "title": "SSL",
  "properties": {
    "cert": {
      "title": "Certificate",
      "description": "Specify the path of the certificate file to upload.",
      "type": "string"
    },
    "only_https": {
      "title": "Only HTTPS allowed",
      "description": "Specify if the service should only be available through an https protocol.",
      "type": "boolean",
      "default": false
    },
    "key": {
      "title": "Key",
      "type": "string",
      "description": "Specify the path of the certificate key file to upload"
    }
  },
  "required": [
    "cert",
    "key"
  ]
}', 'JsonSchema', 'fa-lock', 'SSL Policy', NULL ,TRUE ,FALSE ,FALSE );

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('IPRestriction', 'Whitelist or Blacklist IPs that can make requests', '{
  "type": "object",
  "title": "IP Restriction",
  "properties": {
    "blacklist": {
        "type": "array",
        "items":{
               "type": "string",
               "pattern": "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\\/(\\d|[1-2]\\d|3[0-2]))?$",
                "description": "List of IPs or CIDR ranges to blacklist.",
                "validationMessage":"IP or CIDR required"
        }
    },
    "whitelist": {
        "type": "array",
        "items":{
            "type": "string",
            "pattern": "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\\/(\\d|[1-2]\\d|3[0-2]))?$",
            "description": "List of IPs or CIDR ranges to whitelist.",
            "validationMessage":"IP or CIDR required"
        }
    }
  }
}', 'JsonSchema', 'fa-table', 'IP Restriction Policy', NULL ,TRUE ,TRUE ,FALSE );

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('OAuth2', 'Add an OAuth2 Authentication to your APIs', '{
  "type": "object",
  "title": "OAuth2",
  "properties": {
    "scopes": {
      "type": "array",
      "items": {
          "type": "object",
          "properties":{
            "scope":{
                "title": "Scope",
                "type": "string",
                "pattern": "^[a-z,A-Z,-]+$",
                "description": "Provide the scope identifier that will be available to the end user (use only lowercase characters and no special characters)."
            },
            "scope_desc":{
                "title": "Scope Description",
                "type": "string",
                "description": "Describes the scope that will be available to the end user."
            }
          }
      }
    },
    "mandatory_scope": {
      "title": "Mandatory scope",
      "type": "boolean",
      "func": "function",
      "default": false,
      "description": "An optional boolean value telling the plugin to require at least one scope to be authorized by the end user."
    },
    "token_expiration": {
      "title": "Token expiration",
      "type": "number",
      "default": 7200,
      "description": "An optional integer value telling the plugin how long should a token last, after which the client will need to refresh the token. Set to 0 to disable the expiration."
    },
    "enable_authorization_code": {
      "title": "Enable Authorization Code Grant",
      "type": "boolean",
      "default": true,
      "description": "An optional boolean value to enable the three-legged Authorization Code flow (RFC 6742 Section 4.1)."
    },
    "enable_implicit_grant": {
      "title": "Enable Implicit Grant",
      "type": "boolean",
      "default": false,
      "description": "An optional boolean value to enable the Implicit Grant flow which allows to provision a token as a result of the authorization process (RFC 6742 Section 4.2)."
    },
    "enable_client_credentials": {
      "title": "Enable Client Credentials Grant",
      "type": "boolean",
      "default": false,
      "description": "An optional boolean value to enable the Client Credentials Grant flow (RFC 6742 Section 4.4)."
    },
    "enable_password_grant": {
      "title": "Enable Resource Owner Password Grant",
      "type": "boolean",
      "default": false,
      "description": "An optional boolean value to enable the Resource Owner Password Credentials Grant flow (RFC 6742 Section 4.3)."
    },
    "hide_credentials": {
      "title": "Hide credentials",
      "type": "boolean",
      "default": false,
      "description": "An optional boolean value telling the plugin to hide the credential to the upstream API server. It will be removed by Kong before proxying the request."
    }
  },
  "required": [
    "mandatory_scope",
    "token_expiration",
    "enable_implicit_grant"
  ]
}', 'JsonSchema', 'fa-sign-in', 'OAuth2 Policy', NULL ,TRUE ,FALSE ,FALSE );

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('RateLimiting', 'Rate-limit how many HTTP requests a consumer can make', '{
  "type": "object",
  "title": "Rate Limiting",
  "properties": {
    "day": {
      "title": "Day(s)",
      "description": "The amount of HTTP requests the developer can make per day. At least one limit must exist.",
      "type": "number"
    },
    "minute": {
      "title": "Minute(s)",
      "description": "The amount of HTTP requests the developer can make per minute. At least one limit must exist.",
      "type": "number"
    },
    "second": {
      "title": "Second(s)",
      "description": "The amount of HTTP requests the developer can make per second. At least one limit must exist.",
      "type": "number"
    },
    "hour": {
      "title": "Hour(s)",
      "description": "The amount of HTTP requests the developer can make per hour. At least one limit must exist.",
      "type": "number"
    },
    "month": {
      "title": "Month(s)",
      "description": "The amount of HTTP requests the developer can make per month. At least one limit must exist.",
      "type": "number"
    },
    "year": {
      "title": "Year(s)",
      "description": "The amount of HTTP requests the developer can make per year. At least one limit must exist.",
      "type": "number"
    }
  }
}', 'JsonSchema', 'fa-tachometer', 'Rate Limiting Policy', NULL ,TRUE ,TRUE ,FALSE );

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('RequestSizeLimiting', 'Block requests with bodies greater than a specific size', '{
  "type": "object",
  "title": "Request Size Limiting",
  "properties": {
    "allowed_payload_size": {
      "title": "Allowed payload size",
      "description": "Allowed request payload size in megabytes, default is 128 (128 000 000 Bytes)",
      "type": "number",
      "default": 128
    }
  }
}', 'JsonSchema', 'fa-compress', 'Request Size Limiting Policy', NULL ,TRUE ,TRUE ,FALSE );

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('RequestTransformer', 'Modify the request before hitting the upstream sever', '{
  "type": "object",
  "title": "Request Transformer",
  "properties": {
    "remove": {
      "title": "Remove from request",
      "type": "object",
      "properties": {
          "querystring": {
              "type": "array",
              "items": {
                "title": "Querystring",
                "type": "string",
                "description": "Parameter name to remove from the request querystring."
              }
          },
          "form": {
              "type": "array",
              "items": {
                "title": "Form",
                "type": "string",
                "description": "Parameter names to remove from the request body."
              }
          },
          "headers": {
              "type": "array",
              "items": {
                "title": "Header",
                "type": "string",
                "description": "Header names to remove from the request."
              }
          }
      }
    },
    "add": {
      "title": "Add to request",
      "type": "object",
      "properties": {
          "querystring": {
              "type": "array",
              "items": {
                "title": "Query",
                "type": "string",
                "description": "Paramname:value to add to the request querystring."
              }
          },
          "form": {
              "type": "array",
              "items": {
                "title": "Form",
                "type": "string",
                "description": "Paramname:value to add to the request body in urlencoded format."
              }
          },
          "headers": {
              "type": "array",
              "items": {
                "title": "Header",
                "type": "string",
                "description": "Headername:value to add to the request headers."
              }
          }
      }
    }
  }
}', 'JsonSchema', 'fa-chevron-circle-right', 'Request Transformer Policy', NULL ,TRUE ,FALSE ,FALSE );

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('ResponseTransformer', 'Modify the upstream response before returning it to the client', '{
  "type": "object",
  "title": "Response Transformer",
  "properties": {
    "remove": {
      "title": "Remove from request",
      "type": "object",
      "properties": {
          "headers": {
              "type": "array",
              "items": {
                    "title": "Header",
                    "type": "string",
                    "description": "Header name to remove from the response headers."
              }
          },
          "json": {
              "type": "array",
              "items": {
                "title": "JSON",
                "type": "string",
                "description": "JSON key name to remove from a JSON response body."
              }
          }
      }
    },
    "add": {
      "title": "Add to request",
      "type": "object",
      "properties": {
          "headers": {
              "type": "array",
              "items": {
                "title": "Header",
                "type": "string",
                "description": "Headername:value to add to the response headers."
              }
          },
          "json": {
              "type": "array",
              "items": {
                "title": "JSON",
                "type": "string",
                "description": "Jsonkey:value to add to a JSON response body."
              }
          }
      }
    }
  }
}', 'JsonSchema', 'fa-chevron-circle-left', 'Response Transformer Policy', NULL ,TRUE ,FALSE ,FALSE );

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('TCPLog', 'Send request and response logs to a TCP server', '{
  "type": "object",
  "title": "TCP Log",
  "properties": {
    "host": {
      "title": "Host",
      "description": "The IP address or host name to send data to.",
      "type": "string"
    },
    "keepalive": {
      "title": "Keep alive",
      "description": "Default 60000. An optional value in milliseconds that defines for how long an idle connection will live before being closed.",
      "type": "number",
      "default": 60000
    },
    "timeout": {
      "title": "Time-out",
      "description": "Default 10000. An optional timeout in milliseconds when sending data to the upstream server.",
      "type": "number",
      "default": 10000
    },
    "port": {
      "title": "Port",
      "description": "The port to send data to on the upstream server.",
      "type": "number"
    }
  },
  "required": [
    "host","port"
  ]
}', 'JsonSchema', 'fa-random', 'TCP Log Policy', NULL ,TRUE ,FALSE ,FALSE );

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('UDPLog', 'Send request and response logs to a UDP server', '{
  "type": "object",
  "title": "UDP Log",
  "properties": {
    "host": {
      "title": "Host",
      "description": "The IP address or host name to send data to.",
      "type": "string"
    },
    "timeout": {
      "title": "Time-out",
      "description": "Default 10000. An optional timeout in milliseconds when sending data to the upstream server.",
      "type": "number",
      "default": 10000
    },
    "port": {
      "title": "Port",
      "description": "The port to send data to on the upstream server.",
      "type": "number"
    }
  },
  "required": [
    "host","port"
  ]
}', 'JsonSchema', 'fa-crosshairs', 'UDP Log Policy', NULL ,TRUE ,FALSE ,FALSE );

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('HTTPLog', 'Send request and response logs to a HTTP server', '{
  "type": "object",
  "title": "HTTP Log",
  "properties": {
    "http_endpoint": {
      "title": "HTTP Endpoint",
      "description": "The HTTP endpoint (including the protocol to use) where to send the data to.",
      "type": "string"
    },
    "method": {
      "title": "HTTP Method",
      "description": "Default POST. An optional method used to send data to the http server, other supported values are PUT, PATCH.",
      "type": "string",
      "default": "POST"
    },
    "keepalive": {
      "title": "Keep alive",
      "description": "Default 60000. An optional value in milliseconds that defines for how long an idle connection will live before being closed.",
      "type": "number",
      "default": 60000
    },
    "timeout": {
      "title": "Time-out",
      "description": "Default 10000. An optional timeout in milliseconds when sending data to the upstream server.",
      "type": "number",
      "default": 10000
    }
  },
  "required": [
    "http_endpoint"
  ]
}', 'JsonSchema', 'fa-exchange', 'HTTP Log Policy', NULL ,FALSE ,FALSE ,TRUE );

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('FileLog', 'Append request and response data to a log file on disk', '{
  "type": "object",
  "title": "File Log",
  "properties": {
    "path": {
      "title": "Path",
      "description": "The file path of the output log file. The plugin will create the file if it doesn''t exist yet. Make sure Kong has write permissions to this file.",
      "type": "string"
    }
  },
  "required": [
    "path"
  ]
}', 'JsonSchema', 'fa-file-text-o', 'File Log Policy', NULL ,TRUE ,FALSE ,FALSE );

INSERT INTO policydefs (id, description, form, form_type, icon, name, plugin_id,scope_service,scope_plan,scope_auto) VALUES ('Analytics', 'View API analytics in Mashape analytics - retention 1 day', '{
  "type": "object",
  "title": "File Log",
  "properties": {
    "service_token": {
      "title": "Service token (API key)",
      "type": "string",
      "required": true
    }
  },
  "required": [
    "service_token"
  ]
}', 'JsonSchema', 'fa-line-chart', 'Mashape Analytics Policy', NULL ,TRUE ,FALSE ,FALSE );


INSERT INTO users (username, email, full_name, joined_on,admin,pic) VALUES ('admin', 'admin@example.org', 'Admin', '2015-06-18 17:56:54.794',TRUE ,NULL );

INSERT INTO organizations (id,description,name,created_by,created_on,modified_by,modified_on) VALUES ('Digipolis','Digipolis','Digipolis','admin',CURRENT_DATE,'admin',CURRENT_DATE);

INSERT INTO memberships (id,created_on, org_id, role_id, user_id) VALUES (999,CURRENT_DATE,'Digipolis','OrganizationOwner','admin');