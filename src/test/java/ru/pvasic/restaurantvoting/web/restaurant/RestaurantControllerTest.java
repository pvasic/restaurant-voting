package ru.pvasic.restaurantvoting.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.pvasic.restaurantvoting.model.Restaurant;
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;
import ru.pvasic.restaurantvoting.to.RestaurantTo;
import ru.pvasic.restaurantvoting.util.JsonUtil;
import ru.pvasic.restaurantvoting.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.pvasic.restaurantvoting.util.RestaurantUtil.fromTo;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.MATCHER;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANTS;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.WITH_DISHES_MATCHER;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.getNew;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.getUpdated;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.restaurant_1;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.ADMIN_ID;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.ADMIN_MAIL;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.MANAGER_ID;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.MANAGER_MAIL;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.USER_MAIL;

class RestaurantControllerTest extends AbstractControllerTest {

    private final static String URL = RestaurantController.URL + "/";

    @Autowired
    private RestaurantRepository repository;

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

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getWithDishes() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + RESTAURANT1_ID + "/with-dishes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(WITH_DISHES_MATCHER.contentJson(restaurant_1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        RestaurantTo rTo = getNew();
        Restaurant newRestaurant = fromTo(rTo, ADMIN_ID);
        ResultActions action = perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)));

        Restaurant created = MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        MATCHER.assertMatch(created, newRestaurant);
        MATCHER.assertMatch(repository.getById(newId), newRestaurant);
    }

    @Test
    @WithUserDetails(value = MANAGER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + RESTAURANT1_ID))
                .andExpect(status().isNoContent());
        assertFalse(repository.get(RESTAURANT1_ID, MANAGER_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = MANAGER_MAIL)
    void update() throws Exception {
        RestaurantTo rTo = getUpdated();
        Restaurant updated = fromTo(rTo, MANAGER_ID);
        perform(MockMvcRequestBuilders.put(URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MATCHER.assertMatch(repository.getById(RESTAURANT1_ID), updated);
    }
}
