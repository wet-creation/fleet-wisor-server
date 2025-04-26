-- Добавление владельцев
INSERT INTO owner (name, surname, email, password)
VALUES ('Олександр', 'Петров', 'olexandr.petrov@example.com', 'hashed_password1'),
       ('Марина', 'Іваненко', 'marina.ivanenko@example.com', 'hashed_password2'),
       ('Андрій', 'Сидорчук', 'andriy.sydorchuk@example.com', 'hashed_password3'),
       ('Misha', 'Nedobitkin', 'mishau@mail.com', '$2a$12$wYSTn7Ew6E/dPvZbYtgBpexSlYZAI3kMxdqaEVE9THad2ABDIfh2a');

-- Добавление водителей
INSERT INTO driver (ownerId, driverLicenseNumber, name, surname, phone, frontLicensePhotoUrl, backLicensePhotoUrl,
                    salary, birthday)
VALUES (4, 'AA123456', 'Ігор', 'Коваленко', '+380671234567', 'https://example.com/front1.jpg',
        'https://example.com/back1.jpg', 20000, '1985-06-12'),
       (4, 'BB654321', 'Олег', 'Мельник', '+380672345678', 'https://example.com/front2.jpg',
        'https://example.com/back2.jpg', 25000, '1990-03-22');

-- Добавление машин
INSERT INTO car (brandName, ownerId, carBodyId, color, vin, mileage, model, licensePlate)
VALUES ('Toyota', 4, 1, 'Чорний', 'JH4DB8590SS001234', 120000, 'Camry', 'АА1234ВТ'),
       ('BMW', 4, 2, 'Синій', 'WBA3A5C55CF123456', 80000, 'X5', 'ВС5678СК');

-- Добавление связки водителей и машин
INSERT INTO driver_with_car (carId, driverId, timeStart, timeEnd)
VALUES (1,1 , '2024-03-01 08:00:00', NULL),
       (2, 2, '2024-04-10 09:30:00', NULL);

-- Добавление техобслуговування
INSERT INTO maintenance (carId, description, price, time, checkUrl)
VALUES (1, 'Заміна масла та фільтрів', 1500.50, '2024-06-15 10:00:00', 'https://example.com/check1.jpg'),
       (2, 'Ремонт ходової', 3000.75, '2024-06-20 15:30:00', 'https://example.com/check2.jpg');

-- Заправка машин
INSERT INTO car_fill_up (carId, time, price, checkUrl, amount)
VALUES (1, '2024-07-01 12:00:00', 1200, 'https://example.com/fuel1.jpg', 123),
 (1, '2025-07-01 00:00:00', 90, 'https://example.com/fuel2321.jpg', 123),
       (2, '2024-07-02 14:00:00', 1500, 'https://example.com/fuel2.jpg', 123);



-- Связка машины и типов топлива
INSERT INTO car_fuel_types (fuelTypeId, carId)
VALUES (1, 1),(4, 1),
       (2, 2);

-- Добавление страховки
INSERT INTO insurance (carId, startDate, endDate, photoUrl)
VALUES (1, '2024-01-01', '2025-01-01', 'https://example.com/insurance1.jpg'),
       (2, '2024-02-15', '2025-02-15', 'https://example.com/insurance2.jpg');
