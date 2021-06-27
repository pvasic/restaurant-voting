INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'User_Last', '{noop}userPassword'),
       ('manager@gmail.com', 'Manager_First', 'Manager_Last', '{noop}managerPassword'),
       ('admin@javaops.ru', 'Admin_First', 'Admin_Last', '{noop}adminPassword');

INSERT INTO USER_ROLES (ROLE, USER_ID)
VALUES ('USER', 1),
       ('USER', 2),
       ('MANAGER', 2),
       ('ADMIN', 3),
       ('MANAGER', 3);

INSERT INTO RESTAURANTS (NAME, ADDRESS, VOTE_COUNT, DATE_TIME, USER_ID)
VALUES ('Macdonalds', 'Москва', 0, '2021-05-01 00:00:00', 2),
       ('DoDo пицца', 'Мытищи', 0, '2021-05-02 00:00:00', 3);

INSERT INTO DISHES (NAME, PRICE, CREATED, RESTAURANT_ID, USER_ID)
VALUES ('chicken', 250, '2021-05-07 09:10:00', 1, 2),
       ('rise', 300, '2021-05-06 13:00:00', 1, 2),
       ('Картошка', 100, '2020-05-08 18:00:00', 2, 3),
       ('Рис', 200, '2020-05-08 10:00:00', 2, 3);