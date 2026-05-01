CREATE TABLE souscription
(
    id INT8 PRIMARY KEY,
    subscription_reference VARCHAR(16) NOT NULL UNIQUE,
    simulation_id INT8 NOT NULL,
    assure_id INT8 NOT NULL,
    vehicule_id INT8 NOT NULL,
    utilisateur_id INT8,
    statut VARCHAR(50) NOT NULL,
    numero_attestation VARCHAR(16) NOT NULL UNIQUE,
    date_souscription TIMESTAMP NOT NULL,
    create_at TIMESTAMP,
    create_by VARCHAR(255),
    update_at TIMESTAMP,
    update_by VARCHAR(255),
    version INT8 NOT NULL DEFAULT 0,
    CONSTRAINT souscription_simulation_fk FOREIGN KEY (simulation_id) REFERENCES simulation (id),
    CONSTRAINT souscription_assure_fk FOREIGN KEY (assure_id) REFERENCES assure (id),
    CONSTRAINT souscription_vehicule_fk FOREIGN KEY (vehicule_id) REFERENCES vehicule (id),
    CONSTRAINT souscription_utilisateur_fk FOREIGN KEY (utilisateur_id) REFERENCES utilisateur (id)
);
CREATE SEQUENCE souscription_id_seq INCREMENT BY 50 START 1;
ALTER TABLE souscription ALTER COLUMN id SET DEFAULT nextval('souscription_id_seq');
