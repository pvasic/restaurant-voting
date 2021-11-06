package ru.pvasic.restaurantvoting.web.vote;

import com.sun.xml.bind.v2.TODO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.pvasic.restaurantvoting.model.Vote;
import ru.pvasic.restaurantvoting.repository.vote.VoteRepository;
import ru.pvasic.restaurantvoting.util.JsonUtil;
import ru.pvasic.restaurantvoting.web.AbstractControllerTest;
import ru.pvasic.restaurantvoting.web.VoteTestData;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.pvasic.restaurantvoting.RestaurantTestData.RESTAURANT2_ID;
import static ru.pvasic.restaurantvoting.TestUtil.readFromJson;
import static ru.pvasic.restaurantvoting.UserTestData.USER_MAIL;
import static ru.pvasic.restaurantvoting.web.VoteTestData.VOTE;
import static ru.pvasic.restaurantvoting.web.VoteTestData.VOTE_ID;
import static ru.pvasic.restaurantvoting.web.VoteTestData.VOTE_MATCHER;

class VoteControllerTest extends AbstractControllerTest {

    private final static String REST_URL = VoteController.REST_URL + "/";

    @Autowired
    VoteRepository repository;

    @Test
//    TODO fix (test passed without authorization)
//    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "user/votes/" + VOTE_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "user/votes/" + VOTE_ID))
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(VOTE_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + "user/votes/" + VOTE_ID)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        VOTE_MATCHER.assertMatch(repository.getById(VOTE_ID), updated);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createWithLocation() throws Exception {
        repository.delete(VOTE_ID);
        assertFalse(repository.findById(VOTE_ID).isPresent());
        Vote newVote = VoteTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "/user/restaurant/" + RESTAURANT2_ID + "/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)));

        Vote created = readFromJson(action, Vote.class);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(repository.getById(newId), newVote);
    }
}