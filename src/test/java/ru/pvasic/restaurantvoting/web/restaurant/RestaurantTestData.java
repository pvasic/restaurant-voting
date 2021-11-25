package ru.pvasic.restaurantvoting.web.restaurant;

import ru.pvasic.restaurantvoting.MatcherFactory;
import ru.pvasic.restaurantvoting.model.Restaurant;
import ru.pvasic.restaurantvoting.to.RestaurantTo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.pvasic.restaurantvoting.web.dish.DishTestData.dishes1;
import static ru.pvasic.restaurantvoting.web.dish.DishTestData.dishes2;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.ADMIN_ID;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.MANAGER_ID;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "created", "dishes");

    public static MatcherFactory.Matcher<Restaurant> WITH_DISHES_MATCHER =
            MatcherFactory.usingAssertions(Restaurant.class,
//     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("created", "dishes.restaurant").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;

    public static final Restaurant restaurant_1 = new Restaurant(RESTAURANT1_ID, MANAGER_ID, "Macdonalds", "Москва", "macdonalds@mac.com");
    public static final Restaurant restaurant_2 = new Restaurant(RESTAURANT2_ID, ADMIN_ID, "DoDoPizza", "Мытищи", "dodo@do.com");

    static {
        restaurant_1.setDishes(dishes1);
        restaurant_2.setDishes(dishes2);
    }

    public static final List<Restaurant> RESTAURANTS = List.of(restaurant_2, restaurant_1);

    public static RestaurantTo getUpdated() {
        return new RestaurantTo(RESTAURANT1_ID, "Обновленное Название", "Обновленный адрес", "updated@mac.com");

    }

    public static RestaurantTo getNew() {
        return new RestaurantTo(null, "Новое Название", "Новый адрес", "new@mac.com");
    }


}
