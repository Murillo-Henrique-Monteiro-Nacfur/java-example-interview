insert into FLIGHT(id, code, origin, destination, status) values (1, 'FL10', 'GRU', 'ATL', 'confirmed');
insert into FLIGHT(id, code, origin, destination, status) values (2, 'FL11', 'DXB', 'LAX', 'confirmed');
insert into FLIGHT(id, code, origin, destination, status) values (3, 'FL12', 'IST', 'LHR', 'cancelled');
insert into FLIGHT(id, code, origin, destination, status) values (4, 'FL13', 'DEL', 'DEL', 'confirmed');
insert into FLIGHT(id, code, origin, destination, status) values (5, 'FL14', 'AMS', 'MAD', 'confirmed');

INSERT INTO users (name, email, login, password, role, birth_date) VALUES
    ('Admin User', 'admin@securyflight.com', 'admin', '$2a$10$RNybnNxDNBJMni3HxpeBdeykwaPyix5/ikntktq7yaUn39gjkU4tG', 'ADMIN', '1980-01-01'),
    ('Agent User', 'agent@securyflight.com', 'agent', '$2a$10$RNybnNxDNBJMni3HxpeBdeykwaPyix5/ikntktq7yaUn39gjkU4tG', 'AGENT', '1985-05-10'),
    ('Operator User', 'operator@securyflight.com', 'operator', '$2a$10$RNybnNxDNBJMni3HxpeBdeykwaPyix5/ikntktq7yaUn39gjkU4tG', 'OPERATOR', '1990-12-15');

INSERT INTO passenger (name, email, phone_number, birth_date) VALUES
    ('Passenger One', 'p1@example.com', '555-1111', '1995-03-12'),
    ('Passenger Two', 'p2@example.com', '555-2222', '1998-07-22');
