INSERT INTO USERS (EMAIL, NAME, LAST_NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'User_Last', '{noop}userPassword'),
       ('manager@gmail.com', 'Manager_First', 'Manager_Last', '{noop}managerPassword'),
       ('admin@gmail.com', 'Admin_First', 'Admin_Last', '{noop}adminPassword');

INSERT INTO USER_ROLES (ROLE, USER_ID)
VALUES ('USER', 1),
       ('USER', 2),
       ('MANAGER', 2),
       ('ADMIN', 3),
       ('MANAGER', 3);

INSERT INTO RESTAURANTS (NAME, ADDRESS, EMAIL, USER_ID)
VALUES ('Macdonalds', 'Москва', 'macdonalds@mac.com', 2),
       ('DoDoPizza', 'Мытищи', 'dodo@do.com', 3);

INSERT INTO DISHES (NAME, PRICE, DATE, RESTAURANT_ID)
VALUES ('chicken', 250, '2021-05-07 00:00:00', 1),
       ('rise', 300, '2021-05-06 00:00:00', 1),
       ('Картошка', 100, '2020-05-08 00:00:00', 2),
       ('Рис', 200, '2020-05-09 00:00:00', 2);

INSERT INTO VOTES (USER_ID, RESTAURANT_ID, DATE_TIME)
VALUES (1, 1, '2021-05-07 09:10:00');