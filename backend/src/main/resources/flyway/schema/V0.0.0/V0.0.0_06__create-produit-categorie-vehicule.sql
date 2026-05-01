CREATE TABLE produit_categorie_vehicule
(
    produit_assurance_id INT8 NOT NULL,
    categorie_vehicule_id INT8 NOT NULL,
    PRIMARY KEY (produit_assurance_id, categorie_vehicule_id),
    CONSTRAINT produit_categorie_produit_fk FOREIGN KEY (produit_assurance_id) REFERENCES produit_assurance (id),
    CONSTRAINT produit_categorie_categorie_fk FOREIGN KEY (categorie_vehicule_id) REFERENCES categorie_vehicule (id)
);
