package ru.pvasic.restaurantvoting.web.dish;

import ru.pvasic.restaurantvoting.MatcherFactory;
import ru.pvasic.restaurantvoting.model.Dish;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static ru.pvasic.restaurantvoting.web.user.UserTestData.ADMIN_ID;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.MANAGER_ID;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurant");

    public static final int DISH1_ID = 1;
    public static final int DISH2_ID = 2;
    public static final int DISH3_ID = 3;
    public static final int DISH4_ID = 4;

    public static final Dish DISH_1 = new Dish(DISH1_ID, "chicken", 250, LocalDate.of(2021, Month.MAY, 7), 1, MANAGER_ID);
    public static final Dish DISH_2 = new Dish(DISH2_ID, "rise", 300, LocalDate.of(2021, Month.MAY, 6), 1, MANAGER_ID);
    public static final Dish DISH_3 = new Dish(DISH3_ID, "Картошка", 100, LocalDate.of(2021, Month.MAY, 8), 2, ADMIN_ID);
    public static final Dish DISH_4 = new Dish(DISH4_ID, "Рис", 200, LocalDate.of(2021, Month.MAY, 8), 2, ADMIN_ID);

    public static final List<Dish> dishes1 = List.of(DISH_1, DISH_2);
    public static final List<Dish> dishes2 = List.of(DISH_3, DISH_4);

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Обновлённое блюдо", 555, LocalDate.now(), 1, MANAGER_ID);

    }

    public static Dish getNew() {
        return new Dish(null, "Новое блюдо", 333, LocalDate.of(2021, Month.APRIL, 15), 1, MANAGER_ID);
    }
}
