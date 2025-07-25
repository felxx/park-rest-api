insert into users (id, username, password, role)
    values (100, 'ana@email.com.br', '$2a$12$e6ueYpLCQCT9j7MXTVLfc.R.kWUi.UXkf26rRbAfLmxq9dqNkhQ3C', 'ROLE_ADMIN');
insert into users (id, username, password, role)
    values (101, 'bia@email.com.br', '$2a$12$e6ueYpLCQCT9j7MXTVLfc.R.kWUi.UXkf26rRbAfLmxq9dqNkhQ3C', 'ROLE_CLIENT');
insert into users (id, username, password, role)
    values (102, 'bob@email.com.br', '$2a$12$e6ueYpLCQCT9j7MXTVLfc.R.kWUi.UXkf26rRbAfLmxq9dqNkhQ3C', 'ROLE_CLIENT');

insert into clients (id, name, cpf, user_id) values (21, 'Biatriz Rodrigues', '09191773016', 101);
insert into clients (id, name, cpf, user_id) values (22, 'Rodrigo Silva', '98401203015', 102);

insert into parking_spaces (id, code, status) values (100, 'A-01', 'OCCUPIED');
insert into parking_spaces (id, code, status) values (200, 'A-02', 'OCCUPIED');
insert into parking_spaces (id, code, status) values (300, 'A-03', 'OCCUPIED');
insert into parking_spaces (id, code, status) values (400, 'A-04', 'AVAILABLE');
insert into parking_spaces (id, code, status) values (500, 'A-05', 'AVAILABLE');

insert into client_parking_spaces (receipt, license_plate, make, model, color, entry_date, client_id, parking_space_id)
    values ('20230313-101300', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-13 10:15:00', 22, 100);
insert into client_parking_spaces (receipt, license_plate, make, model, color, entry_date, client_id, parking_space_id)
    values ('20230314-101400', 'SIE-1020', 'FIAT', 'SIENA', 'BRANCO', '2023-03-14 10:15:00', 21, 200);
insert into client_parking_spaces (receipt, license_plate, make, model, color, entry_date, client_id, parking_space_id)
    values ('20230315-101500', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-14 10:15:00', 22, 300);