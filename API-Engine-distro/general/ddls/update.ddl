-- Update marketplace
UPDATE managed_applications SET name='Internal Marketplace' WHERE availability='int';
UPDATE managed_applications SET name='External Marketplace' WHERE availability='ext';
INSERT INTO availabilities(code, name) VALUES ('pub', 'publisher');
UPDATE managed_applications SET availability='pub' WHERE type='Publisher';

-- Add extra column context - PostgreSQL does not support adding a column with a default value in a single command line.
ALTER TABLE public.organizations ADD COLUMN context VARCHAR(255);
UPDATE public.organizations SET context = 'none';
ALTER TABLE public.organizations ALTER COLUMN context SET DEFAULT 'none';
ALTER TABLE public.organizations ALTER COLUMN context SET NOT NULL;