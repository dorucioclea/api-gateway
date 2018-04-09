UPDATE policydefs SET form = '{
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
}', form_override = '[
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
]', default_config = '{"methods":["HEAD","DELETE","GET","POST","PUT","PATCH"],"credentials":false,"exposed_headers":[],"max_age":3600.0,"preflight_continue":false,"origins":["*"],"headers":["Accept","Accept-Version","Content-Length","Content-MD5","Content-Type","Date","apikey","Authorization"]}' WHERE id = 'CORS';


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