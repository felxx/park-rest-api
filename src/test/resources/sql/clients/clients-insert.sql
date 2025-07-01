INSERT INTO users (id, username, password, role) values (101, 'ana@gmail.com', '$2a$12$e6ueYpLCQCT9j7MXTVLfc.R.kWUi.UXkf26rRbAfLmxq9dqNkhQ3C', 'ROLE_ADMIN');
INSERT INTO users (id, username, password, role) values (102, 'bia@gmail.com', '$2a$12$e6ueYpLCQCT9j7MXTVLfc.R.kWUi.UXkf26rRbAfLmxq9dqNkhQ3C', 'ROLE_CLIENT');
INSERT INTO users (id, username, password, role) values (103, 'bob@gmail.com', '$2a$12$e6ueYpLCQCT9j7MXTVLfc.R.kWUi.UXkf26rRbAfLmxq9dqNkhQ3C', 'ROLE_CLIENT');
INSERT INTO users (id, username, password, role) values (104, 'toby@gmail.com', '$2a$12$e6ueYpLCQCT9j7MXTVLfc.R.kWUi.UXkf26rRbAfLmxq9dqNkhQ3C', 'ROLE_CLIENT');

INSERT INTO clients (id, name, cpf, user_id) values (10, 'Bianca Silva', '94975179040', 101);
INSERT INTO clients (id, name, cpf, user_id) values (20, 'Roberto Gomes', '82856169082', 102);