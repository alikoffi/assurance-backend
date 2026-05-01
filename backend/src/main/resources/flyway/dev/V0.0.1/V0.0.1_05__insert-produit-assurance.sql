INSERT INTO produit_assurance (id, code, nom, description, actif) VALUES
(1, 'PAPILLON', 'Papillon', 'RC, Dommages, Vol', TRUE),
(2, 'DOUBY', 'Douby', 'RC, Dommages, Tierce Collision', TRUE),
(3, 'DOUYOU', 'Douyou', 'RC, Dommages, Tierce Collision, Incendie', TRUE),
(4, 'TOUTOURISQUOU', 'Toutourisquou', 'Toutes garanties', TRUE);
SELECT setval('produit_assurance_id_seq', (SELECT MAX(id) FROM produit_assurance));
