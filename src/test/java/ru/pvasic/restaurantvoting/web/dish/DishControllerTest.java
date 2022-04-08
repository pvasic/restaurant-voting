package ru.pvasic.restaurantvoting.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.repository.dish.DishRepository;
import ru.pvasic.restaurantvoting.to.DishTo;
import ru.pvasic.restaurantvoting.util.JsonUtil;
import ru.pvasic.restaurantvoting.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.pvasic.restaurantvoting.util.DishUtil.fromTo;
import static ru.pvasic.restaurantvoting.web.dish.DishTestData.DISH1_ID;
import static ru.pvasic.restaurantvoting.web.dish.DishTestData.DISH_1;
import static ru.pvasic.restaurantvoting.web.dish.DishTestData.MATCHER;
import static ru.pvasic.restaurantvoting.web.dish.DishTestData.dishes1;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.MANAGER_ID;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.MANAGER_MAIL;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.USER_MAIL;

class DishControllerTest extends AbstractControllerTest {

    private final static String URL = DishController.URL + "/";

    @Autowired
    private DishRepository repository;

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

    @Test
    @WithUserDetails(value = MANAGER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + DISH1_ID)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID)))
                .andExpect(status().isNoContent());
        assertFalse(repository.get(DISH1_ID, MANAGER_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = MANAGER_MAIL)
    void update() throws Exception {
        DishTo dishTo = DishTestData.getUpdated();
        Dish updated = fromTo(dishTo, MANAGER_ID);
        callPerformPut(updated);
        MATCHER.assertMatch(repository.getById(DISH1_ID), updated);
    }

    @Test
    @WithUserDetails(value = MANAGER_MAIL)
    void createWithLocation() throws Exception {
        DishTo dishTo = DishTestData.getNew();
        Dish newDish = fromTo(dishTo, MANAGER_ID);
        ResultActions action = callPerformPost(newDish);

        Dish created = DishTestData.MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        MATCHER.assertMatch(created, newDish);
        MATCHER.assertMatch(repository.getById(newId), newDish);
    }

    private ResultActions callPerformPost(Dish newDish) throws Exception {
        return perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)));
    }

    private void callPerformPut(Dish updated) throws Exception {
        perform(MockMvcRequestBuilders.put(URL + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
    }
}
