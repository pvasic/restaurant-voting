package ru.pvasic.restaurantvoting.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.util.JsonUtil;
import ru.pvasic.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.pvasic.restaurantvoting.web.dish.DishTestData.DISH1_ID;
import static ru.pvasic.restaurantvoting.web.dish.DishTestData.DISH_1;
import static ru.pvasic.restaurantvoting.web.dish.DishTestData.MATCHER;
import static ru.pvasic.restaurantvoting.web.dish.DishTestData.dishes;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.USER_MAIL;

class DishControllerTest extends AbstractControllerTest {

    private final static String REST_URL = DishController.REST_URL + "/";

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(DISH_1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "restaurants/" + RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(dishes));
    }
// TODO mvn test not passed, fix CustomDishRepositoryImpl
//    @Test
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
//    @WithUserDetails(value = MANAGER_MAIL)
//    void getHistoryAll() throws Exception {
//        Dish newDish = DishTestData.getNew();
//        ResultActions action = getPerformPost(newDish);
//
//        Dish created = readFromJson(action, Dish.class);
//        int newId = created.id();
//        newDish.setId(newId);
//
//        Dish updated = DishTestData.getUpdated();
//        performPut(updated);
//        DISH_MATCHER.assertMatch(created, newDish);
//        perform(MockMvcRequestBuilders.get(REST_URL + "user/history/restaurants/" + RESTAURANT1_ID))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(DISH_MATCHER.contentJson(newDish, updated));
//        repository.deleteAll();
//    }

    private ResultActions getPerformPost(Dish newDish) throws Exception {
        return perform(MockMvcRequestBuilders.post(REST_URL + "manager/restaurants/" + RESTAURANT1_ID + "/dishes/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)));
    }

    private void performPut(Dish updated) throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "manager/dishes/" + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
    }
}