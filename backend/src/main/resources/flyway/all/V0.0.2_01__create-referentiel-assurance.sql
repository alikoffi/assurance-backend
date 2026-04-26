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

CREATE TABLE garantie
(
    id INT8 PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    libelle VARCHAR(255) NOT NULL,
    taux NUMERIC(10, 6),
    age_maximum INT4,
    prime_minimum NUMERIC(19, 2),
    create_at TIMESTAMP,
    create_by VARCHAR(255),
    update_at TIMESTAMP,
    update_by VARCHAR(255),
    version INT8 NOT NULL DEFAULT 0
);
CREATE SEQUENCE garantie_id_seq INCREMENT BY 50 START 1;
ALTER TABLE garantie ALTER COLUMN id SET DEFAULT nextval('garantie_id_seq');

CREATE TABLE produit_assurance
(
    id INT8 PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    nom VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    create_at TIMESTAMP,
    create_by VARCHAR(255),
    update_at TIMESTAMP,
    update_by VARCHAR(255),
    version INT8 NOT NULL DEFAULT 0
);
CREATE SEQUENCE produit_assurance_id_seq INCREMENT BY 50 START 1;
ALTER TABLE produit_assurance ALTER COLUMN id SET DEFAULT nextval('produit_assurance_id_seq');

CREATE TABLE produit_categorie_vehicule
(
    produit_assurance_id INT8 NOT NULL,
    categorie_vehicule_id INT8 NOT NULL,
    PRIMARY KEY (produit_assurance_id, categorie_vehicule_id),
    CONSTRAINT produit_categorie_produit_fk FOREIGN KEY (produit_assurance_id) REFERENCES produit_assurance (id),
    CONSTRAINT produit_categorie_categorie_fk FOREIGN KEY (categorie_vehicule_id) REFERENCES categorie_vehicule (id)
);

CREATE TABLE produit_garantie
(
    produit_assurance_id INT8 NOT NULL,
    garantie_id INT8 NOT NULL,
    PRIMARY KEY (produit_assurance_id, garantie_id),
    CONSTRAINT produit_garantie_produit_fk FOREIGN KEY (produit_assurance_id) REFERENCES produit_assurance (id),
    CONSTRAINT produit_garantie_garantie_fk FOREIGN KEY (garantie_id) REFERENCES garantie (id)
);

CREATE TABLE tarif_responsabilite_civile
(
    id INT8 PRIMARY KEY,
    puissance_min INT4 NOT NULL,
    puissance_max INT4,
    prime NUMERIC(19, 2) NOT NULL,
    create_at TIMESTAMP,
    create_by VARCHAR(255),
    update_at TIMESTAMP,
    update_by VARCHAR(255),
    version INT8 NOT NULL DEFAULT 0
);
CREATE SEQUENCE tarif_responsabilite_civile_id_seq INCREMENT BY 50 START 1;
ALTER TABLE tarif_responsabilite_civile ALTER COLUMN id SET DEFAULT nextval('tarif_responsabilite_civile_id_seq');
