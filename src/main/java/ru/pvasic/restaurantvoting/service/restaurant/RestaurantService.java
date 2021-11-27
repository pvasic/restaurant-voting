package ru.pvasic.restaurantvoting.service.restaurant;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pvasic.restaurantvoting.model.Restaurant;
import ru.pvasic.restaurantvoting.model.User;
import ru.pvasic.restaurantvoting.repository.UserRepository;
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;
import ru.pvasic.restaurantvoting.util.UserUtil;

import java.util.Optional;

import static ru.pvasic.restaurantvoting.util.UserUtil.updateRole;

@Service
@AllArgsConstructor
public class RestaurantService {
    private final RestaurantRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public Restaurant save(Restaurant restaurant, int userId) {
        User oldUser = userRepository.checkByUserId(userId);
        Optional<User> updated = updateRole(oldUser);
        updated.ifPresent(user -> userRepository.save(UserUtil.prepareToSave(user)));
        return repository.save(restaurant);
    }
}
