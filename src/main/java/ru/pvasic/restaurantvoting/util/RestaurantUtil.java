package ru.pvasic.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import ru.pvasic.restaurantvoting.model.Restaurant;
import ru.pvasic.restaurantvoting.to.RestaurantTo;

@UtilityClass
public class RestaurantUtil {
    public static Restaurant fromTo(RestaurantTo rTo, int userId) {
        return new Restaurant(rTo.getId(), userId, rTo.getName(), rTo.getAddress(), rTo.getEmail());
    }
}