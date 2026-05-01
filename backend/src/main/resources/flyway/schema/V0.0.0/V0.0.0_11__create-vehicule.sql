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
