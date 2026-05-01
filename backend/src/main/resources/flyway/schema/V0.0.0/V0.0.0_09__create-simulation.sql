CREATE TABLE simulation
(
    id INT8 PRIMARY KEY,
    quote_reference VARCHAR(14) NOT NULL UNIQUE,
    end_date DATE NOT NULL,
    produit_assurance_id INT8 NOT NULL,
    categorie_vehicule_id INT8 NOT NULL,
    date_premiere_mise_en_circulation DATE NOT NULL,
    puissance_fiscale INT4 NOT NULL,
    valeur_neuve NUMERIC(19, 2) NOT NULL,
    valeur_venale NUMERIC(19, 2) NOT NULL,
    price NUMERIC(19, 2) NOT NULL,
    create_at TIMESTAMP,
    create_by VARCHAR(255),
    update_at TIMESTAMP,
    update_by VARCHAR(255),
    version INT8 NOT NULL DEFAULT 0,
    CONSTRAINT simulation_produit_fk FOREIGN KEY (produit_assurance_id) REFERENCES produit_assurance (id),
    CONSTRAINT simulation_categorie_fk FOREIGN KEY (categorie_vehicule_id) REFERENCES categorie_vehicule (id)
);
CREATE SEQUENCE simulation_id_seq INCREMENT BY 50 START 1;
ALTER TABLE simulation ALTER COLUMN id SET DEFAULT nextval('simulation_id_seq');
