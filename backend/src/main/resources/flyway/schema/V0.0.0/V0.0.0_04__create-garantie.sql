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
