-- Update marketplace
UPDATE managed_applications SET name='Internal Marketplace' WHERE availability='int';
UPDATE managed_applications SET name='External Marketplace' WHERE availability='ext';
INSERT INTO availabilities(code, name) VALUES ('pub', 'publisher');
UPDATE managed_applications SET availability='pub' WHERE type='Publisher';
