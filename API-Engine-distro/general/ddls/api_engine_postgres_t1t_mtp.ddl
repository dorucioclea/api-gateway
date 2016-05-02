--  ***********...
--  Update Data...
--  ***********...
--  ***********...

INSERT INTO gateways (id, configuration,endpoint, created_by, created_on, description, modified_by, modified_on, name, type, oauth_authorize, oauth_token, oauth_context,jwt_exp_time) VALUES ('KongGateway', '{"endpoint":"http://apim.t1t.be:8001","username":"","password":""}','https://apim.t1t.be:443', '', CURRENT_DATE, 'This is the gateway.', '', CURRENT_DATE, 'Default Kong Gateway', 'REST', '/oauth2/authorize', '/oauth2/token','/secure',7200);
INSERT INTO users (username, email, full_name, joined_on,admin,pic) VALUES ('admin', 'admin.be@trust1team.com', 'Admin', CURRENT_DATE,TRUE ,NULL );
INSERT INTO organizations (id,description,name,created_by,created_on,modified_by,modified_on) VALUES ('trust1Team','Trust1Team bvba','Trust1Team','admin',CURRENT_DATE,'admin',CURRENT_DATE);
INSERT INTO memberships (id,created_on, org_id, role_id, user_id) VALUES (999,CURRENT_DATE,'digipolis','Owner','admin');

INSERT INTO availabilities (code, name) VALUES ('acpaas', 'acpaas');
INSERT INTO availabilities(name, code) VALUES ('external', 'ext');
INSERT INTO availabilities(name, code) VALUES ('internal', 'int');

INSERT INTO users(username, kong_username, email, full_name, joined_on, admin)VALUES ('guillaume@trust1team.com', '','guillaume@trust1team.com', 'Guillaume Vandecasteele', CURRENT_DATE, TRUE);
INSERT INTO users(username, kong_username, email, full_name, joined_on, admin)VALUES ('michallis@trust1team.com', '','michallis@trust1team.com', 'Michallis Pashidis', CURRENT_DATE, TRUE);
INSERT INTO users(username, kong_username, email, full_name, joined_on, admin)VALUES ('maarten.somers@trust1team.com', '','maarten.somers@trust1team.com', 'Maarten Somers', CURRENT_DATE, TRUE);

INSERT INTO managed_applications (id, name, version, type, availability, api_key) VALUES (901, 'marketplace', 'v1', 'Marketplace', 'ext', '***REMOVED***');
INSERT INTO managed_applications (id, name, version, type, availability, api_key) VALUES (902, 'marketplace', 'v1', 'Marketplace', 'int', '***REMOVED***');
INSERT INTO managed_applications (id, name, version, type, availability, api_key) VALUES (903, 'dev.publisher', 'v1', 'Publisher', NULL, '***REMOVED***');
INSERT INTO managed_applications (id, name, version, type, availability, api_key) VALUES (904, 'consent_app_nodejs', 'v1', 'Consent', 'acpaas', '229e2ea08ba94919c9d221cdf3be1124');

-- INSERT default templates
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_REQUEST', 'API Engine ({environment}) - request membership for {orgFriendlyName} ({orgName})','<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>The following user requests membership for your organization: {orgFriendlyName} ({orgName})</p><p>- Username: {userId}</p><p>- Email : {userMail}</p><p>You can add the user in the Members-tab of your organization.</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_APPROVE', 'API Engine ({environment}) - approve membership for {orgFriendlyName} ({orgName})', '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>Your membership request for {orgFriendlyName} ({orgName}) has been granted.</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_REJECT', 'API Engine ({environment}) - reject membership for {orgFriendlyName} ({orgName})', '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>Your membership request for {orgFriendlyName} ({orgName}) has been rejected.</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_GRANTED', 'API Engine ({environment}) - granted membership for {orgFriendlyName} ({orgName})', '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>You have been granted for {orgFriendlyName} ({orgName}).</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_NEW', 'API Engine ({environment}) - Welcome to {orgFriendlyName} ({orgName})', '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>You have been invited to join {orgFriendlyName} ({orgName}).</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_DELETED', 'API Engine ({environment}) - Membership deleted for {orgFriendlyName} ({orgName})', '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>Your membership has been deleted for {orgFriendlyName} ({orgName}).</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_UPDATE_ROLE', 'API Engine ({environment}) - Updated role ({role}) for {orgFriendlyName} ({orgName})', '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>Your role for {orgFriendlyName} ({orgName}) is updated:</p><p>- {role} </p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_ADMIN_NEW', 'API Engine ({environment}) - Admin privileges assigned', '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>You have been added as administrator for the API Engine.</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('MEMBERSHIP_ADMIN_DELETED', 'API Engine ({environment}) - Admin privileges revoked', '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>Your administrator privileges have been revoked.</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('ORGANIZATION_TRANSFER', 'API Engine ({environment}) - Transfer ownership for {orgFriendlyName} ({orgName})', '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>You have been assigned owner for {orgFriendlyName} ({orgName}).</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('CONTRACT_REQUEST', 'API Engine ({environment}) - Contract request for {serviceName}', '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h3>Hi,</h3><p>Their is a contract request from {userId} ({userMail}) pending for:</p><p><strong>Application</strong></p><p>{appName} with version {appVersion} from {appOrgName}</p><p><strong>Service</strong>&nbsp;</p><p>{serviceName} with version {serviceVersion} from {serviceOrgName}</p><p><strong>Contract Plan</strong></p><p>{planName} with version {planVersion} </p><p>&nbsp;</p><p>You have privileges to ''accept'' or ''reject'' the request in the API Publisher.</p><p>When the contract is ''accepted'', the contract will be activated for use. </p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('CONTRACT_APPROVE', 'API Engine ({environment}) - Contract approved for {serviceName}', '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h3>Hi,</h3><p>The following contract has been ''approved'' by {userId} ({userMail}):</p><p><strong>Application</strong></p><p>{appName} ({appVersion}) from {appOrgName}</p><p><strong>Service</strong>&nbsp;</p><p>{serviceName} ({serviceVersion}) from {serviceOrgName}</p><p><strong>Contract Plan</strong></p><p>{planName}</p><p>&nbsp;</p><p>The issued keys  for your application can be retrieved in the API Store; they have been activated to consumer the ''approved'' service.</p><p>&nbsp;</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('CONTRACT_REJECT', 'API Engine ({environment}) - Contract rejected for {serviceName}', '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h3>Hi,</h3><p>The following contract has been ''rejected'' by {userId} ({userMail}):</p><p><strong>Application</strong></p><p>{appName} ({appVersion}) from {appOrgName}</p><p><strong>Service</strong>&nbsp;</p><p>{serviceName} ({serviceVersion}) from {serviceOrgName}</p><p><strong>Contract Plan</strong></p><p>{planName}</p><p>&nbsp;</p><p>The requested contract has been removed from your applications in the API Store.</p><p>&nbsp;</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('STATUS', 'API Engine - status mail ({environment})','<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>API Engine status information has not yet been implemented.</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('TEST', 'API Engine server restart ({environment})','<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>Signbox - Template</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td></td><td class="container" bgcolor="#FFFFFF"><div class="content"><table><tr><td><h2>Hi,</h2><p>This is a test message - no further action required.</p><table class="btn-primary" cellpadding="0" cellspacing="0" border="0"><tr> </tr></table></td></tr></table></div></td><td></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );