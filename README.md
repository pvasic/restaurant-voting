# REST API using Hibernate/Spring-Boot without frontend.

## Voting system for deciding where to have lunch

* 3 types of users: admin, manager and regular user
* Admin can edit users.
* Manager can input a restaurant and it's lunch menu of the day
* Menu changes each day (managers do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
  - If it is before 11:00 we assume that he changed his mind.
  - If it is after 11:00 then it is too late, vote can't be changed

### Swagger UI

<a href="http://localhost:8080/swagger-ui.html">You can watch it here: localhost:8080/swagger-ui</a>

Credential

    user@gmail.com : user

    manager@gmail.com : manager

    admin@gmail.com : admin


### Curl tests:

#### get All Users
    curl -s http://localhost:8080/api/admin/users --user admin@gmail.com:admin

#### get User 3
    curl -s http://localhost:8080/api/admin/users/3 --user admin@gmail.com:admin

#### register User
    curl -s -i -X POST -d '{"name":"New User","lastName":"NewLast","email":"test@mail.ru","password":"test-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/profile

#### get Profile
    curl -s http://localhost:8080/api/profile --user test@mail.ru:test-password

#### get All Restaurants
    curl -s http://localhost:8080/api/restaurants --user user@gmail.com:user

#### get Restaurant with dishes
    curl -s http://localhost:8080/api/restaurants/2/with-dishes --user user@gmail.com:user

#### post Restaurant
    curl -s -i -X POST -d '{"name": "newRestaurant","address": "newTown","email": "new@mac.com"}}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/restaurants --user user@gmail.com:user

#### get All Dishes for restaurant 2
    curl -s http://localhost:8080/api/dishes/2 --user user@gmail.com:user

#### put Dish 2 for restaurant 1
    curl -s -X PUT -d '{"id": 2,"name": "UpdateDish","price": 800,"restaurantId": 1}' -H 'Content-Type: application/json' http://localhost:8080/api/dishes/2 --user manager@gmail.com:manager

#### get Vote 1
    curl -s http://localhost:8080/api/votes/1 --user user@gmail.com:user

#### post Vote
    curl -s -i -X POST -d '{"restaurantId": 2}}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/votes --user user@gmail.com:user



#### validate with Error
    curl -s -X POST -d '{}' -H 'Content-Type: application/json' http://localhost:8080/api/admin/users --user admin@gmail.com:admin

    curl -s -X PUT -d '{"address":"Москва"}' -H 'Content-Type: application/json' http://localhost:8080/api/restaurants --user manager@gmail.com:manager

    curl -s -X PUT -d '{"id": 2,"name": "UpdateDish","price": 800,"restaurantId": 2}' -H 'Content-Type: application/json' http://localhost:8080/api/dishes/2 --user manager@gmail.com:manager
