UPDATE policydefs set form='{
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
}' WHERE id = 'OAuth2';