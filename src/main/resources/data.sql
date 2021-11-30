INSERT INTO USERS (EMAIL, NAME, LAST_NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'User_Last', '{noop}user'),
       ('manager@gmail.com', 'Manager_First', 'Manager_Last', '{noop}manager'),
       ('admin@gmail.com', 'Admin_First', 'Admin_Last', '{noop}admin');

INSERT INTO USER_ROLES (ROLE, USER_ID)
VALUES ('USER', 1),
       ('USER', 2),
       ('MANAGER', 2),
       ('ADMIN', 3),
       ('MANAGER', 3);

INSERT INTO RESTAURANTS (NAME, ADDRESS, EMAIL, USER_ID)
VALUES ('Macdonalds', 'Москва', 'macdonalds@mac.com', 2),
       ('DoDoPizza', 'Мытищи', 'dodo@do.com', 3);

INSERT INTO DISHES (NAME, PRICE, DATE, RESTAURANT_ID, USER_ID)
VALUES ('chicken', 250, '2021-05-07 00:00:00', 1, 2),
       ('rise', 300, '2021-05-06 00:00:00', 1, 2),
       ('Картошка', 100, '2020-05-08 00:00:00', 2, 3),
       ('Рис', 200, '2020-05-09 00:00:00', 2, 3);

INSERT INTO VOTES (USER_ID, RESTAURANT_ID, DATE_TIME)
VALUES (1, 1, '2021-05-07 09:10:00');