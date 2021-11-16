package ru.pvasic.restaurantvoting.web.vote;

import ru.pvasic.restaurantvoting.MatcherFactory;
import ru.pvasic.restaurantvoting.model.Vote;

import java.time.LocalDateTime;
import java.time.Month;

import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.pvasic.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT2_ID;
import static ru.pvasic.restaurantvoting.web.user.UserTestData.USER_ID;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class);

    public static final int VOTE_ID = 1;

    public static final Vote vote = new Vote(VOTE_ID, USER_ID, RESTAURANT1_ID, LocalDateTime.of(2021, Month.MAY, 7, 9, 10, 0));

    public static Vote getUpdated() {
        return new Vote(VOTE_ID, USER_ID, RESTAURANT2_ID, LocalDateTime.of(2021, Month.MAY, 7, 10, 57, 0));
    }

    public static Vote getNew() {
        return new Vote(null, USER_ID, RESTAURANT2_ID, LocalDateTime.now());
    }
}
