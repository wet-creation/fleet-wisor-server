CREATE TABLE IF NOT EXISTS owner
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(255)        NOT NULL,
    surname  VARCHAR(255)        NOT NULL,
    email    VARCHAR(254) UNIQUE NOT NULL,
    password VARCHAR(254)        NOT NULL
);

CREATE TABLE IF NOT EXISTS driver
(
    id                   INT PRIMARY KEY AUTO_INCREMENT,
    ownerId              INT                NOT NULL,
    driverLicenseNumber  VARCHAR(50) UNIQUE NOT NULL,
    name                 VARCHAR(255)       NOT NULL,
    surname              VARCHAR(255)       NOT NULL,
    phone                VARCHAR(20) UNIQUE NOT NULL,
    frontLicensePhotoUrl VARCHAR(255)       NOT NULL,
    backLicensePhotoUrl  VARCHAR(255)       NOT NULL,
    salary               FLOAT(8)           NOT NULL,
    birthday             DATE               NOT NULL,
    FOREIGN KEY (ownerId) REFERENCES owner (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS car_body
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS car
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    brandName    VARCHAR(20)        NOT NULL,
    ownerId      INT                NOT NULL,
    carBodyId    INT                NOT NULL,
    color        VARCHAR(20),
    vin          VARCHAR(18) UNIQUE,
    mileage      BIGINT DEFAULT 0,
    model        VARCHAR(20),
    licensePlate VARCHAR(20) UNIQUE NULL,
    FOREIGN KEY (ownerId) REFERENCES owner (id) ON DELETE CASCADE,
    FOREIGN KEY (carBodyId) REFERENCES car_body (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS driver_with_car
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    carId     INT      NOT NULL,
    driverId  INT      NOT NULL,
    timeStart DATETIME NOT NULL,
    timeEnd   DATETIME NULL,
    FOREIGN KEY (carId) REFERENCES car (id) ON DELETE CASCADE,
    FOREIGN KEY (driverId) REFERENCES driver (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS maintenance
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    carId       INT           NOT NULL,
    description VARCHAR(2000) NOT NULL,
    price       FLOAT(8)      NOT NULL,
    time        DATETIME      NOT NULL,
    checkUrl    VARCHAR(255),
    FOREIGN KEY (carId) REFERENCES car (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS car_fill_up
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    carId    INT      NOT NULL,
    time     DATETIME NOT NULL,
    price    FLOAT(8) NOT NULL,
    amount    FLOAT(8) NOT NULL,
    checkUrl VARCHAR(255),
    FOREIGN KEY (carId) REFERENCES car (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS fuel_type
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS car_fuel_types
(
    fuelTypeId INT NOT NULL,
    carId      INT NOT NULL,
    PRIMARY KEY (fuelTypeId, carId),
    FOREIGN KEY (fuelTypeId) REFERENCES fuel_type (id) ON DELETE CASCADE,
    FOREIGN KEY (carId) REFERENCES car (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS insurance
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    carId     INT  NOT NULL,
    startDate DATE NOT NULL,
    endDate   DATE NOT NULL,
    photoUrl  VARCHAR(255),
    FOREIGN KEY (carId) REFERENCES car (id) ON DELETE CASCADE
);
