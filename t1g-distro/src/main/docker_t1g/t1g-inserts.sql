-- POPULATE TABLES

-- ROLES

INSERT INTO roles (id, auto_grant, created_by, created_on, description, name) VALUES
  ('Owner', TRUE, 'admin', CURRENT_DATE,
   'Automatically granted to the user who creates an Organization.  Grants all privileges.', 'Owner');

INSERT INTO roles (id, auto_grant, created_by, created_on, description, name) VALUES
  ('Developer', NULL, 'admin', CURRENT_DATE,
   'Users responsible for creating and managing applications and services should be granted this role within an Organization.',
   'Developer');

INSERT INTO roles (id, auto_grant, created_by, created_on, description, name) VALUES
  ('Watcher', NULL, 'admin', CURRENT_DATE,
   'Users who only need read access can be granted this role. They can view all information within an Organization, but cannot make changes.',
   'Watcher');

-- PERMISSIONS

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 0);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 1);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 2);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 3);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 4);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 5);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 6);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 7);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 8);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 9);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 10);

INSERT INTO permissions (role_id, permissions) VALUES ('Owner', 11);

INSERT INTO permissions (role_id, permissions) VALUES ('Watcher', 0);

INSERT INTO permissions (role_id, permissions) VALUES ('Watcher', 3);

INSERT INTO permissions (role_id, permissions) VALUES ('Watcher', 6);

INSERT INTO permissions (role_id, permissions) VALUES ('Watcher', 9);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 0);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 1);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 3);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 4);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 6);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 7);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 9);

INSERT INTO permissions (role_id, permissions) VALUES ('Developer', 10);

