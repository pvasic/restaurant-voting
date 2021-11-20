package ru.pvasic.restaurantvoting.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.pvasic.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.pvasic.restaurantvoting.web.dish.DishTestData.DISH1_ID;
import static ru.pvasic.restaurantvoting.web.dish.DishTestData.DISH_1;
import static ru.pvasic.restaurantvoting.web.dish.DishTestData.MATCHER;
import static ru.pvasic.restaurantvoting.web.dish.DishTestData.dishes1;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.USER_MAIL;

class DishControllerTest extends AbstractControllerTest {

    private final static String URL = DishController.URL + "/";

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + DISH1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(DISH_1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(DishController.URL)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(dishes1));
    }
}