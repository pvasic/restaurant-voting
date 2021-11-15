package ru.pvasic.restaurantvoting.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.pvasic.restaurantvoting.DishTestData;
import ru.pvasic.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.pvasic.restaurantvoting.RestaurantTestData.RESTAURANT1_ID;
import static ru.pvasic.restaurantvoting.RestaurantTestData.RESTAURANTS;
import static ru.pvasic.restaurantvoting.RestaurantTestData.RESTAURANT_MATCHER;
import static ru.pvasic.restaurantvoting.RestaurantTestData.WITH_DISHES_MATCHER;
import static ru.pvasic.restaurantvoting.RestaurantTestData.restaurant_1;
import static ru.pvasic.restaurantvoting.UserTestData.USER_MAIL;

class RestaurantControllerTest extends AbstractControllerTest {

    private final static String REST_URL = RestaurantController.REST_URL + "/";

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "user/restaurants/" + RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant_1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "user/restaurants"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANTS));
    }

    // TODO add test
    @Test
    @WithUserDetails(value = USER_MAIL)
    void getWithDishes() throws Exception {

        //TODO mvn test not passed, dishes = null
        restaurant_1.setDishes(DishTestData.dishes);

        perform(MockMvcRequestBuilders.get(REST_URL + "user/restaurants/" + RESTAURANT1_ID + "/with-dishes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(WITH_DISHES_MATCHER.contentJson(restaurant_1));
    }
}