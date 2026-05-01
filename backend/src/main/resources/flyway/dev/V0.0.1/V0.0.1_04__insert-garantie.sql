INSERT INTO garantie (id, code, libelle, taux, age_maximum, prime_minimum) VALUES
(1, 'RC', 'Responsabilité Civile', NULL, NULL, NULL),
(2, 'DOMMAGES', 'Dommages', 0.026000, 5, NULL),
(3, 'TIERCE_COLLISION', 'Tierce Collision', 0.016500, 8, NULL),
(4, 'TIERCE_PLAFONNEE', 'Tierce Plafonnée', 0.042000, 10, 100000),
(5, 'VOL', 'Vol', 0.001400, NULL, NULL),
(6, 'INCENDIE', 'Incendie', 0.001500, NULL, NULL);
SELECT setval('garantie_id_seq', (SELECT MAX(id) FROM garantie));