-- POLICY DEFINITIONS

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('ACL', 'Enable the service to work with an Access Control List', '{
  "type": "object",
  "title": "ACL",
  "properties": {
    "placeholder": {}
  }
}', 'JsonSchema', 'fa-users', 'ACL Policy', NULL, TRUE, FALSE, TRUE, NULL, NULL,
        'iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAYAAAA5ZDbSAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDE0IDc5LjE1Njc5NywgMjAxNC8wOC8yMC0wOTo1MzowMiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MzQ5NDcxQzc1OEU3MTFFNTgxNkVBMDEzQjA2QTNEQTMiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MzQ5NDcxQzg1OEU3MTFFNTgxNkVBMDEzQjA2QTNEQTMiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGQ0EwQkQ3RTU4RTYxMUU1ODE2RUEwMTNCMDZBM0RBMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDozNDk0NzFDNjU4RTcxMUU1ODE2RUEwMTNCMDZBM0RBMyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pp3sFUsAAA7ESURBVHja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',
        'Service has an Access Control List (ACL) that limits who can access it.', NULL);

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('AWSLambda',
  'Invoke an AWS Lambda function from Kong. It can be used in combination with other request plugins to secure, manage or extend the function.', '{
  "type": "object",
  "properties": {
    "aws_key": {
      "title": "AWS Key",
      "description": "The AWS key credential to be used when invoking the function.",
      "type": "string",
      "required": true
    },
    "aws_secret": {
      "title": "AWS Secret",
      "description": "The AWS secret credential to be used when invoking the function.",
      "type": "string",
      "required": true
    },
    "function_name": {
      "title": "Function Name",
      "description": "The AWS Lambda function name to invoke",
      "type": "string",
      "required": true
    },
    "aws_region": {
      "title": "AWS Region",
      "description": "The AWS region where the Lambda function is located. Regions supported are: us-east-1, us-east-2, ap-northeast-1, ap-northeast-2, ap-southeast-1, ap-southeast-2, eu-central-1, eu-west-1.",
      "type": "string",
      "required": true,
      "enum": [
        "us-east-1",
        "us-east-2",
        "ap-northeast-1",
        "ap-northeast-2",
        "us-west-2",
        "ap-southeast-1",
        "ap-southeast-2",
        "eu-central-1",
        "eu-west-1"
      ]
    },
    "qualifier": {
      "title": "Qualifier",
      "description": "The Qualifier to use when invoking the function.",
      "type": "string"
    },
    "invocation_type": {
      "title": "Invocation Type",
      "description": "The InvocationType to use when invoking the function. Available types are RequestResponse, Event, DryRun.",
      "type": "string",
      "required": true,
      "enum": [
        "RequestResponse",
        "Event",
        "DryRun"
      ],
      "default": "RequestResponse"
    },
    "log_type": {
      "title": "Log Type",
      "description": "The LogType to use when invoking the function. By default None and Tail are supported.",
      "type": "string",
      "required": true,
      "enum": [
        "Tail",
        "None"
      ],
      "default": "Tail"
    },
    "timeout": {
      "title": "Timeout",
      "description": "The timeout value in milliseconds.",
      "type": "number",
      "required": true,
      "default": 60000
    },
    "keepalive": {
      "title": "Keepalive",
      "description": "The keepalive value in millisconds.",
      "type": "number",
      "required": true,
      "default": 60000
    }
  }
}', 'JsonSchema', 'fa-amazon', 'AWS Lambda Policy', NULL, TRUE, FALSE, FALSE, NULL, NULL,
        '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',
        'The service invokes an AWS Lambda function from the gateway',
        '<p class="text-light">Invokes: <b>{{functionName}}</b>.</p><p class="text-light">Region: <b>{{awsRegion}}</b>.</p>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('Analytics', 'View API analytics in Mashape analytics - retention 1 day', '{
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
}', 'JsonSchema', 'fa-line-chart', 'Mashape Analytics Policy', NULL, TRUE, FALSE, FALSE, NULL, NULL,
        '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',
        'Analytics data is sent to Galileo', '<span class="text-light">Service token: <b>{{serviceToken}}</b>.</span>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('BasicAuthentication', 'Add Basic Authentication to your APIs', '{
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
}', 'JsonSchema', 'fa-user', 'Basic Authentication Policy', NULL, FALSE, FALSE, FALSE, NULL, NULL,
        'iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAMAAAAOusbgAAAAQlBMVEXW19gAAADa2tra2tra2tra2tra2tra2tra2tr////a2toAtv//alNZYmakqaxmbXLp9fz/c11Wzv//6ub/m41qcnbsFLrIAAAACXRSTlP5ANBuxwYbB8v91il2AAABMUlEQVRo3u3bwQ6CMBBF0akUFarQAv7/r+osTTSZxpe24LsbWRhOJJgwJCPO9Z2XsWDiu945cefTWLzT2Ulf3lX5It1YpU68fsShYFFFL3pfDWVTWV2Fy6Ym4U8RJkyYMGHCX/sHOC4B1hLtcAzQohlesPBihgO4LHgARZgwYcI1YHncs3oICFY3T947nH+p935zESZMmHAI7T/Qo0eY9oc28Ji6h8H8PcKECf8Ar1u6gUrbaodXZWGl1QxvN2ibGU5YOJlh/fYASs9FmDDhBuBpnifrERKe7q8m6xEQnvWEs/XoCHDepT7CzfWH/2PChAnvDkY/0Lc/wqCHtvbH1A8d/Y3Ae4QJEyZMmPDXCGNgsS//YJdwfPmfrKKvt2h1qbRaVm+ZTtcHr2XXB6+6PvgEGyYXnIvXupIAAAAASUVORK5CYII=',
        'Service secured with Basic authentication',
        '<p class="text-light small">Credentials will be hidden from the upstream service: <b>{{hideCredentials}}</b></p>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('CORS', 'Allow consumers to make requests from browsers to your APIs', '{
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
    "origins": {
      "type": "array",
      "items": {
        "title": "Origins",
        "description": "Value for the Access-Control-Allow-Origin header, expects a String. Defaults to *.",
        "type": "string"
      }
    },
    "max_age": {
      "title": "Max age",
      "type": "number",
      "description": "Indicated how long the results of the preflight request can be cached, in seconds.",
      "default": 3600,
      "minimum": 1
    },
    "preflight_continue": {
      "title": "Preflight continue",
      "type": "boolean",
      "description": "A boolean value that instructs the plugin to proxy the OPTIONS preflight request to the upstream API. Defaults to false.",
      "default": false
    }
  }
}', 'JsonSchema', 'fa-code', 'CORS Policy', NULL, TRUE, FALSE, TRUE, '[
  "methods",
  "credentials",
  "headers",
  {
    "type": "help",
    "helpvalue": "<p class=\"text-justified text-warning xsmall\">WARNING: When implementing a custom CORS policy we STRONGLY recommend including the following headers in order to ensure correct functioning of the API Gateway: \"Accept\",\"Accept-Version\",\"Content-Length\",\"Content-MD5\",\"Content-Type\",\"Date\",\"apikey\",\"Authorization\".</p>"
  },
  "exposed_headers",
  "origins",
  "max_age",
  "preflight_continue"
]',
        '{"methods":["HEAD","DELETE","GET","POST","PUT","PATCH"],"credentials":false,"exposed_headers":[],"max_age":3600.0,"preflight_continue":false,"origins":["*"],"headers":["Accept","Accept-Version","Content-Length","Content-MD5","Content-Type","Date","apikey","Authorization"]}',
        'iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAYAAAA5ZDbSAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDE0IDc5LjE1Njc5NywgMjAxNC8wOC8yMC0wOTo1MzowMiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MjUxODJDMDdFMkZBMTFFNEE0MDZDRjUyM0I5ODM0NzYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MjUxODJDMDhFMkZBMTFFNEE0MDZDRjUyM0I5ODM0NzYiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDoyNTE4MkMwNUUyRkExMUU0QTQwNkNGNTIzQjk4MzQ3NiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDoyNTE4MkMwNkUyRkExMUU0QTQwNkNGNTIzQjk4MzQ3NiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PhKCuyEAABHfSURBVHja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',
        'Consumers can make requests from the browser',
        '<p class="text-light">Allowed Methods: <span class="text-bold">{{methods}}</span></p><p class="text-light">Allowed Origin: <span class="text-bold">{{origin}}</span></p>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('DataDog',
  'Log API metrics like request count, request size, response status and latency to the local Datadog agent.', '{
  "type": "object",
  "title": "DataDog",
  "properties": {
    "placeholder": {}
  }
}', 'JsonSchema', 'fa-paw', 'DataDog', NULL, TRUE, FALSE, FALSE, NULL, '{
  "host": "localhost",
  "tags": {},
  "timeout": 10000,
  "metrics": [
    "request_count",
    "latency",
    "request_size",
    "status_count",
    "response_size",
    "unique_users",
    "request_per_user",
    "upstream_latency"
  ],
  "port": 8125
}',
        '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',
        'Analytics data is sent to DataDog', NULL);

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('FileLog', 'Append request and response data to a log file on disk', '{
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
}', 'JsonSchema', 'fa-file-text-o', 'File Log Policy', NULL, FALSE, FALSE, FALSE, NULL, NULL,
        'iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAYAAAA5ZDbSAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDE0IDc5LjE1Njc5NywgMjAxNC8wOC8yMC0wOTo1MzowMiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MEQwRDdDNkZDQUIwMTFFNEFFRURBMUFDNjk5NUY4NTciIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MEQwRDdDNzBDQUIwMTFFNEFFRURBMUFDNjk5NUY4NTciPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDowRDBEN0M2RENBQjAxMUU0QUVFREExQUM2OTk1Rjg1NyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowRDBEN0M2RUNBQjAxMUU0QUVFREExQUM2OTk1Rjg1NyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pn3ut6YAAALxSURBVHja7J3BThNRFIbPDIOkWra4wOJuNpLgghR2hq32BeQBqu58CZ9B5QX6At0blxg0YOpilnXoQra2mQwIeE5S42RsYsiQdubO9yWnIdMEuPfj3DN/S1IviiKZckfrpdZzrU2tpkCVGGsNtHpa78IwPLeL3lTwulZf6zH75ATHWh2VPDLB1rmHyHWOE60dXx9eINdJtrS6gT7s559ptVrSaDTYogqRJInEcZy/vG9HdDq9wUKue5JTPyvXQG51meFuxWdb3AbBCAYEA4IBwYBgQDAgGMGAYEAwIBgQDAgGBCMY3CRY5A8/HYu8HVzJ0dm1TC7c2th7yyLba5682vTlQbOGgr//FOl+uJTxhZudY3+wH0fX8vnsUg72lmRjtWZH9PtvV87KzWJrtLXWbgbbsVwXPv1Y3FoXdkTnZ26/49b9Xqf/t2uTX9xFA4IBwYBgBAOCAcFQEoKq/KLZXDkrN+efvyk3/X5Vye10MEc0IBiYwUX538y77ZnoymvjdDBHNCAYmMG3nYOL5tiiuZgcDAgGBAM5mFxMB3NEA4KBGTzPHMz7wXQwIBjBwAwuRw4m99LBHNGAYGAGzycHF82xRXMxORgQDAgGcjC5mA7miAYEAzN4njmY94PpYEAwgoEZXI4cTO6lgzmiAcHADJ5PDi6aY4vmYnIwIBgQDORgcjEdzBENCAYEA4IBwYBgQDAgGMGAYCg5C3st2j7jPvsZwkXfvy0zjaCGHby95tWmi9r3vfoJ7j7ypbnsvlxbo621doIfrooc7C3Jk3VP7gbuibU12dpsjbbW2s1gY0MX/maX+zzuogHBgGAEA4IBwYBgQDAgGBAMCEYwIBgQDAgGBAOC4R/B59kLSZKwKxVlhruxCf6SvRLHsaRpym5VDHNm7nIM7H+yelq72avD4ZAdc4OeF0XRin5xqLXFfjjFV622HdF2Hj/TOmZPnOFE62kYhumfu+iR1o7Wa60jrQl7VDkmU3fmsK1yzan8FmAAJMLOPnOoa5oAAAAASUVORK5CYII=',
        NULL, NULL);

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('HAL', 'The HAL policy rewrites currie-values from hal/json bodies.', '{
  "type": "object",
  "title": "HAL Authentication",
  "properties": {
    "placeholder" :{}
  },
  "required": []
}', 'JsonSchema', 'fa-share-square', 'HAL Policy', NULL, TRUE, FALSE, FALSE, NULL, NULL,
        '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',
        'Rewrites the HAL documentation links for this Service to pass via the gateway',
        '<p class="text-light">HAL links to the service will be rewritten to have the gateway uri as their basepath.</p>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('HTTPLog', 'Send request and response logs to a HTTP server', '{
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
}', 'JsonSchema', 'fa-exchange', 'HTTP Log Policy', NULL, TRUE, FALSE, FALSE, NULL, NULL,
        'iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAIAAAC2BqGFAAAE60lEQVR4nO3dzWvbZhwHcD2KY0tPtTh244Ibk0Agg2wwGDlk0Ga0EGhXell33R+1+47LNb20FAo9FEIPOZhBYNloR8HBicFx/BI7svySPDuIKZpjx44k//Sy7+fkR7Ze8s2Pnx4L2WZCCOlf3V6/0dJ1o9PrX9iXw+QYY7OxGa4kkhqPz8aulpuBCiEq9bN6U/fvCCMoqfFMao4xJplBCyGOT2q60fH7wCKIK/H7mTRjTJYk6aR2hpSnRDe6lfqZJEkxsy9bTyiJ+LzGNa6YBQ+3JYRo6Ua9pRudrrmk3tST2h1WrjbqzXNzkZKI5+6lEbF7QkjF8qmV9fwXd2R705jXOFL2BGPSvMatoW505F7/whprXPHjqKLJHmavfyHb58soZw/ZwxRCyD4eyv9K7Oanf//jo+e7/PbrLx1v375uuKCiiSBoIgiayJgePe2eGN6ee1uoaCIImgiCJuLDPNpukjl1NPo4KpoIgiaCoIn4PI/2a1/0UNFEEDQRBE2EaB7t5ho05fand55ARRNB0EQQNBH2sXBsDVaXsj4eSvR8OixZj1HRRBA0EQRNJGT3dbjZl79Q0UQQNBEETQT3dRBBRRNB0EQQNJEA3dcRbahoIgiaCIImEqD7OqINFU0EQRNB0ERuPY9G13YGFU0EQRMZ0zomobeN7Z1X1nBtdWVzY33oK1++eVep1szHXFV//un50KcmtJBOvXi25XLd68dv4aqauZtaSKcW0qnlnNsbMbyZR+f3D6zHC+nUqJcdHpX+/PR56Mvy+weOw3KzrrX3m1+/nMv++MPW2urKrfZiF+LWcdtw3axbKJZ++fW3l2/eOd5jiIOWJKlQLI1/kXfrvn3/YWztj+JBj/bEi2dberttX1Iolnb38tZwc2P9eqPM3E25XPe6J48emF1Fbxsnp7Xdvbx9U9s7r9a/+Wryv8sSlHn09aPn6oH9L1zOZUedY92sewOuKsu57HLuOVeVt+8/mAv1tlGp1m44D40S7tZB4/vv/vNPctavEPR4XFXtw4E2NSHve3R+/+DwaPj/vFA8Hro84AYOe2nxvoONeH89ulKtuZl4BU2hWNreeW0NzcbtYDtBmXUEx19/fzbny3rbODw6HujITx8/dLZZBD2oUCyNOt2tra48efTA2Wa9D3rCax3hwlVlc2PdcTlL05hHLy1mR03pd/fyIQqaq4p5XWlpMfv08UOuuvo+UbSOQZsb6+Y7Q5fJDkDQg7iqeBuxCW9YiOC+DiKoaCIImgiCJjLms+C4r8MNfBbcBwiaCIImgnk0EVQ0EQRNBEETCdn3dQT/++1GQUUTQdBEEDQRfO/dFOFahw8QNBEETSRA39eB32EBDyBoIgiaSICuR0ejF4+CiiaCoIkgaCIR/B2WYEJFE0HQRBA0ER/m0cHvp9OAiiaCoIkgaCIhvq8jXFDRRBA0EQRNBL/DQgQVTURmjFkDIYSPhxIxl5dXYcqMybOxGWvc0g0/DimazttXYcZiMzJXEta43tJR1J4QQqq3dGvIlYSc1Lg1NjrdYrnaPG8jbccuhWiet4vlU6PTtRYmNc6EEOVqo2GLH7yV1Pi9dFKWJCmTmrM3EPAQV+KZ1JwkScxsykKISv2s3kRdeymp8UxqzpzXMfvZr9vrN1q6bnR6/T7atDMyY7HYDFcSSY3HZ6/eD/4DG0vhBntCcyIAAAAASUVORK5CYII=',
        'Request and response logs are sent to a server via HTTP',
        '<p class="text-light">Logs are sent to <b>{{httpEndpoint}}</b> via {{method}}.</p><p class="text-light">Timeout in ms: {{timeout}}');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('IPRestriction', 'Whitelist or Blacklist IPs that can make requests', '{
  "type": "object",
  "title": "IP Restriction",
  "properties": {
    "blacklist": {
        "type": "array",
        "items":{
               "type": "string",
               "pattern": "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/(\\d|[1-2]\\d|3[0-2]))?$",
                "description": "List of IPs or CIDR ranges to blacklist. You cannot set blacklist values if you have already whitelist values specified!",
                "validationMessage":"IP or CIDR required"
        }
    },
    "whitelist": {
        "type": "array",
        "items":{
            "type": "string",
            "pattern": "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/(\\d|[1-2]\\d|3[0-2]))?$",
            "description": "List of IPs or CIDR ranges to whitelist. You cannot set whitelist values if you have already blacklist values specified.",
            "validationMessage":"IP or CIDR required"
        }
    }
  }
}', 'JsonSchema', 'fa-table', 'IP Restriction Policy', NULL, TRUE, TRUE, FALSE, NULL, NULL,
        'iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAYAAAA5ZDbSAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDE0IDc5LjE1Njc5NywgMjAxNC8wOC8yMC0wOTo1MzowMiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6RkZGOEQ3RDYxRUJGMTFFNUJGMzc5RjAwMEQwMTRCMjgiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6RkZGOEQ3RDcxRUJGMTFFNUJGMzc5RjAwMEQwMTRCMjgiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGRkY4RDdENDFFQkYxMUU1QkYzNzlGMDAwRDAxNEIyOCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpGRkY4RDdENTFFQkYxMUU1QkYzNzlGMDAwRDAxNEIyOCIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgI1eIoAAAUrSURBVHja7J1NaBxlGIDf3eZ/o2lMU2PVYqgelmYPItMY8eApoILrzSJq0148JIL2R8EUvEShBe3BCvagTVGLgodGsUhPHsTE7KHgBhYRTRFaii2JEbP5ozt+X3e3nexua9LMzs588zzwQTqz7WS+h/f7mX37TkTK2aRak2oNqkVViwj4GVu1nGrLqi3atn3NebJUXmtBLgSXRdXmlWi7VHCbavX0jxGsqDanHUcdkYtcc6gvOJU6x5x7g1QqZcXj8YGWlpZkJBJppL98PAHb9lI2mx3LZDKjlmWlHKealLsFPUTHVGsuHh0fH7d6e3vPqJMNdF+whuWJiYlkX1+fU/JCtLBavkEikRhAbjCHZe2u5FiDjuAtziO5XO4iggM7XC9Ho9H7nceipR9CbnCp5C5Kt5gNghEMCAYEA4IBwYBgQDCCAcGAYEAwIBgQDAhGMCAYEAx+oy4sNzr90w9ybuRNmb/6V1WvE9uyVfoPH5XuJ57yxX2XZVXatn3FRMEnntlVdblOya+enayN0EikM5RDtFdyvb4WczBzcDhJbG529d9L/73AKhpMjuBz4yKnvxd58F6Rg6+ItLXS+0ZF8CdjIrP/iPzym8jhj0Tm/qX3jRK8zbEb+/Myko0T/NaASMdmJBsruEtF8HuDSDZ6FX0rycc+x4Rnq+jkG97/Fud/FRl4R+S13SKPxT255NSsy/vWCBF8e/QK+8MvCTljBV/HxkjVh+ha0dEmMvgCRqoueOxYda946YrIsFo5z8zdPLZzh1p8DXl64y9233Pb86enZ9b1+S8uzDBEV5T7wFaRQ3sItcALvpXcERW57XdhIvCCj55CrtGLrMtX3ZWrH5Cc+Foku6gmyKdFrJ3YrGkE70vmhfY87E7kHld75qnfRf64qBZon4r8eB6bNY3g/r58c4uFpZs/53Ii7xcedz75KFb9+6BjHbz8rPrto+WSf57CqoNgp83qYVlL1XKL3Kdu5+Phso9+sOuh1Tfu8kMzu+RZ9P7JC7URalTarB6OD7y0OpIbqaVamzm4mpLr1G2c/EbJrRcZ4nGnWYI1jyfyDQwVfAf0tJMXDUZGcC0yOtyg/W5PM0LYB3sNGSFhGKLJCDF3kUVGyBoFVzujAxiigSF6w4QlL9oMwRNpkVPf5h9V6nn4ke2ErjFDtP5G6choPudr+pLI8a+waozgSl8XLi1j1YghenKqXK7+2nBvck1/nbxov/PZd+Vy9XfDvT2E7YYi2C+1Npoby+WSj+VCBPul1sbQbpGeHSLd20Te3odc1yJY19rQqaqa4v/QHxn0PpK3d4m8O4RB1yOYWhuGC6bWRgj2wdTaCAzledHPvb6xvGifZlaQF+0WZFaE4UEHmRXB3Sb9H2RW+FzwWjM6fFJr404hL3q9cqm1YYhgam0YLphaG4YLdrvWBvhMsNu1NsBn2yS3a22AD1fRYHAEG4Jf961EMCC4EvqFkZ5dq6MTwV7TP3zEk45v7ey6/npZvxCa18uGhdC+XpY5GBAMCAYEA4IBwYBgQDCCAcGAYEAwIBgQDAhGMCAYEAw+FWzbNtU8A4pyV1rmyNaCrzmPZLPZM3RVMFHuzpYcymnBqyI2nU6PEsWBZEW7Kzm2rNNmN6nW7jyaSqWseDy+NxaLPa/+WE/f+XpYnteRm8lkTlqWlSo5PVus7qQLTTbRXUaxqOfk4iJLT84r9Ik5w3XBadm7QohkAyJXtXkVvXYlwVKYk7XkhsLP4PNpWK+WC4tlPSyv2hX9J8AA50WoYw+L3wsAAAAASUVORK5CYII=',
        'Only certain IP addresses are allowed access to the service',
        '<p class="text-bold">Blacklisted IPs: <span class="text-bold">{{blacklist}}</span></p><p class="text-bold">Whitelisted IPs: <span class="text-bold">{{whitelist}}</span></p>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('JSONThreatProtection',
  'Protect your API from JSON content-level attack attempts that use structures that overwhelm JSON Parsers.', '{
  "type": "object",
  "title": "JSON Threat Protection",
  "properties": {
    "source": {
      "title": "Source",
      "type": "string",
      "pattern": "^request$|^response$|^message$",
      "validationMessage": "Should be one of: request,response,message",
      "description": "The sources that should be protected"
    },
    "container_depth": {
      "title": "Container Depth",
      "description": "Specifies the maximum allowed containment depth, where the containers are objects or arrays. For example, an array containing an object which contains an object would result in a containment depth of 3. If you do not specify this element, or if you specify a negative integer, the system does not enforce any limit.",
      "type": "number",
      "default": 0
    },
    "string_value_length": {
      "title": "String Value Length",
      "description": "Specifies the maximum length allowed for a string value. If you do not specify this element, or if you specify a negative integer, the system does not enforce a limit.",
      "type": "number",
      "default": 0
    },
    "array_element_count": {
      "title": "Array Element Count",
      "description": "Specifies the maximum number of elements allowed in an array. If you do not specify this element, or if you specify a negative integer, the system does not enforce a limit.",
      "type": "number",
      "default": 0
    },
    "object_entry_count": {
      "title": "Object Entry Count",
      "description": "Specifies the maximum number of entries allowed in an object. If you do not specify this element, or if you specify a negative integer, the system does not enforce any limit.",
      "type": "number",
      "default": 0
    },
    "object_entry_name_length": {
      "title": "Object Entry Name Length",
      "description": "Specifies the maximum string length allowed for a property name within an object. If you do not specify this element, or if you specify a negative integer, the system does not enforce any limit.",
      "type": "number",
      "default": 0
    }
  }
}', 'JsonSchema', 'fa-shield', 'JSON Threat Protection', NULL, TRUE, FALSE, FALSE, NULL, NULL,
        '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',
        'Service is protected against JSON-based denial-of-service attacks',
        '<ul class="text-light"><li>Max. array element count: <b>{{arrayElementCount}}</b></li><li>Max. container depth: <b>{{containerDepth}}</b></li><li>Max. object entry count: <b>{{objectEntryCount}}</b></li><li>Max. object entry name length: <b>{{objectEntryNameLength}}</b></li><li>Max. string value length: <b>{{stringValueLength}}</b></li></ul><p class="text-light">Protection limited to: <b>{{source}}</b>.</p>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('JWT', 'Enable the service to accept and validate Json Web Tokens towards the upstream API.', '{
  "type": "object",
  "title": "JWT Token",
  "properties": {
    "placeholder" :{}
  },
  "required": []
}', 'JsonSchema', 'fa-certificate', 'JWT Policy', NULL, TRUE, FALSE, FALSE, NULL, NULL,
        'iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAYAAAA5ZDbSAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDE0IDc5LjE1Njc5NywgMjAxNC8wOC8yMC0wOTo1MzowMiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6RkNBMEJEN0M1OEU2MTFFNTgxNkVBMDEzQjA2QTNEQTMiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6RkNBMEJEN0Q1OEU2MTFFNTgxNkVBMDEzQjA2QTNEQTMiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGQ0EwQkQ3QTU4RTYxMUU1ODE2RUEwMTNCMDZBM0RBMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpGQ0EwQkQ3QjU4RTYxMUU1ODE2RUEwMTNCMDZBM0RBMyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PtTrtMQAABA7SURBVHja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',
        'Service secured with JWT authentication',
        '<span class="text-light">Claims that will be verified: <b>{{claimsToVerify}}</b></span>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('JWTUp',
  'Transforms authentication credentials to upstream certificated signed JWT. When policy is added in combination with JWT policy, JWT will be ignored.', '{
  "type": "object",
  "title": "JWT-Upstream",
  "properties": {
    "placeholder" :{}
  },
  "required": []
}', 'JsonSchema', 'fa-certificate', 'JWT-Up Policy', NULL, TRUE, FALSE, FALSE, NULL, NULL,
        'iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAYAAAA5ZDbSAAAACXBIWXMAAAsTAAALEwEAmpwYAAA6bWlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4KPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxMzIgNzkuMTU5Mjg0LCAyMDE2LzA0LzE5LTEzOjEzOjQwICAgICAgICAiPgogICA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPgogICAgICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgICAgICAgICB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iCiAgICAgICAgICAgIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIgogICAgICAgICAgICB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIKICAgICAgICAgICAgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIKICAgICAgICAgICAgeG1sbnM6ZGM9Imh0dHA6Ly9wdXJsLm9yZy9kYy9lbGVtZW50cy8xLjEvIgogICAgICAgICAgICB4bWxuczpwaG90b3Nob3A9Imh0dHA6Ly9ucy5hZG9iZS5jb20vcGhvdG9zaG9wLzEuMC8iCiAgICAgICAgICAgIHhtbG5zOnRpZmY9Imh0dHA6Ly9ucy5hZG9iZS5jb20vdGlmZi8xLjAvIgogICAgICAgICAgICB4bWxuczpleGlmPSJodHRwOi8vbnMuYWRvYmUuY29tL2V4aWYvMS4wLyI+CiAgICAgICAgIDx4bXA6Q3JlYXRvclRvb2w+QWRvYmUgUGhvdG9zaG9wIENDIDIwMTUuNSAoTWFjaW50b3NoKTwveG1wOkNyZWF0b3JUb29sPgogICAgICAgICA8eG1wOkNyZWF0ZURhdGU+MjAxNi0wNy0zMVQxODowNjo0NyswMjowMDwveG1wOkNyZWF0ZURhdGU+CiAgICAgICAgIDx4bXA6TW9kaWZ5RGF0ZT4yMDE2LTA3LTMxVDIxOjExOjI1KzAyOjAwPC94bXA6TW9kaWZ5RGF0ZT4KICAgICAgICAgPHhtcDpNZXRhZGF0YURhdGU+MjAxNi0wNy0zMVQyMToxMToyNSswMjowMDwveG1wOk1ldGFkYXRhRGF0ZT4KICAgICAgICAgPHhtcE1NOkluc3RhbmNlSUQ+eG1wLmlpZDplZTYyOTI0Yi1jMWQ2LTQ3NTYtYmY5Ni0zYTY3ODM2M2U4MTg8L3htcE1NOkluc3RhbmNlSUQ+CiAgICAgICAgIDx4bXBNTTpEb2N1bWVudElEPnhtcC5kaWQ6RkNBMEJEN0Q1OEU2MTFFNTgxNkVBMDEzQjA2QTNEQTM8L3htcE1NOkRvY3VtZW50SUQ+CiAgICAgICAgIDx4bXBNTTpEZXJpdmVkRnJvbSByZGY6cGFyc2VUeXBlPSJSZXNvdXJjZSI+CiAgICAgICAgICAgIDxzdFJlZjppbnN0YW5jZUlEPnhtcC5paWQ6RkNBMEJEN0E1OEU2MTFFNTgxNkVBMDEzQjA2QTNEQTM8L3N0UmVmOmluc3RhbmNlSUQ+CiAgICAgICAgICAgIDxzdFJlZjpkb2N1bWVudElEPnhtcC5kaWQ6RkNBMEJEN0I1OEU2MTFFNTgxNkVBMDEzQjA2QTNEQTM8L3N0UmVmOmRvY3VtZW50SUQ+CiAgICAgICAgIDwveG1wTU06RGVyaXZlZEZyb20+CiAgICAgICAgIDx4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ+eG1wLmRpZDpGQ0EwQkQ3RDU4RTYxMUU1ODE2RUEwMTNCMDZBM0RBMzwveG1wTU06T3JpZ2luYWxEb2N1bWVudElEPgogICAgICAgICA8eG1wTU06SGlzdG9yeT4KICAgICAgICAgICAgPHJkZjpTZXE+CiAgICAgICAgICAgICAgIDxyZGY6bGkgcmRmOnBhcnNlVHlwZT0iUmVzb3VyY2UiPgogICAgICAgICAgICAgICAgICA8c3RFdnQ6YWN0aW9uPnNhdmVkPC9zdEV2dDphY3Rpb24+CiAgICAgICAgICAgICAgICAgIDxzdEV2dDppbnN0YW5jZUlEPnhtcC5paWQ6ZWU2MjkyNGItYzFkNi00NzU2LWJmOTYtM2E2NzgzNjNlODE4PC9zdEV2dDppbnN0YW5jZUlEPgogICAgICAgICAgICAgICAgICA8c3RFdnQ6d2hlbj4yMDE2LTA3LTMxVDIxOjExOjI1KzAyOjAwPC9zdEV2dDp3aGVuPgogICAgICAgICAgICAgICAgICA8c3RFdnQ6c29mdHdhcmVBZ2VudD5BZG9iZSBQaG90b3Nob3AgQ0MgMjAxNS41IChNYWNpbnRvc2gpPC9zdEV2dDpzb2Z0d2FyZUFnZW50PgogICAgICAgICAgICAgICAgICA8c3RFdnQ6Y2hhbmdlZD4vPC9zdEV2dDpjaGFuZ2VkPgogICAgICAgICAgICAgICA8L3JkZjpsaT4KICAgICAgICAgICAgPC9yZGY6U2VxPgogICAgICAgICA8L3htcE1NOkhpc3Rvcnk+CiAgICAgICAgIDxkYzpmb3JtYXQ+aW1hZ2UvcG5nPC9kYzpmb3JtYXQ+CiAgICAgICAgIDxwaG90b3Nob3A6Q29sb3JNb2RlPjM8L3Bob3Rvc2hvcDpDb2xvck1vZGU+CiAgICAgICAgIDxwaG90b3Nob3A6RG9jdW1lbnRBbmNlc3RvcnM+CiAgICAgICAgICAgIDxyZGY6QmFnPgogICAgICAgICAgICAgICA8cmRmOmxpPnhtcC5kaWQ6RkNBMEJEN0Q1OEU2MTFFNTgxNkVBMDEzQjA2QTNEQTM8L3JkZjpsaT4KICAgICAgICAgICAgPC9yZGY6QmFnPgogICAgICAgICA8L3Bob3Rvc2hvcDpEb2N1bWVudEFuY2VzdG9ycz4KICAgICAgICAgPHRpZmY6T3JpZW50YXRpb24+MTwvdGlmZjpPcmllbnRhdGlvbj4KICAgICAgICAgPHRpZmY6WFJlc29sdXRpb24+NzIwMDAwLzEwMDAwPC90aWZmOlhSZXNvbHV0aW9uPgogICAgICAgICA8dGlmZjpZUmVzb2x1dGlvbj43MjAwMDAvMTAwMDA8L3RpZmY6WVJlc29sdXRpb24+CiAgICAgICAgIDx0aWZmOlJlc29sdXRpb25Vbml0PjI8L3RpZmY6UmVzb2x1dGlvblVuaXQ+CiAgICAgICAgIDxleGlmOkNvbG9yU3BhY2U+NjU1MzU8L2V4aWY6Q29sb3JTcGFjZT4KICAgICAgICAgPGV4aWY6UGl4ZWxYRGltZW5zaW9uPjEyMDwvZXhpZjpQaXhlbFhEaW1lbnNpb24+CiAgICAgICAgIDxleGlmOlBpeGVsWURpbWVuc2lvbj4xMjA8L2V4aWY6UGl4ZWxZRGltZW5zaW9uPgogICAgICA8L3JkZjpEZXNjcmlwdGlvbj4KICAgPC9yZGY6UkRGPgo8L3g6eG1wbWV0YT4KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAKPD94cGFja2V0IGVuZD0idyI/Pj0xzk4AAAAgY0hSTQAAeiUAAICDAAD5/wAAgOkAAHUwAADqYAAAOpgAABdvkl/FRgAAEh9JREFUeNrsnXl0VFW2xn+35okkkJCkAiQMgigOARQhhAAhEQJhCK0tbUtjR5/dYvN6yVPboXFowdeI2morzsKz8XWrTwIJyBQUMyCTTC2iqECUACkImSqVmu/7IzGAlaFuUlVU4v3WOmslt86wa3/3nLPPPvucEvDFlcCdwBRgIKBFRjjDARwFNgJvAl+2llEDvAR4AFFOXTJ5gFcBfUvkbpEV1G3S1iZOm/GylApUKpVoNBplRYYgdULXL/1I7jDA7W9Bg8EgVlZWiqIoildffbVMQhBTJ3XtbuKWv0lpsLq6WrwQ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',
        'Upstream API always receives signed JWT', '<span class="text-light">JWT RS256 signed to upstream API.</span>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('KeyAuthentication', 'Add Key Authentication to your APIs', '{
  "type": "object",
  "title": "Key Authentication",
  "properties": {
    "key_names": {
      "title":"Key names",
      "type": "array",
      "items": {
        "type": "string",
        "description":"Describes a name where the plugin will look for a valid credential. The client must send the authentication key in one of the specified key names, and the plugin will try to read the credential from a header, the querystring, a form parameter (in this order).",
        "default": "apikey"
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
}', 'JsonSchema', 'fa-key', 'Key Authentication Policy', NULL, TRUE, FALSE, TRUE, NULL,
        '{"key_names":["apikey"],"hide_credentials":false}',
        'iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAMAAAAOusbgAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6ODRGQzI0ODhFNUQxMTFFNDlEODRDQzU3NTAxNjc2MEYiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6ODRGQzI0ODdFNUQxMTFFNDlEODRDQzU3NTAxNjc2MEYiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjcyNTUwMTgzRTU3NzExRTQ4NTEyRTY1QjRGQzA5MEU2IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjcyNTUwMTg0RTU3NzExRTQ4NTEyRTY1QjRGQzA5MEU2Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+Ri+ZzwAAAwBQTFRFuenDm9qk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',
        'Service secured with Key authentication',
        '<span class="text-light">Key can be sent in one of the following fields: {{keyNames}}</span><p class="text-light">Credentials will be hidden from the upstream service: <b>{{hideCredentials}}</b></p>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('LDAPAuthentication', 'Add LDAP Bind Authentication to your APIs, with username and password protection.', '{
  "type": "object",
  "title": "LDAP Authentication",
  "properties": {
    "ldap_host": {
      "title": "LDAP Host",
      "description": "Host on which the LDAP server is running.",
      "type": "string",
      "default": "antwerpen.local"
    },
    "ldap_port": {
      "title": "LDAP Port",
      "description": "TCP port where the LDAP server is listening.",
      "type": "number",
      "default": 389
    },
    "base_dn": {
      "title": "Base DN",
      "description": "Base DN as the starting point for the search.",
      "type": "string"
    },
    "attribute": {
      "title": "Attribute",
      "description": "Attribute to be used to search the user.",
      "type": "string"
    },
    "cache_ttl": {
      "title": "Cache TTL",
      "description": "Cache expiry time",
      "type": "number",
      "default": 60
    },
    "timeout": {
      "title": "Timeout",
      "description": "An optional timeout in milliseconds when waiting for connection with LDAP server.",
      "type": "number",
      "default": 10000
    },
    "keepalive": {
      "title": "Keep Alive",
      "description": "An optional value in milliseconds that defines for how long an idle connection to LDAP server will live before being closed.",
      "type": "number",
      "default": 60000
    },
    "verify_ldap_host": {
      "title": "Verify LDAP Host",
      "description": "Set it to true to authenticate LDAP server.",
      "type": "boolean",
      "default": false
    },
    "start_tls": {
      "title": "Start TLS",
      "description": "Set it to true to issue StartTLS (Transport Layer Security) extended operation over ldap connection.",
      "type": "boolean",
      "default": false
    },
    "hide_credentials": {
      "title": "Hide credentials",
      "description": "An optional boolean value telling the plugin to hide the credential to the upstream API server. It will be removed by the gateway before proxying the request.",
      "type": "boolean",
      "default": true
    }
  },
  "required": [
    "ldap_host",
    "cache_ttl",
    "ldap_port"
  ]
}', 'JsonSchema', 'fa-database', 'LDAP Authentication Policy', NULL, TRUE, FALSE, FALSE, NULL, NULL,
        'iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAYAAAA5ZDbSAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDE0IDc5LjE1Njc5NywgMjAxNC8wOC8yMC0wOTo1MzowMiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NzY0NDQ3RUZENzc1MTFFNUE1NDdBOUJCREQyMURDQkQiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NzY0NDQ3RjBENzc1MTFFNUE1NDdBOUJCREQyMURDQkQiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo3NjQ0NDdFREQ3NzUxMUU1QTU0N0E5QkJERDIxRENCRCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3NjQ0NDdFRUQ3NzUxMUU1QTU0N0E5QkJERDIxRENCRCIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PudtG0UAAAmLSURBVHja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',
        'Authentication is integrated with an LDAP server',
        '<p class="text-light">LDAP host: <b>{{ldapHost}}:{{ldapPort}}</b></p><ul class="text-light"><li>Base DN: {{baseDn}}</li><li>Attribute: {{attribute}}</li></ul><p class="text-light">Credentials will be hidden from the upstream server: <b>{{hideCredentials}}</b>.</p><p class="text-light">LDAP host will be verified: <b>{{verifyLdapHost}}</b>.</p>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('OAuth2', 'Add an OAuth2 Authentication to your APIs', '{
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
                "pattern": "^[a-z,A-Z]+$",
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
      "minimum": 0,
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
    "hide_credentials": {
      "title": "Hide credentials",
      "type": "boolean",
      "default": false,
      "description": "An optional boolean value telling the plugin to hide the credential to the upstream API server. It will be removed by Kong before proxying the request."
    }
  }
}', 'JsonSchema', 'fa-sign-in', 'OAuth2 Policy', NULL, TRUE, FALSE, FALSE, '[
  "scopes",
  "mandatory_scope",
  "token_expiration",
  "enable_authorization_code",
  "enable_implicit_grant",
  {
    "type":"help",
    "helpvalue":"<p class=\"text-justified text-warning xsmall\">WARNING: The Implicit Grant flow is necessary for try-out functionality in the marketplace, but also prohibits the usage of refresh tokens.</p>"
  },
  "enable_client_credentials",
  "hide_credentials"
]', NULL,
        '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',
        'Service secured with OAuth2.0 authentication',
        '<p class="text-bold">Enabled profiles:</p><ul class="text-light"><li><p class="text-light">Authorization Code: <b>{{enableAuthorizationCode}}</b></p></li><li><p class="text-light">Client Credentials: <b>{{enableClientCredentials}}</b></p></li><li><p class="text-light">Implicit Grant: <b>{{enableImplicitGrant}}</b></p></li></ul><p class="text-bold">Scopes: {{scopes}}</p><p class="text-light">Mandatary Scopes: <strong>{{mandatoryScope}}</strong>.</p><p class="text-light">Credentials will be hidden from the upstream server: <b>{{hideCredentials}}</b></p><p class="text-light">Tokens expiration in seconds: {{tokenExpiration}}.</p>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('RateLimiting', 'Rate-limit how many HTTP requests a consumer can make', '{
  "type": "object",
  "title": "Rate Limiting",
  "properties": {
    "day": {
      "title": "Day(s)",
      "description": "The amount of HTTP requests the developer can make per day. At least one limit must exist.",
      "type": "integer"
    },
    "minute": {
      "title": "Minute(s)",
      "description": "The amount of HTTP requests the developer can make per minute. At least one limit must exist.",
      "type": "integer"
    },
    "second": {
      "title": "Second(s)",
      "description": "The amount of HTTP requests the developer can make per second. At least one limit must exist.",
      "type": "integer"
    },
    "hour": {
      "title": "Hour(s)",
      "description": "The amount of HTTP requests the developer can make per hour. At least one limit must exist.",
      "type": "integer"
    },
    "month": {
      "title": "Month(s)",
      "description": "The amount of HTTP requests the developer can make per month. At least one limit must exist.",
      "type": "integer"
    },
    "year": {
      "title": "Year(s)",
      "description": "The amount of HTTP requests the developer can make per year. At least one limit must exist.",
      "type": "integer"
    }
  }
}', 'JsonSchema', 'fa-tachometer', 'Rate Limiting Policy', NULL, TRUE, TRUE, FALSE, NULL, NULL,
        'iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4BAMAAADLSivhAAAALVBMVEXB1tWn0c/a2tqn0c/a2trB1tX////D9fIAzb7a2tqn0c//AAAAsKL9AAD29vabewDgAAAABnRSTlMCzctubRuL2NnbAAAAwElEQVRYw+3TLQ7CQBCG4Y+fenQVtg6NwuJ6ikosCSdAN5gxJA2CZK7AFfYKa0m4BZvQ0pDQBKZqN9+rRsyjJgPMCzFVb4AsWKMuMRVzS2zt+IRCzpWph9QQudvwTiTgyhgxMTExMXHq2PtbWxX6Z/Y+VhzpqYiJDfj4UYrYuX6xw/t3aWLVbpGY+NvbOJcqHn4b1TRxHzFxzPjVAG5LC/8aMTExMfFInOvVZg/aYK3mLpjY8QJZbrVNCcxyo13hCYDoEvoovFChAAAAAElFTkSuQmCC',
        'Consumers can send a limited number of requests',
        '<span class="text-light">Requests to this service are limited to:</span><ul class="text-light"><li>Per second: {{second}}</li><li>Per minute: {{minute}}</li><li>Per hour: {{hour}}</li><li>Per day: {{day}}</li><li>Per month: {{month}}</li><li>Per year: {{year}}</li></ul>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('RequestSizeLimiting', 'Block requests with bodies greater than a specific size', '{
  "type": "object",
  "title": "Request Size Limiting",
  "properties": {
    "allowed_payload_size": {
      "title": "Allowed payload size",
      "description": "Allowed request payload size in megabytes, default is 128 (128 000 000 Bytes)",
      "type": "number",
      "default": 128,
      "minimum": 0
    }
  },
  "required": [
    "allowed_payload_size"
  ]
}', 'JsonSchema', 'fa-compress', 'Request Size Limiting Policy', NULL, TRUE, TRUE, FALSE, NULL, NULL,
        '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',
        'Incoming requests cannot exceed a certain size',
        '<span class="text-light">Maximum request size to this service in MB: {{allowedPayloadSize}}</span>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('RequestTransformer', 'Modify the request before hitting the upstream sever', '{
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
            "description": "List of querystring names. Remove the querystring if it is present."
          }
        },
        "body": {
          "type": "array",
          "items": {
            "title": "Form",
            "type": "string",
            "description": "List of parameter names. Remove the parameter if and only if content-type is one the following [application/json, multipart/form-data, application/x-www-form-urlencoded] and parameter is present."
          }
        },
        "headers": {
          "type": "array",
          "items": {
            "title": "Header",
            "type": "string",
            "description": "List of header names. Unset the headers with the given name."
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
            "description": "List of queryname:value pairs. If and only if the querystring is not already set, set a new querystring with the given value. Ignored if the header is already set."
          }
        },
        "body": {
          "type": "array",
          "items": {
            "title": "Form",
            "type": "string",
            "description": "List of pramname:value pairs. If and only if content-type is one the following [application/json, multipart/form-data, application/x-www-form-urlencoded] and the parameter is not present, add a new parameter with the given value to form-encoded body. Ignored if the parameter is already present."
          }
        },
        "headers": {
          "type": "array",
          "items": {
            "title": "Header",
            "type": "string",
            "description": "List of headername:value pairs. If and only if the header is not already set, set a new header with the given value. Ignored if the header is already set."
          }
        }
      }
    },
    "replace": {
      "title": "Replace in request",
      "type": "object",
      "properties": {
        "querystring": {
          "type": "array",
          "items": {
            "title": "Query",
            "type": "string",
            "description": "List of queryname:value pairs. If and only if the header is already set, replace its old value with the new one. Ignored if the header is not already set."
          }
        },
        "body": {
          "type": "array",
          "items": {
            "title": "Form",
            "type": "string",
            "description": "List of paramname:value pairs. If and only if content-type is one the following [application/json, multipart/form-data, application/x-www-form-urlencoded] and the parameter is already present, replace its old value with the new one. Ignored if the parameter is not already present."
          }
        },
        "headers": {
          "type": "array",
          "items": {
            "title": "Header",
            "type": "string",
            "description": "List of headername:value pairs. If and only if the header is already set, replace its old value with the new one. Ignored if the header is not already set."
          }
        }
      }
    },
    "append": {
      "title": "Append to request",
      "type": "object",
      "properties": {
        "querystring": {
          "type": "array",
          "items": {
            "title": "Query",
            "type": "string",
            "description": "List of queryname:value pairs. If the querystring is not set, set it with the given value. If it is already set, a new querystring with the same name and the new value will be set."
          }
        },
        "body": {
          "type": "array",
          "items": {
            "title": "Form",
            "type": "string",
            "description": "List of paramname:value pairs. If the content-type is one the following [application/json, application/x-www-form-urlencoded], add a new parameter with the given value if the parameter is not present, otherwise if it is already present, the two values (old and new) will be aggregated in an array."
          }
        },
        "headers": {
          "type": "array",
          "items": {
            "title": "Header",
            "type": "string",
            "description": "List of headername:value pairs. If the header is not set, set it with the given value. If it is already set, a new header with the same name and the new value will be set."
          }
        }
      }
    }
  }
}', 'JsonSchema', 'fa-chevron-circle-right', 'Request Transformer Policy', NULL, TRUE, FALSE, FALSE, NULL, NULL,
        'iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAYAAAA5ZDbSAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDE0IDc5LjE1Njc5NywgMjAxNC8wOC8yMC0wOTo1MzowMiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NkM2NTU5REVFMkZBMTFFNEE0MDZDRjUyM0I5ODM0NzYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NkM2NTU5REZFMkZBMTFFNEE0MDZDRjUyM0I5ODM0NzYiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDoyNTE4MkMwOUUyRkExMUU0QTQwNkNGNTIzQjk4MzQ3NiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDoyNTE4MkMwQUUyRkExMUU0QTQwNkNGNTIzQjk4MzQ3NiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pm2rGPMAAAhtSURBVHja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',
        'Incoming requests are transformed',
        '<p class="text-bold">Removes</p><ul class="text-light"><li>From querystring: {{removeQuerystrings}}</li><li>From body: {{removeBody}}</li><li>From headers: {{removeHeaders}}</li></ul></p><p class="text-bold">Adds</p><ul class="text-light"><li>To querystring: {{addQuerystrings}}</li><li>To body: {{addBody}}</li><li>To headers: {{addHeaders}}</li></ul></p><p class="text-bold">Replaces</p><ul class="text-light"><li>In querystring: {{replaceQuerystrings}}</li><li>In body: {{replaceBody}}</li><li>In headers: {{replaceHeaders}}</li></ul></p><p class="text-bold">Appends</p><ul class="text-light"><li>To querystring: {{appendQuerystrings}}</li><li>To body: {{appendBody}}</li><li>To headers: {{appendHeaders}}</li></ul></p>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('ResponseTransformer', 'Modify the upstream response before returning it to the client', '{
  "type": "object",
  "title": "Response Transformer",
  "properties": {
    "remove": {
      "title": "Remove from response",
      "type": "object",
      "properties": {
        "headers": {
          "type": "array",
          "items": {
            "title": "Header",
            "type": "string",
            "description": "List of header names. Unset the header(s) with the given name."
          }
        },
        "json": {
          "type": "array",
          "items": {
            "title": "JSON",
            "type": "string",
            "description": "List of property names. Remove the property from the JSON body if it is present."
          }
        }
      }
    },
    "add": {
      "title": "Add to response",
      "type": "object",
      "properties": {
        "headers": {
          "type": "array",
          "items": {
            "title": "Header",
            "type": "string",
            "description": "List of headername:value pairs. If and only if the header is not already set, set a new header with the given value. Ignored if the header is already set."
          }
        },
        "json": {
          "type": "array",
          "items": {
            "title": "JSON",
            "type": "string",
            "description": "List of property:value pairs. If and only if the property is not present, add a new property with the given value to the JSON body. Ignored if the property is already present."
          }
        }
      }
    },
    "replace": {
      "title": "Replace in response",
      "type": "object",
      "properties": {
        "headers": {
          "type": "array",
          "items": {
            "title": "Header",
            "type": "string",
            "description": "List of headername:value pairs. If and only if the header is already set, replace its old value with the new one. Ignored if the header is not already set."
          }
        },
        "json": {
          "type": "array",
          "items": {
            "title": "JSON",
            "type": "string",
            "description": "List of property:value pairs. If and only if the parameter is already present, replace its old value with the new one. Ignored if the parameter is not already present."
          }
        }
      }
    },
    "append": {
      "title": "Append to response",
      "type": "object",
      "properties": {
        "headers": {
          "type": "array",
          "items": {
            "title": "Header",
            "type": "string",
            "description": "List of headername:value pairs. If the header is not set, set it with the given value. If it is already set, a new header with the same name and the new value will be set."
          }
        },
        "json": {
          "type": "array",
          "items": {
            "title": "JSON",
            "type": "string",
            "description": "List of property:value pairs. If the property is not present in the JSON body, add it with the given value. If it is already present, the two values (old and new) will be aggregated in an array."
          }
        }
      }
    }
  }
}', 'JsonSchema', 'fa-chevron-circle-left', 'Response Transformer Policy', NULL, TRUE, FALSE, FALSE, NULL, NULL,
        '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',
        'Outgoing responses are transformed',
        '<p class="text-bold">Removes</p><ul class="text-light"><li>From headers: {{removeHeaders}}</li><li>From JSON body: {{removeJson}}</li></ul></p><p class="text-bold">Adds</p><ul class="text-light"><li>To headers: {{addHeaders}}</li><li>To JSON body: {{addJson}}</li></ul></p><p class="text-bold">Replaces</p><ul class="text-light"><li>In headers: {{replaceHeaders}}</li><li>In JSON body: {{replaceJson}}</li></ul></p><p class="text-bold">Appends</p><ul class="text-light"><li>To headers: {{appendHeaders}}</li><li>To JSON body: {{appendJson}}</li></ul></p>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('SSL', 'Add an SSL certificate for an underlying service', '{
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
}', 'JsonSchema', 'fa-lock', 'SSL Policy', NULL, FALSE, FALSE, FALSE, NULL, NULL,
        'iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAIAAAC2BqGFAAAFqElEQVR4nO3dT3ATVRzA8fdedpt0kwLbBqdJ+g87nrDO2AoOBxiPjOWPDjc9gg6mV/So6AzgwLl0xnKl6h3GenEcvVRLK4Z2oINKIQ3JSJpN2mTzZ5N9HnZcMlmEWJrfWzK/zyl52c178+3OZtukE8o5J/8qmyXNWM9XNypmmZPH46h5lNAO5g1IO1S5x8t8j8et0JzwVCmRMR6JW2EbUuVgry/CCCNWaE74ff3PQm1T9MLakN/TNagMU0IZISRVWsPKLVKobaZKCUKIZJ2X7QcUj1+VgzvlXZQwcct7gXFi5oysZqT1WsEa0Yx0d0eQJktr65W/rSHF4x9SXqGEiltnm+CEr+p37dY9HS+xfHXDfliVg1h5W1BCVTlo381XN1jFLNv3d8qqiFW1p/qYFbMs1V8v1x/OD8sJ0HW1i7A3Yt2oj8kJx1c8IBgaCIYGIjWzUdDb0+p1vNDS5fVnboNHNBAMDQRDA8HQQDA0EAwNBEMDwdBAMDQQDA0EQwPB0EAwNBAMDQRDA8HQQDA0EAwNBEMDaeo9Q7E44bHsrbnM3K3cUqKYyFc3CSEBqSvSGXl1x94DPQde2zXi/g9YuTq0yflsavbqg5lEsfHTPFpF0yraUm7pm/i3kc7I+wPvHe49zKh7c7v31JEsJaOL0Ysrl5yVGySKiYsrl6KL0WQpCbO2LXBp6JvZm6cXonc2V5rf5c7myumF6O/ZWOtW9TzcGHo+c+Pj2Cc5I/d/d8wZuTOxM/OZ+Vas6jm5LnRcj3+2fNYwq1vb3TCrny6fjevx7V3V83PXi6HJzXO3z+s13fnQkH9wPDT+Zvf+kC9ECHlYevhrZv568vpq4X7DlsVa8dzt85dHJxl10WHkrtCzqe+d52WZSdHh6PHw8fqLikFlcFAZPBE5cS15bfKPy+W6T3kTQu5srnyXmh0PvQ2x6Oa46GfOCb/6YKZhUGbSlyMX3o2888RLN0bpsfDRCyPnZdZ4xMw8+NpV/yrpotCxbMx5JTcxPDGmjj19x1H19ehwtGEwUUzE3HQF4qLQc5lfGkaG/EPHwkeb2fd4+NiQf+iZTyiQi0Iv5ZYbRo6Expt8QWOUHQmNNwwuO55QIBeFXiuuNYzs797X/O7OjeOOJxTIRaGtvxbVC/nCze/u3Nj5hAK5KLTzlxTntcRTODfe8m89reCi0O0NQwPB0EAwNBAMDcRFf1T68a0fhD9D6+ARDQRDA8HQQESeo9eKa1/9Nb2gLRSqT3hLZRv5JWVMHfvw5Q/6OvtaOtFTCAsd1+MfLU7kq3mAuQpV/adHPy9qv02NTvYr/QAzOgk7dUzfuwJT2Zav5qfvXYGcsZ6w0AvaAvyk85kb8JNahIVu9Xn5iYq1IvykFrzqAIKhgWBoIBgaCIYGgqGBYGggGBoIhgaCoYFgaCAYGgiGBoKhgWBoIBgaCIYGgqGBYGggGBoIhgaCoYFgaCDCQvslBX7STk8n/KQWYaGf+R/erbCv+w34SS3CQp/aczIgBSBnDEiBU3tOQs5YT1joAWVganTy0O6Diqfl5xDFoxzafXBqdHJAGWj1XP9F5Oej+5X+L/Z+LnABkPCqAwiGBoKhgWBoIBgaCIYGgqGBYGggGBoIhgaCoYFgaCAYGgiGBoKhgWBoIBgaCIYGgqGBNPWeYbq83up1tD08ooFgaCAYGohECbW/r4QTbn8xYNgbEbeqdmBy077NKGMdzGvfzxmaiCW1p41q1r4t0w4WkHbY9zUj7apv43lxccI1I23fDUhdTJV77NOFXius6nezRgZzb5nJzayRWdXv6rWCNUIJVeUg5ZwnS/FMXX60vbrlYMjXzwghvb4+v6dL9Hrak9/T1evrI4RQzjkhhBOeKiUyxiPRC2srqhzs9UUYYcQObSmbJc1I56sbFbOCp+mtYZTJtCMgdaly0Mt89vg/mB3M0C5oaDgAAAAASUVORK5CYII=',
        NULL, NULL);

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('TCPLog', 'Send request and response logs to a TCP server', '{
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
}', 'JsonSchema', 'fa-random', 'TCP Log Policy', NULL, TRUE, FALSE, FALSE, NULL, NULL,
        'iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAMAAAAOusbgAAAAbFBMVEXa2tra2tra2tra2tra2tra2tr////a2toAwP/Z8v//5sz/gQD/gAC6z9rryK4Apdz2bACi4v//8eL/vnf1/P/7+ff/jwP/0J3k9v8Aw///qkp11f9bzv9Byf8exf//nSrD6/+Q3P//5Md21f9CFRjSAAAABnRSTlMCzPZuG2+a9LtCAAAB40lEQVRo3u3b6YqDMBSG4a6n6aSzaNRW7T73f48TlfIhVTAqJzie93fLg8tRIWRhWy9XO8ZWy/WiaLPcsbfcWJjThWzP885L6+qAY2Isrg55xelCXi0KnpgrTIGbElhggQUWWODWBBaYKFDqwQnDteWRG/xtU7XS/OQIq7Jzmxxm+lDrxwZYoaujrKoup2YXbAuM8l6wuu6poezQHU7d4Uv5vzu9p5vhT9u+lrK5w9FZFQX0VkF91PqyjQZTlJfyYyQ4UB0joofCWA2A3VyFX5+jEeBOJq7tPcVYccC4p/ZXjNVgmFw6YaxYYPQaq9nAr1M96ZurxzhN+AHC/8h0KKjg6HeUl4ST7PW1iDFihjFGvDDGiBXGGPHCGCNmGGPEByOB/wdstDYe4DjTNsMOhzddxg2HifYCPxPtBT6WZsIKw72F3LAp3SwmNhhjVM2R7pgZDGOMjgS4gzwcDhO4ZDrLg2CMUfLE1WaAMUYhuTQYxhjRbOB+p3rSN1fvcZrwA4T7kdn+ktDG02vRzOJDoBqreX3sjf1567IYEme9YH0Yvvxj+sAZxJEXvNCwBa807+u6LPFNfBlXYIEFFrglgQUWWGCBBW5N4FFgb5twvG078rbRytfWMo+b6Wxb7u2DW4v+AenYnnqPew4CAAAAAElFTkSuQmCC',
        'Request and response logs are sent to a server via TCP',
        '<p class="text-light">Logs are sent to <b>{{host}}:{{port}}</b>.</p><p class="text-light">Timeout in ms: {{timeout}}.</p>');

