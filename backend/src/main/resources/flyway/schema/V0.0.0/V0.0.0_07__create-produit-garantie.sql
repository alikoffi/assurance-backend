CREATE TABLE produit_garantie
(
    produit_assurance_id INT8 NOT NULL,
    garantie_id INT8 NOT NULL,
    PRIMARY KEY (produit_assurance_id, garantie_id),
    CONSTRAINT produit_garantie_produit_fk FOREIGN KEY (produit_assurance_id) REFERENCES produit_assurance (id),
    CONSTRAINT produit_garantie_garantie_fk FOREIGN KEY (garantie_id) REFERENCES garantie (id)
);
