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
