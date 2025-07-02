INSERT INTO users (id, username, password, role) values (100, 'ana@gmail.com', '$2a$12$nNfaTNIRlhhoP07EImYwqO34smBNPXbWAKrn6nSiESqj6Tv.FgFey', 'ROLE_ADMIN');
INSERT INTO users (id, username, password, role) values (101, 'bia@gmail.com', '$2a$12$nNfaTNIRlhhoP07EImYwqO34smBNPXbWAKrn6nSiESqj6Tv.FgFey', 'ROLE_CLIENT');
INSERT INTO users (id, username, password, role) values (102, 'bob@gmail.com', '$2a$12$nNfaTNIRlhhoP07EImYwqO34smBNPXbWAKrn6nSiESqj6Tv.FgFey', 'ROLE_CLIENT');

INSERT INTO parking_spaces (id, code, status) values (10, 'A-01', 'AVAILABLE');
INSERT INTO parking_spaces (id, code, status) values (20, 'A-02', 'AVAILABLE');
INSERT INTO parking_spaces (id, code, status) values (30, 'A-03', 'OCCUPIED');
INSERT INTO parking_spaces (id, code, status) values (40, 'A-04', 'AVAILABLE');