INSERT INTO car_body (nameUk, nameEn)
VALUES ('Седан', 'Sedan'),
       ('Хетчбек', 'Hatchback'),
       ('Універсал', 'Station Wagon'),
       ('Купе', 'Coupe'),
       ('Кабріолет', 'Convertible'),
       ('Позашляховик', 'SUV'),
       ('Кросовер', 'Crossover'),
       ('Мінівен', 'Minivan'),
       ('Пікап', 'Pickup'),
       ('Інше', 'Other'),
       ('Фургон', 'Van');

INSERT INTO fuel_type (nameUk, nameEn)
VALUES ('Бензин', 'Petrol'),
       ('Дизель', 'Diesel'),
       ('Газ', 'Gas'),
       ('Електрика', 'Electricity'),
       ('Гібрид', 'Hybrid'),
       ('Інше', 'Other'),
       ('Водень', 'Hydrogen');


INSERT INTO fuel_units (nameUk, nameEn, fuelTypeId)
VALUES
-- Бензин (id = 1)
('л', 'L', 1),
('гал США', 'US gal', 1),
('імп гал', 'Imp gal', 1),

-- Дизель (id = 2)
('л', 'L', 2),
('гал США', 'US gal', 2),
('імп гал', 'Imp gal', 2),

-- Газ (id = 3)
('л', 'L', 3),
('кг', 'kg', 3),
('гал США', 'US gal', 3),
('нм³', 'Nm³', 3),

-- Електрика (id = 4)
('кВт·год', 'kWh', 4),
('МВт·год', 'MWh', 4),
('А', 'A', 4),
('В', 'V', 4),

-- Гібрид (id = 5)
('л', 'L', 5),
('гал США', 'US gal', 5),
('імп гал', 'Imp gal', 5),
('кВт·год', 'kWh', 5),

-- Інше (id = 6)
('од', 'unit', 6),

-- Водень (id = 7)
('кг', 'kg', 7),
('нм³', 'Nm³', 7),
('МПа', 'MPa', 7);

