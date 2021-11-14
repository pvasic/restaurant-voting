package ru.pvasic.restaurantvoting;

import ru.pvasic.restaurantvoting.model.Vote;

import java.time.LocalDateTime;
import java.time.Month;

import static ru.pvasic.restaurantvoting.RestaurantTestData.RESTAURANT1_ID;
import static ru.pvasic.restaurantvoting.RestaurantTestData.RESTAURANT2_ID;
import static ru.pvasic.restaurantvoting.UserTestData.USER_ID;

public class VoteTestData {
    public static final TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Vote.class);

    public static final int VOTE_ID = 1;

    public static final Vote vote = new Vote(VOTE_ID, USER_ID, RESTAURANT1_ID, LocalDateTime.of(2021, Month.MAY, 7, 9, 10, 0));

    public static Vote getUpdated() {
        Vote vote = new Vote(VOTE_ID, USER_ID, RESTAURANT2_ID, LocalDateTime.of(2021, Month.MAY, 7, 10, 57, 0));
        return vote;
    }

    public static Vote getNew() {
        return new Vote(null, USER_ID, RESTAURANT2_ID, LocalDateTime.now());
    }
}
