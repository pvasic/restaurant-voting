INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'User_Last', '{noop}userPassword'),
       ('admin@javaops.ru', 'Admin_First', 'Admin_Last', '{noop}adminPassword'),
       ('manager@gmail.com', 'Manager Коля', 'Соловьев', '{noop}managerKolyaPassword'),
       ('managervasya@gmail.com', 'Manager Вася', 'Пупкин', '{noop}managerVasyaPassword');

INSERT INTO USER_ROLES (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('USER', 3),
       ('MANAGER', 3),
       ('USER', 4),
       ('MANAGER', 4);

INSERT INTO RESTAURANTS (NAME, ADDRESS, VOTE_COUNT, DATE_TIME, USER_ID)
VALUES ('Macdonalds', 'Москва', 0, '2021-05-01 00:00:00', 3),
       ('DoDo пицца', 'Мытищи', 0, '2021-05-02 00:00:00', 4);

INSERT INTO DISHES (NAME, PRICE, DATE_TIME, RESTAURANT_ID, USER_ID)
VALUES ('chicken', 250, '2021-05-06 00:00:00', 1, 3),
       ('мясо', 300, '2021-05-07 00:00:00', 1, 3),
       ('Картошка', 100, '2020-05-08 00:00:00', 2, 4),
       ('Рис', 200, '2020-05-08 10:00:00', 2, 4);