INSERT INTO public.policydefs (id, description, form, form_type, icon, name, plugin_id, scope_service, scope_plan, scope_auto, form_override, default_config, logo, marketplace_description, popover_template)
VALUES ('UDPLog', 'Send request and response logs to a UDP server', '{
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
}', 'JsonSchema', 'fa-crosshairs', 'UDP Log Policy', NULL, TRUE, FALSE, FALSE, NULL, NULL,
        'iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAMAAAAOusbgAAAAVFBMVEXa2tra2tra2tra2tra2tra2tr////a2toAfd6izPLvzPnRfvDYteSKr86zas0Aar4AhODY6vr3+Prx8v2Kv+9aqOk3muUOj+N5t+211vXhqfW01fXvn55GAAAABnRSTlMC9s/Hbhsvz/I3AAABVklEQVRo3u3b3Y6CMBCG4SJYhnV/KD+K7v3f57bN7AFJTcDUmZB+74lH5EmMA5hmjK+pq1awqm5M6HxqxTudPSzssmxM06rUmDp8DFawIYi1qYRdlisTeCtcMAGnAgwYMGDAgJ8GGPDB4B8frepnl9cZH5d1374E7GmX1WVuA0xzTvixA+5zwpc0/OXrVgU5N/yx6tMHGDBgwIABvxmeiBZhmF3fPMjDFLuOSjDdnBJMvVOAb1G+y8PjlUKdOGyHOcpLJniiDfEVC/FYZYA3unxFx2OVAd7sTjZ073msRGB2Yy7KvcsC2z05Hitx2P6PVTEwf9W/h/5xvTBOB76ByN8ydzRRzofELln1schjVNCrTxyjsl5vtV7ol7L+tAEGDLhMWOAw5ADHPxIHXmpHfAWepgJOBBgwYMCAAT8NMGDAgJOw2hKO2tqR2qKV1mqZ3jKd2vrgH/W3idgykdWgAAAAAElFTkSuQmCC',
        'Request and response logs are sent to a server via UDP',
        '<p class="text-light">Logs are sent to <b>{{host}}:{{port}}</b>.</p><p class="text-light">Timeout in ms: {{timeout}}.</p>');

