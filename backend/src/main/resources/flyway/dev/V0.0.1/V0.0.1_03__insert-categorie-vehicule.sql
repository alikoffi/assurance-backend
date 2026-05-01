INSERT INTO categorie_vehicule (id, code, libelle, description) VALUES
(1, '201', 'Promenade et Affaires', 'Usage personnel'),
(2, '202', 'Véhicules motorisés à 2 ou 3 roues', 'Motocycle, tricycles'),
(3, '203', 'Transport public de voyage', 'Véhicule de transport de personnes'),
(4, '204', 'Véhicule de transport avec taximètres', 'Taxis');
SELECT setval('categorie_vehicule_id_seq', (SELECT MAX(id) FROM categorie_vehicule));
