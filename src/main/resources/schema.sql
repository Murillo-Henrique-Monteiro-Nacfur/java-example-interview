CREATE TABLE IF NOT EXISTS flight
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(255) NOT NULL,
    origin      VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    status      VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    login       VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    birth_date  DATE,
    role        VARCHAR(255) NOT NULL,
    CONSTRAINT chk_role_user CHECK (role IN ('ADMIN', 'AGENT', 'OPERATOR'))
);

CREATE TABLE IF NOT EXISTS passenger
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255),
    birth_date   DATE
);

CREATE TABLE IF NOT EXISTS flight_passenger (
    flight_id BIGINT NOT NULL,
    passenger_id BIGINT NOT NULL,
    PRIMARY KEY (flight_id, passenger_id),
    FOREIGN KEY (flight_id) REFERENCES flight(id),
    FOREIGN KEY (passenger_id) REFERENCES passenger(id)
);