-- CONFIG

INSERT INTO config (id, config_path) VALUES (7, '/opt/wildfly/standalone/configuration/application.conf');

-- OPERATING MODES

INSERT INTO operating_modes VALUES ('MAINTENANCE', FALSE);

INSERT INTO defaults (id, service_terms) VALUES ('DEV', 'This is a default terms & conditions
====================================
[![][t1t-logo]][Trust1Team-url]

Kong is a scalable, open source API Layer *(also known as an API Gateway, or
API Middleware)*. Kong was originally built at [Mashape][mashape-url] to
secure, manage and extend over [15,000 APIs &
Microservices](http://stackshare.io/mashape/how-mashape-manages-over-15000-apis-and-microservices)
for its API Marketplace, which generates billions of requests per month.

Backed by the battle-tested **NGINX** with a focus on high performance, Kong
was made available as an open-source platform in 2015. Under active
development, Kong is now used in production at hundreds of organizations from
startups, to large enterprises and government departments.

The JWT upstream plugin has been developed to provide a JWT towards upstream APIs - registered on Kong - without worrying about the authentication plugin used by a consumer.

More information can be found on the [jwt-up wiki page][jwt-up-doc]

[Website Trust1Team][Trust1Team-url]

[Website Kong][kong-url]

## Summary

Only works from Kong 0.8.0.
You can not use the JWT-up plugin in combination with the JWT plugin. This is because the JWT-up by default validates HS256 incoming JWT and implicitly generates a RS256 token for upstream APIs.
The JWT-up can be used in order to have 2 types of consumers:
- consumer applications, using a key-auth policy to reqeust a service
- end-user consumers, using a consumer application, in order to request a service

Both authentication means can be different:
- consumer applications: uses only key-auth
- end-user consumer: uses JWT received after successful login

Both will result - after applying the JWT-up - in a RS256 JWT - enriched - send to upstream API.

## Roadmap

Update to Kong 0.9.0
At the moment we need to provide the certificates hardcodes in the fixtures files.
This is due to a cert upload issue we have using Kong 0.8.0

## Development

TBD

## License

```
This file is part of the Trust1Team(R) sarl project.
 Copyright (c) 2014 Trust1Team sarl
 Authors: Trust1Team development


This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation with the addition of the
 following permission added to Section 15 as permitted in Section 7(a):
 FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY Trust1T,
 Trust1T DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.

 This program is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU Affero General Public License for more details.
 You should have received a copy of the GNU Affero General Public License
 along with this program; if not, see http://www.gnu.org/licenses or write to
 the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 Boston, MA, 02110-1301 USA.

 The interactive user interfaces in modified source and object code versions
 of this program must display Appropriate Legal Notices, as required under
 Section 5 of the GNU Affero General Public License.


You can be released from the requirements of the Affero General Public License
 by purchasing
 a commercial license. Buying such a license is mandatory if you wish to develop commercial activities involving the Trust1T software without
 disclosing the source code of your own applications.
 Examples of such activities include: offering paid services to customers as an ASP,
 Signing PDFs on the fly in a web application, shipping OCS with a closed
 source product...
Irrespective of your choice of license, the T1T logo as depicted below may not be removed from this file, or from any software or other product or service to which it is applied, without the express prior written permission of Trust1Team sarl. The T1T logo is an EU Registered Trademark (n° 12943131).
```

[kong-url]: https://getkong.org/
[Trust1Team-url]: http://trust1team.com
[t1t-logo]: http://imgur.com/lukAaxx.png
[jwt-up-doc]: https://trust1t.atlassian.net/wiki/pages/viewpage.action?pageId=74547210
[mashape-url]: https://www.mashape.com/');

-- GATEWAYS - Insert your PEM encoded private key in the insert statement

INSERT INTO gateways (id, configuration, endpoint, created_by, created_on, description, modified_by, modified_on, name, type, jwt_exp_time, oauth_exp_time, jwt_pub_key, jwt_pub_key_endpoint, jwt_priv_key)
VALUES
  ('KongGateway', '{"endpoint":"http://localhost:8001","username":"","password":""}', 'http://localhost:8000', '',
                  CURRENT_DATE, 'This is the gateway.', 'admin', CURRENT_DATE, 'Default Kong Gateway', 'REST', 7200,
   7200, '-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmUfvkZOft2YeSFq/XPMq
azKYXrkN3pMa+6xpUf81BZd56XS3S6McJUcfyz4a9ff30n3UIwBdkfn2TWYRa2RM
jZhrdHyV1cyBflQGw3ZfLNffAhxneqtVkNo2P013eOnTLelNHhtPQ4hnxh1UKE9f
KARHirlSzwqaRgBIkS5p3eTwCzw4gbcIXQ6QPhI/vw23bjCBemNN8+DlVQYqTich
h5ATxxwI/sw0t2g5argJY2lQGqxnkKEN3FIxQcIxMvmgBCqRH17WEOZPzRMABFGM
yXUiOBUtXSNsNdInee7+pzSg7Wyu1jOIfDiBZhesWIePeAyQ9CxUN+W6V7iEpkZY
cQIDAQAB
-----END PUBLIC KEY-----', '/keys/pub', '-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCZR++Rk5+3Zh5I
Wr9c8yprMpheuQ3ekxr7rGlR/zUFl3npdLdLoxwlRx/LPhr19/fSfdQjAF2R+fZN
ZhFrZEyNmGt0fJXVzIF+VAbDdl8s198CHGd6q1WQ2jY/TXd46dMt6U0eG09DiGfG
HVQoT18oBEeKuVLPCppGAEiRLmnd5PALPDiBtwhdDpA+Ej+/DbduMIF6Y03z4OVV
BipOJyGHkBPHHAj+zDS3aDlquAljaVAarGeQoQ3cUjFBwjEy+aAEKpEfXtYQ5k/N
EwAEUYzJdSI4FS1dI2w10id57v6nNKDtbK7WM4h8OIFmF6xYh494DJD0LFQ35bpX
uISmRlhxAgMBAAECggEAHnVz/qqmH0yUke4ufqr3b+vS6We+gM8cmoxNNXUQNTc/
i4uc7NPRwPqcL8f8lYob3eERKY9mCJ7yObkhlwSMTcrO94vWPnDv/ol1+ptoGEIh
V4sLaQ3o5+rkwrmDFyznwQTORUBKBPJzJKQpWAVFtG79RGzFLs09kNF3mhnuAaCw
9ElntW98DrIX98lvSx/CQ8eIPZfO5B7shJDbpI6k8npFtUhsdi40l5dYbWjAOyLc
HJbajUsOdAohb3bUXTFSaP74Q9gmBGGZKBzTbg45yJaStem8Sbs0C69o8H9n+zOw
CZ5Sg6iGvN7yRyd+ACL/9iw2tCAkBWIiEYSd/zHoAQKBgQDG0AacHFdByr1aGgim
Q57M99tMbZmQ6xPoPnIY8dEKZRLHCe3FhSLMKin/8+ptPl8rqB9am2D7VIh5s8mA
zJv4hEPWp3repeNA11L2+c3resnqgQFdYGMgM7N0KDnSOHF/XyJVzcscRvrfboQT
X0lLzsv9fFKTCNo1/Fftuq0R8QKBgQDFXxjVF2iXmy4H97Osxl3igpBWMKl9lM4n
0GTEh+lQuphYSWUxFWUufMVywD187kWfBeKEzpx8xwjO73Kq2yNCdCN8FDf8SF0A
LuUZR9Ao97JoCx/MsgDqZCSf8ExAfbVduLRMnBMP2V4fp8WBRh+x5a0RgW1IED0V
nQAUokcugQKBgQCivLM8Y+J040xdjYPY/O7m3leu1uPmANa5uyvQCVjQRm25aQIt
NU6Hilf4wCL7Kxvh82s+0oLvF4HjBOyDNnjRUwrokQz9ZRKBCABMU6xf/ZF8F/8S
/QwG1uskwKybzEHzPd8wFxUT33IJJECOTugDWpIVuwcofg2INTeQS8bRMQKBgCfJ
Sr0x8dCV5IqQoTST7DsL7MKjHpeFFqWp+ZYwe5TdeHZbnT1uva3IUxkVrY9wjLJI
wmfZKmxwxtLILApLEGmF9d0+SibeA3lU+rPE6S5I+DrG+0sNGugk9ckT7JsXfCb/
Y8EJuUH5pGKnNEYh96Op0+BygbAELfwcYPbH26eBAoGBAKP1NfY2wFb772DcxTSE
Rj+pb6cHZe8yY1q2y1LAdY07xSHnvGJq8UbklGlgc72aPF8XtF20O8o/yaZP5Gdh
HI5PPa7GK7nyyLGCpfARLMcnVTK6F94UAf1N6uH3uruOvYnWLW9lNCWVahBMsR0X
4jNpp7Cs/XG6xoRkEaTa60FT
-----END PRIVATE KEY-----');

INSERT INTO users (username, email, full_name, joined_on, admin, pic)
VALUES ('ad955d6f-c15a-4ab2-adfd-fbde8251a032', 'dummy-mail@dummy-domain.com', 'Admin', CURRENT_DATE, TRUE, NULL);

INSERT INTO managed_applications (id, name, version, type, prefix, activated, restricted, app_id)
VALUES (901, 'External Marketplace', 'v1', 'ExternalMarketplace', 'ext', TRUE, FALSE, 'marketplace');
INSERT INTO managed_applications (id, name, version, type, prefix, activated, restricted, app_id)
VALUES (902, 'Internal Marketplace', 'v1', 'InternalMarketplace', 'int', TRUE, FALSE, 'marketplace');
INSERT INTO managed_applications (id, name, version, type, prefix, activated, restricted, app_id)
VALUES (903, 'Publisher', 'v1', 'Publisher', 'pub', TRUE, FALSE, 'publisher');
INSERT INTO managed_applications (id, name, version, type, prefix, activated, restricted, app_id)
VALUES (904, 'Admin Application', 'v1', 'Admin', 'admin', TRUE, FALSE, 'adminapp');

INSERT INTO managed_application_keys (managed_app_id, api_key) VALUES (901, 'ext-mkt-api-key');
INSERT INTO managed_application_keys (managed_app_id, api_key) VALUES (902, 'int-mkt-api-key');
INSERT INTO managed_application_keys (managed_app_id, api_key) VALUES (903, 'pub-api-key');

-- INSERT default templates
INSERT INTO mail_templates (topic, subject, content, created_on, updated_on) VALUES
  ('MEMBERSHIP_REQUEST', 'API Engine ({environment}) - request membership for {orgFriendlyName} ({orgName})',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>The following user requests membership for your organization: {orgFriendlyName} ({orgName})</p><p>- Username: {userId}</p><p>- Email : {userMail}</p><p>You can add the user in the Members-tab of your organization.</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',
   CURRENT_DATE, CURRENT_DATE);
INSERT INTO mail_templates (topic, subject, content, created_on, updated_on) VALUES
  ('MEMBERSHIP_APPROVE', 'API Engine ({environment}) - approve membership for {orgFriendlyName} ({orgName})',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>Your membership request for {orgFriendlyName} ({orgName}) has been granted.</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',
   CURRENT_DATE, CURRENT_DATE);
INSERT INTO mail_templates (topic, subject, content, created_on, updated_on) VALUES
  ('MEMBERSHIP_REJECT', 'API Engine ({environment}) - reject membership for {orgFriendlyName} ({orgName})',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>Your membership request for {orgFriendlyName} ({orgName}) has been rejected.</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',
   CURRENT_DATE, CURRENT_DATE);
INSERT INTO mail_templates (topic, subject, content, created_on, updated_on) VALUES
  ('MEMBERSHIP_GRANTED', 'API Engine ({environment}) - granted membership for {orgFriendlyName} ({orgName})',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>You have been granted for {orgFriendlyName} ({orgName}).</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',
   CURRENT_DATE, CURRENT_DATE);
INSERT INTO mail_templates (topic, subject, content, created_on, updated_on) VALUES
  ('MEMBERSHIP_NEW', 'API Engine ({environment}) - Welcome to {orgFriendlyName} ({orgName})',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>You have been invited to join {orgFriendlyName} ({orgName}).</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',
   CURRENT_DATE, CURRENT_DATE);
INSERT INTO mail_templates (topic, subject, content, created_on, updated_on) VALUES
  ('MEMBERSHIP_DELETED', 'API Engine ({environment}) - Membership deleted for {orgFriendlyName} ({orgName})',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>Your membership has been deleted for {orgFriendlyName} ({orgName}).</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',
   CURRENT_DATE, CURRENT_DATE);
INSERT INTO mail_templates (topic, subject, content, created_on, updated_on) VALUES
  ('MEMBERSHIP_UPDATE_ROLE', 'API Engine ({environment}) - Updated role ({role}) for {orgFriendlyName} ({orgName})',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>Your role for {orgFriendlyName} ({orgName}) is updated:</p><p>- {role} </p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',
   CURRENT_DATE, CURRENT_DATE);
INSERT INTO mail_templates (topic, subject, content, created_on, updated_on) VALUES
  ('MEMBERSHIP_ADMIN_NEW', 'API Engine ({environment}) - Admin privileges assigned',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>You have been added as administrator for the API Engine.</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',
   CURRENT_DATE, CURRENT_DATE);
INSERT INTO mail_templates (topic, subject, content, created_on, updated_on) VALUES
  ('MEMBERSHIP_ADMIN_DELETED', 'API Engine ({environment}) - Admin privileges revoked',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>Your administrator privileges have been revoked.</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',
   CURRENT_DATE, CURRENT_DATE);
INSERT INTO mail_templates (topic, subject, content, created_on, updated_on) VALUES
  ('ORGANIZATION_TRANSFER', 'API Engine ({environment}) - Transfer ownership for {orgFriendlyName} ({orgName})',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>You have been assigned owner for {orgFriendlyName} ({orgName}).</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',
   CURRENT_DATE, CURRENT_DATE);
INSERT INTO mail_templates (topic, subject, content, created_on, updated_on) VALUES
  ('CONTRACT_REQUEST', 'API Engine ({environment}) - Contract request for {serviceName}',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h3>Hi,</h3><p>There is a contract request from {userId} ({userMail}) pending for:</p><p><strong>Application</strong></p><p>{appName} with version {appVersion} from {appOrgName}</p><p><strong>Service</strong>&nbsp;</p><p>{serviceName} with version {serviceVersion} from {serviceOrgName}</p><p><strong>Contract Plan</strong></p><p>{planName} with version {planVersion} </p><p>&nbsp;</p><p>You have privileges to ''accept'' or ''reject'' the request in the API Publisher.</p><p>When the contract is ''accepted'', the contract will be activated for use. </p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',
   CURRENT_DATE, CURRENT_DATE);
INSERT INTO mail_templates (topic, subject, content, created_on, updated_on) VALUES
  ('CONTRACT_APPROVE', 'API Engine ({environment}) - Contract approved for {serviceName}',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h3>Hi,</h3><p>The following contract has been ''approved'' by {userId} ({userMail}):</p><p><strong>Application</strong></p><p>{appName} ({appVersion}) from {appOrgName}</p><p><strong>Service</strong>&nbsp;</p><p>{serviceName} ({serviceVersion}) from {serviceOrgName}</p><p><strong>Contract Plan</strong></p><p>{planName}</p><p>&nbsp;</p><p>The issued keys  for your application can be retrieved in the API Store; they have been activated to consumer the ''approved'' service.</p><p>&nbsp;</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',
   CURRENT_DATE, CURRENT_DATE);
INSERT INTO mail_templates (topic, subject, content, created_on, updated_on) VALUES
  ('CONTRACT_REJECT', 'API Engine ({environment}) - Contract rejected for {serviceName}',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h3>Hi,</h3><p>The following contract has been ''rejected'' by {userId} ({userMail}):</p><p><strong>Application</strong></p><p>{appName} ({appVersion}) from {appOrgName}</p><p><strong>Service</strong>&nbsp;</p><p>{serviceName} ({serviceVersion}) from {serviceOrgName}</p><p><strong>Contract Plan</strong></p><p>{planName}</p><p>&nbsp;</p><p>The requested contract has been removed from your applications in the API Store.</p><p>&nbsp;</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',
   CURRENT_DATE, CURRENT_DATE);
INSERT INTO mail_templates (topic, subject, content, created_on, updated_on) VALUES
  ('STATUS', 'API Engine - status mail ({environment})',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>API Engine status information has not yet been implemented.</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',
   CURRENT_DATE, CURRENT_DATE);
INSERT INTO mail_templates (topic, subject, content, created_on, updated_on) VALUES
  ('TEST', 'API Engine server restart ({environment})',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>Signbox - Template</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td></td><td class="container" bgcolor="#FFFFFF"><div class="content"><table><tr><td><h2>Hi,</h2><p>This is a test message - no further action required.</p><table class="btn-primary" cellpadding="0" cellspacing="0" border="0"><tr> </tr></table></td></tr></table></div></td><td></td></tr></table></body></html>',
   CURRENT_DATE, CURRENT_DATE);

INSERT INTO public.idp_issuers (issuer, jwks_uri) VALUES ('http://localhost:28080/auth/realms/Trust1Gateway', 'http://localhost:28080/auth/realms/Trust1Gateway/protocol/openid-connect/certs');