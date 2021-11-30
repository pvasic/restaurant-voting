#   Restaurant voting

# REST API using Hibernate/Spring-Boot without frontend.

<a href="https://http://localhost:8080/swagger-ui">Swagger UI. You can watch it here: localhost:8080/swagger-ui</a>
### Curl tests:

#### get All Users
`curl -s http://localhost:8080/api/admin/users --user admin@gmail.com:adminPassword`

#### get User 3
`curl -s http://localhost:8080/api/admin/users/3 --user admin@gmail.com:adminPassword`

#### register User
`curl -s -i -X POST -d '{"name":"New User","lastName":"NewLast","email":"test@mail.ru","password":"test-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/profile`

#### get Profile
`curl -s http://localhost:8080/api/profile --user test@mail.ru:test-password`

#### get All Restaurants
`curl -s http://localhost:8080/api/user/restaurants --user user@gmail.com:userPassword`

#### get Restaurants with dishes
`curl -s http://localhost:8080/api/user/restaurants/2/with-dishes --user user@gmail.com:userPassword`

#### post Restaurant
`curl -s -i -X POST -d '{"name": "newRestaurant","address": "newTown","email": "new@mac.com"}}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/user/restaurants --user user@gmail.com:userPassword`

#### get All Dishes for restaurant 2
`curl -s http://localhost:8080/api/user/dishes/2 --user user@gmail.com:userPassword`

#### put Dish 2 for restaurant 1
`curl -s -X PUT -d '{"id": 2,"name": "UpdateDish","price": 800,"restaurantId": 1}' -H 'Content-Type: application/json' http://localhost:8080/api/manager/dishes/2 --user manager@gmail.com:managerPassword`

#### get Vote 1
`curl -s http://localhost:8080/api/user/votes/1 --user user@gmail.com:userPassword`

#### post Vote
`curl -s -i -X POST -d '{"restaurantId": 2}}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/user/votes --user user@gmail.com:userPassword`



#### validate with Error
`curl -s -X POST -d '{}' -H 'Content-Type: application/json' http://localhost:8080/api/admin/users --user admin@gmail.com:adminPassword`

`curl -s -X PUT -d '{"address":"Москва"}' -H 'Content-Type: application/json' http://localhost:8080/api/manager/restaurants --user manager@gmail.com:managerPassword`

`curl -s -X PUT -d '{"id": 2,"name": "UpdateDish","price": 800,"restaurantId": 2}' -H 'Content-Type: application/json' http://localhost:8080/api/manager/dishes/2 --user manager@gmail.com:managerPassword`


### Voting system for deciding where to have lunch
For further reference, please consider the following sections:

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
  - If it is before 11:00 we assume that he changed his mind.
  - If it is after 11:00 then it is too late, vote can't be changed






- <a href="https://github.com/JavaWebinar/topjava/blob/doc/doc/graduation.md">Graduation hints</a>
