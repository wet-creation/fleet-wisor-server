CREATE TABLE IF NOT EXISTS `trip`
(
    `id`             int           NOT NULL AUTO_INCREMENT,
    `name`           varchar(255)  NOT NULL,
    description      varchar(1500) NOT NULL,
    `latitudeStart`  DECIMAL(9, 6) NOT NULL,
    `latitudeEnd`    DECIMAL(9, 6) NOT NULL,
    `longitudeStart` DECIMAL(9, 6) NOT NULL,
    `longitudeEnd`   DECIMAL(9, 6) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `car_on_trip`
(
    `tripId`        int           NOT NULL AUTO_INCREMENT,
    `timestampStart` bigint        NOT NULL,
    `timestampEnd`   bigint DEFAULT NULL,
    `latitude`       DECIMAL(9, 6) NOT NULL,
    `longitude`      DECIMAL(9, 6) NOT NULL,
    `carId`          int           NOT NULL,
    PRIMARY KEY (`tripId`, `carId`),
    KEY `fk_car_on_trip_tripId__id` (`tripId`),
    KEY `fk_car_on_trip_carId__id` (`carId`),
    CONSTRAINT `fk_car_on_trip_carId__id` FOREIGN KEY (`carId`) REFERENCES `car` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_car_on_trip_tripId__id` FOREIGN KEY (`tripId`) REFERENCES `trip` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);


INSERT INTO `trip` (`id`, `name`, `description`, `latitudeStart`, `latitudeEnd`, `longitudeStart`, `longitudeEnd`)
VALUES (1, 'trip 1', 'trip from city A to city B', 40.712776, 41.403629, -74.005974, 2.174356),
       (2, 'trip 2', 'trip from city B to city C', 41.403629, 48.856613, 2.174356, 2.352222),
       (3, 'trip 3', 'trip from city C to city D', 48.856613, 51.507351, 2.352222, -0.127758),
       (4, 'trip 4', 'trip from city D to city E', 51.507351, 52.520008, -0.127758, 13.404954),
       (5, 'trip 5', 'trip from city E to city F', 52.520008, 55.755825, 13.404954, 37.617298);


INSERT INTO `car_on_trip` (`tripId`, `timestampStart`, `timestampEnd`, `latitude`, `longitude`, `carId`)
VALUES (1, 1672531200, 1672617600, 40.712776, -74.005974, 1), -- Машина 1 на маршруте 1
       (1, 1672531200, 1672617600, 41.403629, 2.174356, 2),   -- Машина 2 на маршруте 1
       (2, 1672617600, 1672704000, 41.403629, 2.174356, 2),   -- Машина 2 на маршруте 2
       (2, 1672617600, 1672704000, 48.856613, 2.352222, 3),   -- Машина 3 на маршруте 2
       (3, 1672704000, 1672790400, 48.856613, 2.352222, 3),   -- Машина 3 на маршруте 3
       (3, 1672704000, 1672790400, 51.507351, -0.127758, 1),  -- Машина 1 на маршруте 3
       (4, 1672790400, 1672876800, 51.507351, -0.127758, 1),  -- Машина 1 на маршруте 4
       (4, 1672790400, 1672876800, 52.520008, 13.404954, 2),  -- Машина 2 на маршруте 4
       (5, 1672876800, 1672963200, 52.520008, 13.404954, 2),  -- Машина 2 на маршруте 5
       (5, 1672876800, 1672963200, 55.755825, 37.617298, 3); -- Машина 3 на маршруте 5

