-- Update marketplace
UPDATE managed_applications SET name='Internal Marketplace' WHERE availability='int';
UPDATE managed_applications SET name='External Marketplace' WHERE availability='ext';
UPDATE managed_applications SET availability='pub' WHERE type='Publisher';

-- Add extra column context - PostgreSQL does not support adding a column with a default value in a single command line.
ALTER TABLE public.organizations ADD COLUMN context VARCHAR(255);
UPDATE public.organizations SET context = 'pub';
ALTER TABLE public.organizations ALTER COLUMN context SET DEFAULT 'pub';
ALTER TABLE public.organizations ALTER COLUMN context SET NOT NULL;

-- remove foreign key to availabilities and gateways
ALTER TABLE public.managed_applications DROP CONSTRAINT fk_67jdhkwjqd78t8kcsil9c3dk2;
ALTER TABLE public.managed_applications DROP CONSTRAINT fk_67jdhkwjqd78t8kcsil9c3dk1;

-- drop availabilities
DROP TABLE public.availabilities;

-- update managed apps
ALTER TABLE public.managed_applications ALTER COLUMN type TYPE VARCHAR(255);
ALTER TABLE public.managed_applications ALTER COLUMN type SET NOT NULL;

ALTER TABLE public.managed_applications ALTER COLUMN api_key TYPE VARCHAR(255);
ALTER TABLE public.managed_applications ALTER COLUMN api_key SET NOT NULL;

ALTER TABLE public.managed_applications RENAME availability TO prefix;
ALTER TABLE public.managed_applications ALTER COLUMN prefix TYPE VARCHAR(255);
ALTER TABLE public.managed_applications ALTER COLUMN prefix SET NOT NULL;

/* PostgreSQL does not support adding a column with a default value in a single command line. */
ALTER TABLE public.managed_applications ADD COLUMN activated BOOL;
UPDATE public.managed_applications SET activated = true;
ALTER TABLE public.managed_applications ALTER COLUMN activated SET DEFAULT true;
ALTER TABLE public.managed_applications ALTER COLUMN activated SET NOT NULL;

/* PostgreSQL does not support adding a column with a default value in a single command line. */
ALTER TABLE public.managed_applications ADD COLUMN restricted BOOL;
UPDATE public.managed_applications SET restricted = false;
ALTER TABLE public.managed_applications ALTER COLUMN restricted SET DEFAULT false;
ALTER TABLE public.managed_applications ALTER COLUMN restricted SET NOT NULL;