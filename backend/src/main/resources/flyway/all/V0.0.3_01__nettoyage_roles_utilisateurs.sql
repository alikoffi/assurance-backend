UPDATE utilisateur
SET statut = 'INACTIF'
WHERE username IN ('abadou', 'ibamba', 'daka', 'akouadio');

INSERT INTO utilisateur (id, username, password, prenoms, nom, role, statut)
SELECT nextval('utilisateur_id_seq'), 'amazone1', '$2a$10$KdGQLTDCV.nw5zblVb3JN.9DMrLUJi8lLJS0ocvmH4ryrSI7DBF/e', 'Awa', 'Kouame', 'AMAZONE', 'ACTIF'
WHERE NOT EXISTS (SELECT 1 FROM utilisateur WHERE username = 'amazone1');

INSERT INTO utilisateur (id, username, password, prenoms, nom, role, statut)
SELECT nextval('utilisateur_id_seq'), 'amazone2', '$2a$10$KdGQLTDCV.nw5zblVb3JN.9DMrLUJi8lLJS0ocvmH4ryrSI7DBF/e', 'Mariam', 'Traore', 'AMAZONE', 'ACTIF'
WHERE NOT EXISTS (SELECT 1 FROM utilisateur WHERE username = 'amazone2');
