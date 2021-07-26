package ru.pvasic.restaurantvoting;

import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.model.Restaurant;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class RestaurantTestData {
    public static final ru.pvasic.restaurantvoting.TestMatcher<Restaurant> RESTAURANT_MATCHER = ru.pvasic.restaurantvoting.TestMatcher.usingIgnoringFieldsComparator(Restaurant.class, "user", "dishes", "votes");


    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;

    public static final Restaurant RESTAURANT_1 = new Restaurant(RESTAURANT1_ID,"Macdonalds", "Москва", "macdonalds@mac.com", 0, LocalDateTime.of(2021, Month.MAY, 1, 0, 0, 0));
    public static final Restaurant RESTAURANT_2 = new Restaurant(RESTAURANT2_ID,"DoDoPizza", "Мытищи", "dodo@do.com", 0, LocalDateTime.of(2021, Month.MAY, 2, 3, 0, 0));

    public static final List<Restaurant> RESTAURANTS = List.of(RESTAURANT_2, RESTAURANT_1);

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID,"Обновлеенное Название", "Обновленный адрес", "updated@mac.com", 0, LocalDateTime.of(2021, Month.SEPTEMBER, 5, 0, 0, 0));
    }

    public static Restaurant getNew() {
        return new Restaurant(null,"Новое Название", "Новый адрес", "new@mac.com", 0, LocalDateTime.of(2021, Month.DECEMBER, 9, 0, 0, 0));
    }


}
