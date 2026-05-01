INSERT INTO role (id, code, designation) VALUES
(1, 'ADMIN', 'Administrateur'),
(2, 'AMAZONE', 'Amazone');
SELECT setval('role_id_seq', (SELECT MAX(id) FROM role));
