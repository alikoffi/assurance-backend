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
