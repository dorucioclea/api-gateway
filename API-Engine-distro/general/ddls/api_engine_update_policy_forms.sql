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
}', 'JsonSchema', 'fa-user', 'Basic Authentication Policy', NULL ,FALSE ,FALSE ,FALSE );

update policydefs set scope_service=FALSE where id='BasicAuthentication';