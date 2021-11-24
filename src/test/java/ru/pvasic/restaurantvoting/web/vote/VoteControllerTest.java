package ru.pvasic.restaurantvoting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.pvasic.restaurantvoting.model.Vote;
import ru.pvasic.restaurantvoting.repository.vote.VoteRepository;
import ru.pvasic.restaurantvoting.to.VoteTo;
import ru.pvasic.restaurantvoting.util.JsonUtil;
import ru.pvasic.restaurantvoting.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.pvasic.restaurantvoting.util.VoteUtil.*;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.*;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.*;
import static ru.pvasic.restaurantvoting.web.vote.VoteTestData.*;
import static ru.pvasic.restaurantvoting.web.vote.VoteTestData.MATCHER;

class VoteControllerTest extends AbstractControllerTest {

    private final static String URL = VoteController.URL + "/";

    @Autowired
    VoteRepository repository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + VOTE_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(vote));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + VOTE_ID))
                .andExpect(status().isNoContent());
        assertFalse(repository.get(VOTE_ID, USER_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        VoteTo updateTo = new VoteTo(VOTE_ID, RESTAURANT2_ID);
        Vote updated = voteFromTo(updateTo, USER_ID);
        perform(MockMvcRequestBuilders.put(URL + VOTE_ID)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeValue(updateTo)))
                .andExpect(status().isNoContent());
        MATCHER.assertMatch(repository.getById(VOTE_ID), updated);
    }

    @Test
    @WithUserDetails(value = MANAGER_MAIL)
    void createWithLocation() throws Exception {
        VoteTo newTo = new VoteTo(null, RESTAURANT2_ID);
        Vote newVote = voteFromTo(newTo, MANAGER_ID);
        ResultActions action = perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)));

        Vote created = MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        MATCHER.assertMatch(created, newVote);
        MATCHER.assertMatch(repository.getById(newId), newVote);
    }
}