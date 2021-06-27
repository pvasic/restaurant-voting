package ru.pvasic.restaurantvoting.web;

import ru.pvasic.restaurantvoting.model.Dish;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DishTestData {
    public static final ru.pvasic.restaurantvoting.TestMatcher<Dish> DISH_MATCHER = ru.pvasic.restaurantvoting.TestMatcher.usingIgnoringFieldsComparator(Dish.class, "restaurant");

    public static final String USER_EMAIL = "user@gmail.com";
    public static final String MANAGER_EMAIL = "manager@gmail.com";
    public static final String ADMIN_EMAIL = "admin@javaops.ru";

    public static final int USER_ID = 1;
    public static final int MANAGER_ID = 2;
    public static final int ADMIN_ID = 3;


    public static final int DISH1_ID = 1;
    public static final int DISH2_ID = 2;
    public static final int DISH3_ID = 3;
    public static final int DISH4_ID = 4;

    public static final Dish DISH_1 = new Dish(DISH1_ID, "chicken", 250, LocalDateTime.of(2021, Month.MAY, 7, 9, 10, 0), 2);
    public static final Dish DISH_2 = new Dish(DISH2_ID, "rise", 300, LocalDateTime.of(2021, Month.MAY, 6, 13, 0, 0), 2);
    public static final Dish DISH_3 = new Dish(DISH3_ID, "Картошка", 100, LocalDateTime.of(2021, Month.MAY, 8, 18, 0, 0), 3);
    public static final Dish DISH_4 = new Dish(DISH4_ID, "Рис", 200, LocalDateTime.of(2021, Month.MAY, 8, 10, 0, 0), 3);

    public static final List<Dish> DISHES = List.of(DISH_2, DISH_1);

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Обновлённое блюдо", 555, DISH_1.getCreated().plus(2, ChronoUnit.MINUTES), 2);
    }


    public static Dish getNew() {
        return new Dish(null, "Новое бдюдо", 333, LocalDateTime.of(2021, Month.MAY, 7, 9, 15, 0), 2);
    }
}
