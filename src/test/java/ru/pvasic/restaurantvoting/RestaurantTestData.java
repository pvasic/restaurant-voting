package ru.pvasic.restaurantvoting;

import ru.pvasic.restaurantvoting.model.Restaurant;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.pvasic.restaurantvoting.DishTestData.*;
import static ru.pvasic.restaurantvoting.UserTestData.*;

public class RestaurantTestData {
    public static final ru.pvasic.restaurantvoting.TestMatcher<Restaurant> RESTAURANT_MATCHER = ru.pvasic.restaurantvoting.TestMatcher.usingIgnoringFieldsComparator(Restaurant.class, "dishes");

    public static ru.pvasic.restaurantvoting.TestMatcher<Restaurant> WITH_DISHES_MATCHER =
            ru.pvasic.restaurantvoting.TestMatcher.usingAssertions(Restaurant.class,
//     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("dishes.restaurant").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;

    public static final Restaurant restaurant_1 = new Restaurant(RESTAURANT1_ID, MANAGER_ID, "Macdonalds", "Москва", "macdonalds@mac.com", LocalDateTime.of(2021, Month.MAY, 1, 0, 0, 0));
    public static final Restaurant restaurant_2 = new Restaurant(RESTAURANT2_ID, ADMIN_ID, "DoDoPizza", "Мытищи", "dodo@do.com", LocalDateTime.of(2021, Month.MAY, 2, 3, 0, 0));

    static {
        restaurant_1.setDishes(dishes);
    }

    public static final List<Restaurant> RESTAURANTS = List.of(restaurant_2, restaurant_1);

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, USER_ID, "Обновленное Название", "Обновленный адрес", "updated@mac.com", LocalDateTime.of(2021, Month.SEPTEMBER, 5, 0, 0, 0));

    }

    public static Restaurant getNew() {
        return new Restaurant(null, ADMIN_ID, "Новое Название", "Новый адрес", "new@mac.com", LocalDateTime.of(2021, Month.DECEMBER, 9, 0, 0, 0));
    }


}
