CREATE TABLE categorie_vehicule
(
    id INT8 PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    libelle VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    create_at TIMESTAMP,
    create_by VARCHAR(255),
    update_at TIMESTAMP,
    update_by VARCHAR(255),
    version INT8 NOT NULL DEFAULT 0
);
CREATE SEQUENCE categorie_vehicule_id_seq INCREMENT BY 50 START 1;
ALTER TABLE categorie_vehicule ALTER COLUMN id SET DEFAULT nextval('categorie_vehicule_id_seq');
