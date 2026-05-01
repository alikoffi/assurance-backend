INSERT INTO tarif_responsabilite_civile (id, puissance_min, puissance_max, prime) VALUES
(1, 2, 2, 37601),
(2, 3, 6, 45181),
(3, 7, 10, 51078),
(4, 11, 14, 65677),
(5, 15, 23, 86456),
(6, 24, NULL, 104143);
SELECT setval('tarif_responsabilite_civile_id_seq', (SELECT MAX(id) FROM tarif_responsabilite_civile));
