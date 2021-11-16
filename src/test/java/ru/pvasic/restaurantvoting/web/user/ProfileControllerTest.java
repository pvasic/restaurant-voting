package ru.pvasic.restaurantvoting.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.pvasic.restaurantvoting.model.User;
import ru.pvasic.restaurantvoting.repository.UserRepository;
import ru.pvasic.restaurantvoting.to.UserTo;
import ru.pvasic.restaurantvoting.util.JsonUtil;
import ru.pvasic.restaurantvoting.util.UserUtil;
import ru.pvasic.restaurantvoting.web.AbstractControllerTest;
import ru.pvasic.restaurantvoting.web.error.GlobalExceptionHandler;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.*;
import static ru.pvasic.restaurantvoting.web.user.ProfileController.URL;

class ProfileControllerTest extends AbstractControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(user));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL))
                .andExpect(status().isNoContent());
        MATCHER.assertMatch(userRepository.findAll(), manager, admin);
    }

    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null, "New_First", "New_Last", "new@gmail.com", "newPass");
        User newUser = UserUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        MATCHER.assertMatch(created, newUser);
        MATCHER.assertMatch(userRepository.getById(newId), newUser);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "UpdatedFirst", "UpdatedLast", USER_MAIL, "updatedPassword");
        perform(MockMvcRequestBuilders.put(URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MATCHER.assertMatch(userRepository.getById(USER_ID), UserUtil.updateFromTo(new User(user), updatedTo));
    }

    @Test
    void registerInvalid() throws Exception {
        UserTo newTo = new UserTo(null, null, null, null, null);
        perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateInvalid() throws Exception {
        UserTo updatedTo = new UserTo(null, null, null, null, "password");
        perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateDuplicate() throws Exception {
        UserTo updatedTo = new UserTo(null, "New_First", "New_Last", MANAGER_MAIL, "newPass");
        perform(MockMvcRequestBuilders.put(URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL)));
    }
}