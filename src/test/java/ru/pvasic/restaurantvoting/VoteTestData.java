package ru.pvasic.restaurantvoting;

import ru.pvasic.restaurantvoting.model.Vote;

import java.time.LocalDateTime;
import java.time.Month;

import static ru.pvasic.restaurantvoting.RestaurantTestData.RESTAURANT2_ID;
import static ru.pvasic.restaurantvoting.UserTestData.ADMIN_ID;
import static ru.pvasic.restaurantvoting.UserTestData.MANAGER_ID;

public class VoteTestData {
    public static final TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingEqualsComparator(Vote.class);
    // field "$$_hibernate_interceptor"

    public static final int VOTE_ID = 1;

    public static final Vote VOTE = new Vote(VOTE_ID, MANAGER_ID, LocalDateTime.of(2021, Month.MAY, 7, 9, 10, 0));

    public static Vote getUpdated() {
        return new Vote(VOTE_ID, ADMIN_ID, LocalDateTime.of(2021, Month.MAY, 7, 10, 57, 0));
    }

    public static Vote getNew() {
        return new Vote(null, RESTAURANT2_ID, LocalDateTime.now());
    }
}
