package ru.pvasic.restaurantvoting.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.repository.dish.DishRepository;
import ru.pvasic.restaurantvoting.util.JsonUtil;
import ru.pvasic.restaurantvoting.web.AbstractControllerTest;
import ru.pvasic.restaurantvoting.web.DishTestData;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.pvasic.restaurantvoting.TestUtil.readFromJson;
import static ru.pvasic.restaurantvoting.web.DishTestData.DISH1_ID;
import static ru.pvasic.restaurantvoting.web.DishTestData.DISHES;
import static ru.pvasic.restaurantvoting.web.DishTestData.DISH_1;
import static ru.pvasic.restaurantvoting.web.DishTestData.DISH_MATCHER;
import static ru.pvasic.restaurantvoting.web.DishTestData.MANAGER_EMAIL;
import static ru.pvasic.restaurantvoting.web.DishTestData.MANAGER_ID;
import static ru.pvasic.restaurantvoting.web.DishTestData.USER_EMAIL;
import static ru.pvasic.restaurantvoting.web.RestaurantTestData.RESTAURANT1_ID;

class DishControllerTest extends AbstractControllerTest {

    private final static String BASE_URL = DishController.BASE_URL + "/";

    @Autowired
    private DishRepository dishRepository;

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(BASE_URL + "user/dish/" + DISH1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISH_1));
    }

    @Test
    @WithUserDetails(value = MANAGER_EMAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(BASE_URL + "manager/dish/" + DISH1_ID))
                .andExpect(status().isNoContent());
        assertFalse(dishRepository.get(DISH1_ID, RESTAURANT1_ID, MANAGER_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(BASE_URL + "user/restaurant/" + RESTAURANT1_ID + "/dish"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISHES));
    }

    @Test
    @WithUserDetails(value = MANAGER_EMAIL)
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        performPut(updated);
        DISH_MATCHER.assertMatch(dishRepository.get(DISH1_ID, RESTAURANT1_ID, MANAGER_ID).get(), updated);
    }

    @Test
    @WithUserDetails(value = MANAGER_EMAIL)
    void createWithLocation() throws Exception {
        Dish newDish = DishTestData.getNew();
        ResultActions action = getPerformPost(newDish);

        Dish created = readFromJson(action, Dish.class);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishRepository.get(newId, RESTAURANT1_ID, MANAGER_ID).get(), newDish);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @WithUserDetails(value = MANAGER_EMAIL)
    void getHistoryAll() throws Exception {
        Dish newDish = DishTestData.getNew();
        ResultActions action = getPerformPost(newDish);

        Dish created = readFromJson(action, Dish.class);
        int newId = created.id();
        newDish.setId(newId);

        Dish updated = DishTestData.getUpdated();
        performPut(updated);
        DISH_MATCHER.assertMatch(created, newDish);
        perform(MockMvcRequestBuilders.get(BASE_URL + "manager/history/restaurant/" + RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(newDish, updated));
    }

    private ResultActions getPerformPost(Dish newDish) throws Exception {
        return perform(MockMvcRequestBuilders.post(BASE_URL + "manager/restaurant/" + RESTAURANT1_ID + "/dish/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)));
    }

    private void performPut(Dish updated) throws Exception {
        perform(MockMvcRequestBuilders.put(BASE_URL + "manager/restaurant/" + RESTAURANT1_ID + "/dish/" + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
    }
}