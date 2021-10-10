package ru.pvasic.restaurantvoting.service.Dish;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.repository.dish.DishRepository;
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;

@Service
@AllArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Dish save(Dish dish, int userId, int restaurantId) {
        dish.setUserId(userId);
        dish.setRestaurant(restaurantRepository.checkBelong(restaurantId, userId));
        return dishRepository.save(dish);
    }
}
