CREATE TABLE assure
(
    id INT8 PRIMARY KEY,
    adresse VARCHAR(255) NOT NULL,
    telephone VARCHAR(50) NOT NULL,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    numero_carte_identite VARCHAR(100) NOT NULL,
    ville VARCHAR(100) NOT NULL,
    create_at TIMESTAMP,
    create_by VARCHAR(255),
    update_at TIMESTAMP,
    update_by VARCHAR(255),
    version INT8 NOT NULL DEFAULT 0
);
CREATE SEQUENCE assure_id_seq INCREMENT BY 50 START 1;
ALTER TABLE assure ALTER COLUMN id SET DEFAULT nextval('assure_id_seq');

CREATE TABLE vehicule
(
    id INT8 PRIMARY KEY,
    date_premiere_mise_en_circulation DATE NOT NULL,
    numero_immatriculation VARCHAR(100) NOT NULL,
    couleur VARCHAR(100) NOT NULL,
    nombre_sieges INT4 NOT NULL,
    nombre_portes INT4 NOT NULL,
    categorie_vehicule_id INT8 NOT NULL,
    puissance_fiscale INT4 NOT NULL,
    valeur_neuve NUMERIC(19, 2) NOT NULL,
    valeur_venale NUMERIC(19, 2) NOT NULL,
    create_at TIMESTAMP,
    create_by VARCHAR(255),
    update_at TIMESTAMP,
    update_by VARCHAR(255),
    version INT8 NOT NULL DEFAULT 0,
    CONSTRAINT vehicule_categorie_fk FOREIGN KEY (categorie_vehicule_id) REFERENCES categorie_vehicule (id)
);
CREATE SEQUENCE vehicule_id_seq INCREMENT BY 50 START 1;
ALTER TABLE vehicule ALTER COLUMN id SET DEFAULT nextval('vehicule_id_seq');

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
