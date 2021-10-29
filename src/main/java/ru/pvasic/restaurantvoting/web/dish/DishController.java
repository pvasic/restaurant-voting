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
import ru.pvasic.restaurantvoting.repository.dish.DishRepository;
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;
import ru.pvasic.restaurantvoting.service.Dish.DishService;

import java.net.URI;
import java.util.List;

import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;
import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class DishController {
    static final String REST_URL = "/api";

    private final DishRepository dishRepository;
    private final DishService dishService;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/user/dish/{id}")
    public ResponseEntity<Dish> get(@PathVariable int id) {
        log.info("get dish {}", id);
        return ResponseEntity.of(dishRepository.findById(id));
    }

    @DeleteMapping("/manager/dish/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        int userId = authUser.id();
        log.info("delete {} for user {}", id, userId);
        dishRepository.checkBelong(id, userId);
        dishRepository.delete(id);
    }

    @GetMapping("/user/restaurant/{restaurantId}/dish")
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("getAll for restaurant {}", restaurantId);
        return dishRepository.getAll(restaurantId);
    }


    @PutMapping(value = "/manager/restaurant/{restaurantId}/dish/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestBody Dish dish, @PathVariable int restaurantId, @PathVariable int id) {
        int userId = authUser.id();
        log.info("update {} for restaurant {}", dish, userId);
        assureIdConsistent(dish, id);
        dishRepository.checkBelong(id, userId);
        dishService.save(dish, userId, restaurantId);
    }

    @PostMapping(value = "/manager/restaurant/{restaurantId}/dish", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestBody Dish dish, @PathVariable int restaurantId) {
        int userId = authUser.id();
        log.info("create {} for user {}", dish, userId);
        checkNew(dish);
        Dish created = dishService.save(dish, userId, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/user/dish/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/manager/history/restaurant/{restaurantId}")
    public List<Dish> getHistoryAll(@AuthenticationPrincipal AuthUser authUser, @PathVariable int restaurantId) {
        int userId = authUser.id();
        log.info("getHistoryAll for user {}", userId);
        restaurantRepository.checkBelong(restaurantId, userId);
        return dishRepository.getHistoryAll(restaurantId);
    }
}