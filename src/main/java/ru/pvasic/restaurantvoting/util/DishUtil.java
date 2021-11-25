package ru.pvasic.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.model.Vote;
import ru.pvasic.restaurantvoting.to.DishTo;
import ru.pvasic.restaurantvoting.to.VoteTo;

import java.time.LocalTime;

@UtilityClass
public class DishUtil {

    public static Dish dishFromTo(DishTo dishTo, int userId) {
        return new Dish(dishTo.getId(), dishTo.getName(), dishTo.getPrice(), dishTo.getRestaurantId(), userId);
    }

    public static Dish updateFromTo(DishTo dishTo, int id, int userId) {
        return new Dish(id, dishTo.getName(), dishTo.getPrice(), dishTo.getRestaurantId(), userId);
    }

    public static Dish newFromTo(DishTo dishTo, int userId) {
        return new Dish(null, dishTo.getName(), dishTo.getPrice(), dishTo.getRestaurantId(), userId);
    }
}