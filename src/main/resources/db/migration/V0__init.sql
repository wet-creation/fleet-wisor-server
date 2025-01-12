CREATE TABLE IF NOT EXISTS `user`
(
    `id`       int                     NOT NULL AUTO_INCREMENT,
    `email`    varchar(255)            NOT NULL,
    `password` varchar(255)            NOT NULL,
    `name`     varchar(255)            NOT NULL,
    `surname`  varchar(255)            NOT NULL,
    `role`     enum ('OWNER','DRIVER') NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_email_unique` (`email`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `driver`
(
    `userId`              int          NOT NULL,
    `phone`               varchar(255) NOT NULL,
    `driverLicenseNumber` varchar(255) NOT NULL,
    `uniqueCode`          int          NOT NULL,
    UNIQUE KEY `driver_userId_unique` (`userId`),
    CONSTRAINT `fk_driver_userId__id` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `car_body`
(
    `id`   int          NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `fuel_type`
(
    `id`   int          NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;



CREATE TABLE IF NOT EXISTS `car`
(
    `id`           int          NOT NULL AUTO_INCREMENT,
    `name`         varchar(255) NOT NULL,
    `brandName`    varchar(255) NOT NULL,
    `color`        varchar(50)           DEFAULT NULL,
    `vin`          varchar(18)           DEFAULT NULL,
    `model`        varchar(255)          DEFAULT NULL,
    `licensePlate` varchar(50)           DEFAULT NULL,
    `mileAge`      int          NOT NULL DEFAULT '0',
    `ownerId`      int          NOT NULL,
    `carBodyId`    int          NOT NULL,
    `fuelTypeId`   int          NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_car_carBodyId__id` FOREIGN KEY (`carBodyId`) REFERENCES `car_body` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_car_fuelTypeId__id` FOREIGN KEY (`fuelTypeId`) REFERENCES `fuel_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_car_ownerId__userId` FOREIGN KEY (`ownerId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `driver_with_car`
(
    `id`             int    NOT NULL AUTO_INCREMENT,
    `timestampStart` bigint NOT NULL,
    `timestampEnd`   bigint DEFAULT NULL,
    `driverId`       int    NOT NULL,
    `carId`          int    NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_driver_with_car_driverId__userId` (`driverId`),
    KEY `fk_driver_with_car_carId__id` (`carId`),
    CONSTRAINT `fk_driver_with_car_carId__id` FOREIGN KEY (`carId`) REFERENCES `car` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_driver_with_car_driverId__userId` FOREIGN KEY (`driverId`) REFERENCES `driver` (`userId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- Таблица user
INSERT INTO `user` (`id`, `email`, `password`, `name`, `surname`, `role`)
VALUES
    (1, 'owner1@example.com', 'securepassword1', 'John', 'Doe', 'OWNER'),
    (2, 'driver1@example.com', 'securepassword2', 'Jane', 'Smith', 'DRIVER'),
    (3, 'owner2@example.com', 'securepassword3', 'Alice', 'Brown', 'OWNER'),
    (4, 'driver2@example.com', 'securepassword4', 'Bob', 'Johnson', 'DRIVER'),
    (5, 'owner3@example.com', 'securepassword5', 'Charlie', 'Williams', 'OWNER');



-- Таблица driver
INSERT INTO `driver` (`userId`, `phone`, `driverLicenseNumber`, `uniqueCode`)
VALUES
    (2, '123-456-7890', 'DL12345678', 1001),
    (4, '987-654-3210', 'DL87654321', 1002);


-- Таблица car_body
INSERT INTO `car_body` (`id`, `name`)
VALUES (1, 'Sedan'),
       (2, 'SUV'),
       (3, 'Hatchback'),
       (4, 'Coupe'),
       (5, 'Convertible'),
       (6, 'Wagon'),
       (7, 'Pickup'),
       (8, 'Van');

-- Таблица fuel_type
INSERT INTO `fuel_type` (`id`, `name`)
VALUES (1, 'Petrol'),
       (2, 'Diesel'),
       (3, 'Electric'),
       (4, 'Hybrid'),
       (5, 'LPG'),
       (6, 'CNG');

-- Таблица car
INSERT INTO `car` (`id`, `name`, `brandName`, `color`, `vin`, `model`, `licensePlate`, `mileAge`, `ownerId`,
                   `carBodyId`, `fuelTypeId`)
VALUES (1, 'Corolla', 'Toyota', 'Red', 'VIN12345ABCDE', '2020', 'ABC123', 50000, 1, 1, 1),
       (2, 'Model S', 'Tesla', 'Black', 'VIN67890FGHIJ', '2021', 'XYZ789', 10000, 3, 4, 3),
       (3, 'Civic', 'Honda', 'Blue', 'VIN11223KLMNO', '2019', 'LMN456', 75000, 5, 3, 1);

-- Таблица driver_with_car
INSERT INTO `driver_with_car` (`id`, `timestampStart`, `timestampEnd`, `driverId`, `carId`)
VALUES (1, 1672444800, NULL, 2, 1),
       (2, 1672527600, NULL, 4, 2);

