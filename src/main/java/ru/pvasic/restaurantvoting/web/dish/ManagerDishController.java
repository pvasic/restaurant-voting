package ru.pvasic.restaurantvoting.web.dish;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.pvasic.restaurantvoting.AuthUser;
import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.model.Restaurant;
import ru.pvasic.restaurantvoting.repository.dish.DishRepository;
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;

import java.net.URI;
import java.util.List;

import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;
import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ManagerDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ManagerDishController {
    static final String REST_URL = "/api/manager/restaurants/{restaurantId}";

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @DeleteMapping("/dishes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int restaurantId, @PathVariable int id) {
        int userId = authUser.id();
        log.info("delete dish with id = {} for user {}", id, userId);
        dishRepository.checkBelong(id, restaurantId);
        dishRepository.delete(id);
    }

    @PutMapping(value = "/dishes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestBody Dish dish, @PathVariable int restaurantId, @PathVariable int id) {
        int userId = authUser.id();
        log.info("update {} for restaurant {}", dish, userId);
        assureIdConsistent(dish, id);
        dishRepository.checkBelong(id, dish.getRestaurantId());
        dishRepository.save(dish);
    }

    @PostMapping(value = "/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestBody Dish dish, @PathVariable int restaurantId) {
        int userId = authUser.id();
        log.info("create {} for user {}", dish, userId);
        checkNew(dish);
        Restaurant restaurant = restaurantRepository.checkBelong(restaurantId, userId);
        dish.setRestaurantId(restaurant.getId());
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/user/dishes/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/restaurants/{restaurantId}/history-dishes")
    public List<Dish> getHistoryAll(@AuthenticationPrincipal AuthUser authUser, @PathVariable int restaurantId) {
        int userId = authUser.id();
        log.info("get restaurant history for user {}", userId);
        restaurantRepository.checkBelong(restaurantId, userId);
        return dishRepository.getHistoryAll(restaurantId);
    }
}