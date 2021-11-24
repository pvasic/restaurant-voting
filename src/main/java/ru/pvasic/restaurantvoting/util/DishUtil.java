package ru.pvasic.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import ru.pvasic.restaurantvoting.model.Vote;
import ru.pvasic.restaurantvoting.to.VoteTo;

import java.time.LocalTime;

@UtilityClass
public class DishUtil {

    public static Vote createNewFromTo(VoteTo voteTo, int userId) {
        return new Vote(null, userId, voteTo.getRestaurantId());
    }

    public static Vote createUpdateFromTo(VoteTo voteTo, int userId) {
        return new Vote(voteTo.getId(), userId, voteTo.getRestaurantId());
    }
}