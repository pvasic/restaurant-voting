package ru.pvasic.restaurantvoting.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.pvasic.restaurantvoting.RestaurantTestData;
import ru.pvasic.restaurantvoting.model.Restaurant;
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;
import ru.pvasic.restaurantvoting.util.JsonUtil;
import ru.pvasic.restaurantvoting.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.pvasic.restaurantvoting.RestaurantTestData.RESTAURANT1_ID;
import static ru.pvasic.restaurantvoting.RestaurantTestData.RESTAURANTS;
import static ru.pvasic.restaurantvoting.RestaurantTestData.RESTAURANT_1;
import static ru.pvasic.restaurantvoting.RestaurantTestData.RESTAURANT_MATCHER;
import static ru.pvasic.restaurantvoting.TestUtil.readFromJson;
import static ru.pvasic.restaurantvoting.UserTestData.MANAGER_ID;
import static ru.pvasic.restaurantvoting.UserTestData.MANAGER_MAIL;
import static ru.pvasic.restaurantvoting.UserTestData.USER_MAIL;

class RestaurantRestControllerTest extends AbstractControllerTest {

    private final static String REST_URL = RestaurantRestController.REST_URL + "/";

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "user/restaurant/" + RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANT_1));
    }

    @Test
    @WithUserDetails(value = MANAGER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "manager/restaurant/" + RESTAURANT_1))
                .andExpect(status().isNoContent());
        assertFalse(restaurantRepository.get(RESTAURANT1_ID, MANAGER_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "user/restaurant"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANTS));
    }

    @Test
    @WithUserDetails(value = MANAGER_MAIL)
    void update() throws Exception {
        Restaurant updated = RestaurantTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + "manager/restaurant/" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(restaurantRepository.get(RESTAURANT1_ID, MANAGER_ID).get(), updated);
    }

    @Test
    @WithUserDetails(value = MANAGER_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = RestaurantTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "manager/restaurant/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)));

        Restaurant created = readFromJson(action, Restaurant.class);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.get(newId, MANAGER_ID).get(), newRestaurant);
    }

    @Test
    void getWithDishes() {
    }
}