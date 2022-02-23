DELETE
FROM user_roles;
DELETE
FROM user_meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');


INSERT INTO user_meals (description, calories, dateTime, user_id)
VALUES ('Завтрак',                      500, '2020-01-30 10:00', 100000),
       ('Обед',                         1000, '2020-01-30 13:00', 100000),
       ('Ужин',                         500, '2020-01-30 20:00', 100000),
       ('Еда на граничное значение',    100, '2020-01-31 00:00', 100000),
       ('Завтрак', 1000, '2020-01-31 10:00', 100000),
       ('Обед', 500, '2020-01-31 13:00', 100000),
       ('Ужин', 410, '2020-01-31 20:00', 100000),


       ('Завтрак', 50, '2020-01-30 10:00', 100001),
       ('Обед', 10, '2020-01-30 13:00', 100001),
       ('Ужин', 50, '2020-01-30 20:00', 100001)
;


INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);
