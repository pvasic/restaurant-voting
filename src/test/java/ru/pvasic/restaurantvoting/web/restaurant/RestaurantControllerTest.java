package ru.pvasic.restaurantvoting.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.pvasic.restaurantvoting.web.dish.DishTestData;
import ru.pvasic.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANTS;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.MATCHER;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.WITH_DISHES_MATCHER;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.restaurant_1;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.USER_MAIL;

class RestaurantControllerTest extends AbstractControllerTest {

    private final static String URL = RestaurantController.URL + "/";

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(restaurant_1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(RESTAURANTS));
    }

    // TODO add test
    @Test
    @WithUserDetails(value = USER_MAIL)
    void getWithDishes() throws Exception {

        //TODO mvn test not passed, dishes = null
        restaurant_1.setDishes(DishTestData.dishes);

        perform(MockMvcRequestBuilders.get(URL + RESTAURANT1_ID + "/with-dishes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(WITH_DISHES_MATCHER.contentJson(restaurant_1));
    }
}