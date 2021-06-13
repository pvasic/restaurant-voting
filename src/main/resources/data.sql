INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'User_Last', '{noop}password'),
       ('admin@javaops.ru', 'Admin_First', 'Admin_Last', '{noop}admin'),
       ('manager@gmail.com', 'Manager Коля', 'Соловьев', '{noop}manager'),
       ('managervasya@gmail.com', 'Manager Вася', 'Пупкин', '{noop}managervasya');

INSERT INTO USER_ROLES (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('USER', 3),
       ('MANAGER', 3),
       ('USER', 4),
       ('MANAGER', 4);

INSERT INTO RESTAURANTS (NAME, ADDRESS, DATE_TIME, USER_ID)
VALUES ('Macdonalds', 'Москва', '2021-05-01 00:00:00', 3),
       ('DoDo пицца', 'Мытищи', '2021-05-02 00:00:00', 4);

INSERT INTO DISHES (NAME, PRICE, DATE_TIME, RESTAURANT_ID)
VALUES ('chicken', 250, '2021-05-06 00:00:00', 1),
       ('мясо', 300, '2021-05-07 00:00:00', 1),
       ('Картошка', 100, '2020-05-08 00:00:00', 2),
       ('Рис', 200, '2020-05-08 10:00:00', 2);