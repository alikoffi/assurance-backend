INSERT INTO categorie_vehicule (id, code, libelle, description) VALUES
(1, '201', 'Promenade et Affaire', 'Usage personnel'),
(2, '202', 'Vehicules Motorises a 2 ou 3 roues', 'Motocycle, tricycles'),
(3, '203', 'Transport public de voyage', 'Vehicule transport de personnes'),
(4, '204', 'Vehicule de transport avec taximetres', 'Taxis');
SELECT setval('categorie_vehicule_id_seq', (SELECT MAX(id) FROM categorie_vehicule));

INSERT INTO garantie (id, code, libelle, taux, age_maximum, prime_minimum) VALUES
(1, 'RC', 'Responsabilite Civile', NULL, NULL, NULL),
(2, 'DOMMAGES', 'Dommages', 0.026000, 5, NULL),
(3, 'TIERCE_COLLISION', 'Tierce Collision', 0.016500, 8, NULL),
(4, 'TIERCE_PLAFONNEE', 'Tierce Plafonnee', 0.042000, 10, 100000),
(5, 'VOL', 'Vol', 0.001400, NULL, NULL),
(6, 'INCENDIE', 'Incendie', 0.001500, NULL, NULL);
SELECT setval('garantie_id_seq', (SELECT MAX(id) FROM garantie));

INSERT INTO produit_assurance (id, code, nom, description, actif) VALUES
(1, 'PAPILLON', 'Papillon', 'RC, Dommages, Vol', TRUE),
(2, 'DOUBY', 'Douby', 'RC, Dommages, Tierce Collision', TRUE),
(3, 'DOUYOU', 'Douyou', 'RC, Dommages, Tierce Collision, Incendie', TRUE),
(4, 'TOUTOURISQUOU', 'Toutourisquou', 'Toutes garanties', TRUE);
SELECT setval('produit_assurance_id_seq', (SELECT MAX(id) FROM produit_assurance));

INSERT INTO produit_categorie_vehicule (produit_assurance_id, categorie_vehicule_id) VALUES
(1, 1),
(2, 2),
(3, 1),
(3, 2),
(4, 1);

INSERT INTO produit_garantie (produit_assurance_id, garantie_id) VALUES
(1, 1),
(1, 2),
(1, 5),
(2, 1),
(2, 2),
(2, 3),
(3, 1),
(3, 2),
(3, 3),
(3, 6),
(4, 1),
(4, 2),
(4, 3),
(4, 4),
(4, 5),
(4, 6);

INSERT INTO tarif_responsabilite_civile (id, puissance_min, puissance_max, prime) VALUES
(1, 2, 2, 37601),
(2, 3, 6, 45181),
(3, 7, 10, 51078),
(4, 11, 14, 65677),
(5, 15, 23, 86456),
(6, 24, NULL, 104143);
SELECT setval('tarif_responsabilite_civile_id_seq', (SELECT MAX(id) FROM tarif_responsabilite_civile));
