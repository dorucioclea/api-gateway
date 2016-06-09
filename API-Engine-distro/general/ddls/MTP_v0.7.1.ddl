UPDATE oauth_scopes SET oauth_scopes = 'astad.aprofiel.v1.phone' WHERE serviceversionbean_id = 1038 AND oauth_scopes = 'phone';
UPDATE oauth_scopes SET oauth_scopes = 'astad.aprofiel.v1.name' WHERE serviceversionbean_id = 1038 AND oauth_scopes = 'name';
UPDATE oauth_scopes SET oauth_scopes = 'astad.aprofiel.v1.avatar' WHERE serviceversionbean_id = 1038 AND oauth_scopes = 'avatar';
UPDATE oauth_scopes SET oauth_scopes = 'astad.aprofiel.v1.email' WHERE serviceversionbean_id = 1038 AND oauth_scopes = 'email';
UPDATE oauth_scopes SET oauth_scopes = 'astad.aprofiel.v1.username' WHERE serviceversionbean_id = 1038 AND oauth_scopes = 'username';

UPDATE policies SET configuration = '{"mandatory_scope":true,"token_expiration":7200,"enable_authorization_code":true,"enable_implicit_grant":true,"enable_client_credentials":false,"hide_credentials":false,"scopes":[{"scope":"astad.aprofiel.v1.username","scope_desc":"username"},{"scope":"astad.aprofiel.v1.name","scope_desc":"name"},{"scope":"astad.aprofiel.v1.avatar","scope_desc":"avatar"},{"scope":"astad.aprofiel.v1.email","scope_desc":"email"},{"scope":"astad.aprofiel.v1.phone","scope_desc":"phone"}]}' WHERE id = 1044;