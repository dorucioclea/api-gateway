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
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('CONTRACT_REQUEST', 'API Engine ({environment}) - Contract request for {serviceName}', '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h3>Hi,</h3><p>There is a contract request from {userId} ({userMail}) pending for:</p><p><strong>Application</strong></p><p>{appName} with version {appVersion} from {appOrgName}</p><p><strong>Service</strong>&nbsp;</p><p>{serviceName} with version {serviceVersion} from {serviceOrgName}</p><p><strong>Contract Plan</strong></p><p>{planName} with version {planVersion} </p><p>&nbsp;</p><p>You have privileges to ''accept'' or ''reject'' the request in the API Publisher.</p><p>When the contract is ''accepted'', the contract will be activated for use. </p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('CONTRACT_APPROVE', 'API Engine ({environment}) - Contract approved for {serviceName}', '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h3>Hi,</h3><p>The following contract has been ''approved'' by {userId} ({userMail}):</p><p><strong>Application</strong></p><p>{appName} ({appVersion}) from {appOrgName}</p><p><strong>Service</strong>&nbsp;</p><p>{serviceName} ({serviceVersion}) from {serviceOrgName}</p><p><strong>Contract Plan</strong></p><p>{planName}</p><p>&nbsp;</p><p>The issued keys  for your application can be retrieved in the API Store; they have been activated to consumer the ''approved'' service.</p><p>&nbsp;</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('CONTRACT_REJECT', 'API Engine ({environment}) - Contract rejected for {serviceName}', '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h3>Hi,</h3><p>The following contract has been ''rejected'' by {userId} ({userMail}):</p><p><strong>Application</strong></p><p>{appName} ({appVersion}) from {appOrgName}</p><p><strong>Service</strong>&nbsp;</p><p>{serviceName} ({serviceVersion}) from {serviceOrgName}</p><p><strong>Contract Plan</strong></p><p>{planName}</p><p>&nbsp;</p><p>The requested contract has been removed from your applications in the API Store.</p><p>&nbsp;</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('STATUS', 'API Engine - status mail ({environment})','<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>API Engine</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi,</h2><p>API Engine status information has not yet been implemented.</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the APIe-Team.</p></div></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );
INSERT INTO mail_templates(topic, subject, content, created_on,updated_on) VALUES('TEST', 'API Engine server restart ({environment})','<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>Signbox - Template</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td></td><td class="container" bgcolor="#FFFFFF"><div class="content"><table><tr><td><h2>Hi,</h2><p>This is a test message - no further action required.</p><table class="btn-primary" cellpadding="0" cellspacing="0" border="0"><tr> </tr></table></td></tr></table></div></td><td></td></tr></table></body></html>',CURRENT_DATE ,CURRENT_DATE );


ALTER TABLE managed_applications DROP CONSTRAINT FK_67jdhkwjqd78t8kcsil9c3dk5;
ALTER TABLE managed_applications DROP CONSTRAINT FK_67jdhkwjqd78t8kcsil9c3dk6;
ALTER TABLE services DROP CONSTRAINT FK_31hj3xmhp1wedxjh5bklnlg15;
ALTER TABLE contracts DROP CONSTRAINT FK_6h06sgs4dudh1wehmk0us973g;
ALTER TABLE service_defs DROP CONSTRAINT FK_81fuw1n8afmvpw4buk7l4tyxk;
ALTER TABLE application_versions DROP CONSTRAINT FK_8epnoby31bt7xakegakigpikp;
ALTER TABLE contracts DROP CONSTRAINT FK_8o6t1f3kg96rxy5uv51f6k9fy;
ALTER TABLE service_versions DROP CONSTRAINT FK_92erjg9k1lni97gd87nt6tq37;
ALTER TABLE endpoint_properties DROP CONSTRAINT FK_gn0ydqur10sxuvpyw2jvv4xxb;
ALTER TABLE applications DROP CONSTRAINT FK_jenpu34rtuncsgvtw0sfo8qq9;
ALTER TABLE policies DROP CONSTRAINT FK_l4q6we1bos1yl9unmogei6aja;
ALTER TABLE oauth_apps DROP CONSTRAINT FK_l5q6we1bos1yl98nmogei7aja;
ALTER TABLE plans DROP CONSTRAINT FK_lwhc7xrdbsun1ak2uvfu0prj8;
ALTER TABLE contracts DROP CONSTRAINT FK_nyw8xu6m8cx4rwwbtrxbjneui;
ALTER TABLE svc_gateways DROP CONSTRAINT FK_p5dm3cngljt6yrsnvc7uc6a75;
ALTER TABLE permissions DROP CONSTRAINT FK_sq51ihfrapwdr98uufenhcocg;
ALTER TABLE svc_plans DROP CONSTRAINT FK_t7uvfcsswopb9kh8wpa86blqr;
ALTER TABLE svc_visibility DROP CONSTRAINT FK_svc_version_visibility;
ALTER TABLE plan_versions DROP CONSTRAINT FK_tonylvm2ypnq3efxqr1g0m9fs;
ALTER TABLE followers DROP CONSTRAINT FK_29hj3xmhp1wedxjh1bklnlg15;
ALTER TABLE mail_templates DROP constraint mail_templates_pkey CASCADE;
ALTER TABLE events DROP constraint events_pkey CASCADE;
ALTER TABLE followers DROP constraint followers_pkey CASCADE;
ALTER TABLE endpoint_properties DROP constraint endpoint_properties_pkey CASCADE;
ALTER TABLE svc_gateways DROP constraint svc_gateways_pkey CASCADE;
ALTER TABLE availabilities DROP constraint availabilities_pkey CASCADE ;
ALTER TABLE white_ip_restriction constraint white_ip_restriction_pkey CASCADE;
ALTER TABLE black_ip_restriction constraint black_ip_restriction_pkey CASCADE;
ALTER TABLE svc_plans DROP constraint svc_plans_pkey CASCADE;
ALTER TABLE svc_visibility DROP constraint svc_visibility_pkey CASCADE;
ALTER TABLE application_versions DROP constraint application_versions_pkey CASCADE;
ALTER TABLE applications DROP constraint applications_pkey CASCADE;
ALTER TABLE auditlog DROP constraint auditlog_pkey CASCADE;
ALTER TABLE announcements DROP constraint announcements_pkey CASCADE;
ALTER TABLE contracts DROP constraint contracts_pkey CASCADE;
ALTER TABLE gateways DROP constraint gateways_pkey CASCADE;
ALTER TABLE memberships DROP constraint memberships_pkey CASCADE;
ALTER TABLE organizations DROP constraint organizations_pkey CASCADE;
ALTER TABLE plan_versions DROP constraint plan_versions_pkey CASCADE;
ALTER TABLE plans DROP constraint plans_pkey CASCADE;
ALTER TABLE plugins DROP constraint plugins_pkey CASCADE;
ALTER TABLE policies DROP constraint policies_pkey CASCADE;
ALTER TABLE oauth_apps DROP constraint oauth_apps_pkey CASCADE;
ALTER TABLE policydefs DROP constraint policydefs_pkey CASCADE;
ALTER TABLE roles DROP constraint roles_pkey CASCADE;
ALTER TABLE service_defs DROP constraint service_defs_pkey CASCADE;
ALTER TABLE service_versions DROP constraint service_versions_pkey CASCADE;
ALTER TABLE services DROP constraint services_pkey CASCADE;
ALTER TABLE users DROP constraint users_pkey CASCADE;
ALTER TABLE support DROP constraint support_pkey CASCADE;
ALTER TABLE support_comments DROP constraint support_comments_pkey CASCADE;
ALTER TABLE managed_applications DROP constraint managed_applications_pkey CASCADE;

update organizations SET id=lower(id);
update applications SET organization_id=lower(organization_id);
update application_versions SET app_org_id=lower(app_org_id);
update categories SET servicebean_organization_id=lower(servicebean_organization_id);
update auditlog SET organization_id=lower(organization_id);
update followers SET servicebean_organization_id=lower(servicebean_organization_id);
update memberships SET org_id=lower(org_id);
update plan_versions SET plan_org_id=lower(plan_org_id);
update plans SET organization_id=lower(organization_id);
update policies SET organization_id=lower(organization_id);
update service_versions SET service_org_id=lower(service_org_id);
update services SET organization_id=lower(organization_id);
update support SET organization_id=lower(organization_id);

DELETE FROM organizations WHERE id like 'testorg%';
DELETE FROM applications WHERE organization_id like 'testorg%';
DELETE FROM application_versions WHERE app_org_id like 'testorg%';
DELETE FROM categories WHERE servicebean_organization_id like 'testorg%';
DELETE FROM auditlog WHERE organization_id like 'testorg%';
DELETE FROM followers WHERE servicebean_organization_id like 'testorg%';
DELETE FROM memberships WHERE org_id like 'testorg%';
DELETE FROM plan_versions WHERE plan_org_id like 'testorg%';
DELETE FROM plans WHERE organization_id like 'testorg%';
DELETE FROM policies WHERE organization_id like 'testorg%';
DELETE FROM service_versions WHERE service_org_id like 'testorg%';
DELETE FROM services WHERE organization_id like 'testorg%';
DELETE FROM support WHERE organization_id like 'testorg%';
-- Delete orphaned contracts
DELETE FROM contracts WHERE appv_id NOT IN (SELECT id FROM application_versions);
DELETE FROM contracts WHERE svcv_id NOT IN (SELECT id FROM service_versions);


ALTER TABLE mail_templates ADD CONSTRAINT pksb_mail_templates PRIMARY KEY (topic);
ALTER TABLE events ADD PRIMARY KEY (id);
ALTER TABLE followers ADD PRIMARY KEY (ServiceBean_id,ServiceBean_organization_id,user_id);
ALTER TABLE endpoint_properties ADD PRIMARY KEY (service_version_id, name);
ALTER TABLE svc_gateways ADD PRIMARY KEY (service_version_id, gateway_id);
ALTER TABLE availabilities ADD PRIMARY KEY (code);
ALTER TABLE white_ip_restriction ADD PRIMARY KEY (netw_value);
ALTER TABLE black_ip_restriction ADD PRIMARY KEY (netw_value);
ALTER TABLE svc_plans ADD PRIMARY KEY (service_version_id, plan_id, version);
ALTER TABLE svc_visibility ADD PRIMARY KEY (service_version_id, code);
ALTER TABLE application_versions ADD PRIMARY KEY (id);
ALTER TABLE applications ADD PRIMARY KEY (id, organization_id);
ALTER TABLE auditlog ADD PRIMARY KEY (id);
ALTER TABLE announcements ADD PRIMARY KEY (id);
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
ALTER TABLE support ADD PRIMARY KEY (id);
ALTER TABLE support_comments ADD PRIMARY KEY (id);
ALTER TABLE managed_applications ADD PRIMARY KEY (id);


ALTER TABLE managed_applications ADD CONSTRAINT FK_67jdhkwjqd78t8kcsil9c3dk5 FOREIGN KEY (gateway_id) REFERENCES gateways (id) ON UPDATE CASCADE;
ALTER TABLE managed_applications ADD CONSTRAINT FK_67jdhkwjqd78t8kcsil9c3dk6 FOREIGN KEY (availability) REFERENCES availabilities (code) ON UPDATE CASCADE;
ALTER TABLE services ADD CONSTRAINT FK_31hj3xmhp1wedxjh5bklnlg15 FOREIGN KEY (organization_id) REFERENCES organizations (id) ON UPDATE CASCADE;
ALTER TABLE contracts ADD CONSTRAINT FK_6h06sgs4dudh1wehmk0us973g FOREIGN KEY (appv_id) REFERENCES application_versions (id) ON UPDATE CASCADE;
ALTER TABLE service_defs ADD CONSTRAINT FK_81fuw1n8afmvpw4buk7l4tyxk FOREIGN KEY (service_version_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE application_versions ADD CONSTRAINT FK_8epnoby31bt7xakegakigpikp FOREIGN KEY (app_id, app_org_id) REFERENCES applications (id, organization_id) ON UPDATE CASCADE;
ALTER TABLE contracts ADD CONSTRAINT FK_8o6t1f3kg96rxy5uv51f6k9fy FOREIGN KEY (svcv_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE service_versions ADD CONSTRAINT FK_92erjg9k1lni97gd87nt6tq37 FOREIGN KEY (service_id, service_org_id) REFERENCES services (id, organization_id) ON UPDATE CASCADE;
ALTER TABLE endpoint_properties ADD CONSTRAINT FK_gn0ydqur10sxuvpyw2jvv4xxb FOREIGN KEY (service_version_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE applications ADD CONSTRAINT FK_jenpu34rtuncsgvtw0sfo8qq9 FOREIGN KEY (organization_id) REFERENCES organizations (id) ON UPDATE CASCADE;
ALTER TABLE policies ADD CONSTRAINT FK_l4q6we1bos1yl9unmogei6aja FOREIGN KEY (definition_id) REFERENCES policydefs (id) ON UPDATE CASCADE;
ALTER TABLE oauth_apps ADD CONSTRAINT FK_l5q6we1bos1yl98nmogei7aja FOREIGN KEY (app_id) REFERENCES application_versions (id) ON UPDATE CASCADE;
ALTER TABLE plans ADD CONSTRAINT FK_lwhc7xrdbsun1ak2uvfu0prj8 FOREIGN KEY (organization_id) REFERENCES organizations (id) ON UPDATE CASCADE;
ALTER TABLE contracts ADD CONSTRAINT FK_nyw8xu6m8cx4rwwbtrxbjneui FOREIGN KEY (planv_id) REFERENCES plan_versions (id) ON UPDATE CASCADE;
ALTER TABLE svc_gateways ADD CONSTRAINT FK_p5dm3cngljt6yrsnvc7uc6a75 FOREIGN KEY (service_version_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE permissions ADD CONSTRAINT FK_sq51ihfrapwdr98uufenhcocg FOREIGN KEY (role_id) REFERENCES roles (id) ON UPDATE CASCADE;
ALTER TABLE svc_plans ADD CONSTRAINT FK_t7uvfcsswopb9kh8wpa86blqr FOREIGN KEY (service_version_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE svc_visibility ADD CONSTRAINT FK_svc_version_visibility FOREIGN KEY (service_version_id) REFERENCES service_versions (id) ON UPDATE CASCADE;
ALTER TABLE plan_versions ADD CONSTRAINT FK_tonylvm2ypnq3efxqr1g0m9fs FOREIGN KEY (plan_id, plan_org_id) REFERENCES plans (id, organization_id) ON UPDATE CASCADE;
ALTER TABLE followers ADD CONSTRAINT FK_29hj3xmhp1wedxjh1bklnlg15 FOREIGN KEY (ServiceBean_id,ServiceBean_organization_id) REFERENCES services (id,organization_id) ON UPDATE CASCADE;

-- Fix announcements
ALTER TABLE announcements ALTER COLUMN status DROP NOT NULL;
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
}' WHERE id = 'CORS';
UPDATE policydefs SET form = '{
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
}' WHERE id = 'OAuth2';