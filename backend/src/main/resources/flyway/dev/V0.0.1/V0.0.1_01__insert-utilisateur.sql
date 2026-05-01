INSERT INTO utilisateur (id, username, password, prenoms, nom, role, statut, must_change_password) VALUES
(nextval('utilisateur_id_seq'), 'admin', '$2a$10$KdGQLTDCV.nw5zblVb3JN.9DMrLUJi8lLJS0ocvmH4ryrSI7DBF/e', '', 'Administrateur', 'ADMIN', 'ACTIF', FALSE),
(nextval('utilisateur_id_seq'), 'amazone1', '$2a$10$KdGQLTDCV.nw5zblVb3JN.9DMrLUJi8lLJS0ocvmH4ryrSI7DBF/e', 'Awa', 'Kouame', 'AMAZONE', 'ACTIF', FALSE),
(nextval('utilisateur_id_seq'), 'amazone2', '$2a$10$KdGQLTDCV.nw5zblVb3JN.9DMrLUJi8lLJS0ocvmH4ryrSI7DBF/e', 'Mariam', 'Traore', 'AMAZONE', 'ACTIF', FALSE);
