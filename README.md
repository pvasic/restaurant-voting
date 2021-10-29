# Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

- <a href="https://github.com/JavaWebinar/topjava/blob/doc/doc/graduation.md">Graduation hints</a>

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

####2.6: На управление (CRUD) ресторанами и едой должны быть ОТДЕЛЬНЫЕ контроллеры. Не надо все, что может админ, сваливать в одну кучу!

####2.7: Проверьте в Swagger, что в POST и PUT нет ничего лишнего, а в GET есть все необходимые данные. Например, при запросе голоса должен быть id ресторана, а при создании-редактировании ресторана не должно быть меню и еды.

####5.3: Не смешивайте TO и Entity вместе. Они должны быть независимыми друг от друга. На TopJava мы смотрели разные варианты c использованием TO и без. Делаем максимально просто.