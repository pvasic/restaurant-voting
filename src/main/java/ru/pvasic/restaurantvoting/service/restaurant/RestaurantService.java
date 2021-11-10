package ru.pvasic.restaurantvoting.service.restaurant;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pvasic.restaurantvoting.model.Restaurant;
import ru.pvasic.restaurantvoting.repository.UserRepository;
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;

@Service
@AllArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Restaurant save(Restaurant restaurant, int userId) {
        restaurant.setUserId(userId);
        return restaurantRepository.save(restaurant);
    }
}
