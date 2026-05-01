CREATE TABLE utilisateur
(
    id INT8 PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL DEFAULT '$2a$10$KdGQLTDCV.nw5zblVb3JN.9DMrLUJi8lLJS0ocvmH4ryrSI7DBF/e', -- Mot de passe par défaut eburtis2020
    nom VARCHAR(100) NOT NULL,
    prenoms VARCHAR(100) NOT NULL,
    role VARCHAR(100) NOT NULL,
    statut VARCHAR(100) NOT NULL DEFAULT 'ACTIF',
    must_change_password BOOLEAN NOT NULL DEFAULT FALSE,
    create_at      TIMESTAMP,
    create_by     VARCHAR(255),
    update_at  TIMESTAMP,
    update_by VARCHAR(255),
    version INT8 NOT NULL DEFAULT 0
);
CREATE SEQUENCE utilisateur_id_seq INCREMENT BY 50 START 1;
ALTER TABLE utilisateur
    ALTER COLUMN id SET DEFAULT nextval('utilisateur_id_seq');
