ALTER TABLE simulation ADD COLUMN IF NOT EXISTS utilisateur_id INT8;

UPDATE simulation s
SET utilisateur_id = u.id
FROM utilisateur u
WHERE s.utilisateur_id IS NULL
  AND s.create_by = u.username;

DO $$
BEGIN
	IF NOT EXISTS (
		SELECT 1
		FROM information_schema.table_constraints
		WHERE constraint_name = 'simulation_utilisateur_fk'
	) THEN
		ALTER TABLE simulation
			ADD CONSTRAINT simulation_utilisateur_fk FOREIGN KEY (utilisateur_id) REFERENCES utilisateur (id);
	END IF;
END $$;
