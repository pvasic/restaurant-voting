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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.pvasic.restaurantvoting.AuthUser;
import ru.pvasic.restaurantvoting.error.IllegalRequestDataException;
import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.model.Restaurant;
import ru.pvasic.restaurantvoting.repository.dish.DishRepository;
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;

import java.net.URI;
import java.util.List;

import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;
import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ManagerDishController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ManagerDishController {
    static final String URL = "/api/manager/dishes";

    private final DishRepository repository;
    private final RestaurantRepository restaurantRepository;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id, @RequestParam int restaurantId) {
        int userId = authUser.id();
        log.info("delete dish with id = {} for user {}", id, userId);
        checkBelong(id, restaurantId, userId);
        repository.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestBody Dish dish, @PathVariable int id) {
        int userId = authUser.id();
        log.info("update {} for restaurant {}", dish, userId);
        assureIdConsistent(dish, id);
        checkBelong(id, dish.getRestaurantId(), userId);

        // TODO add check unique date-name
        repository.save(dish);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestBody Dish dish, @RequestParam int restaurantId) {
        int userId = authUser.id();
        log.info("create {} for user {}", dish, userId);
        checkNew(dish);
        restaurantRepository.checkBelong(restaurantId, userId);
        dish.setRestaurantId(restaurantId);
        Dish created = repository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/user/dishes/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/history-dishes")
    public List<Dish> getHistoryAll(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurantId) {
        int userId = authUser.id();
        log.info("get restaurant history for user {}", userId);
        restaurantRepository.checkBelong(restaurantId, userId);
        return repository.getHistoryAll(restaurantId);
    }

    private void checkBelong(int id, int restaurantId, int userId) {

        // To simplify verification, you can add idUser to Dish or bi-directional Dish-Restaurant relationship to the Dish.
        Restaurant restaurant = restaurantRepository.getWithDishes(restaurantId).orElseThrow(
                () -> new IllegalRequestDataException("Restaurant for id=" + id + " not found!"));
        List<Dish> dishes = restaurant.getDishes();
        if (!dishes.isEmpty()) {
            if (dishes.stream().noneMatch(d -> d.getId() == id)) {
                if (restaurant.getUserId() != userId) {
                    throw new IllegalRequestDataException("Restaurant for id=" + restaurantId + " doesn't belong to User id=" + userId);
                } else {
                    throw new IllegalRequestDataException("Dish for id=" + id + " not found!");
                }
            }
        } else {
            throw new IllegalRequestDataException("Dish for id=" + id + " not found!");
        }
    }
}