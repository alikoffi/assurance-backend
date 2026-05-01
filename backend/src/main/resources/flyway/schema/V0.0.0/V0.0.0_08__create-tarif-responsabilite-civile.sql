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
