package ru.pvasic.restaurantvoting.web.restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
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
import ru.pvasic.restaurantvoting.model.Restaurant;
import ru.pvasic.restaurantvoting.repository.UserRepository;
import ru.pvasic.restaurantvoting.repository.dish.DishRepository;
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;
import ru.pvasic.restaurantvoting.util.validation.ValidationUtil;

import java.net.URI;
import java.util.List;

import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;
import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkNew;
import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkSingleModification;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantRestController {
    static final String REST_URL = "/api/rest";

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @GetMapping("/user/restaurant/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        return ResponseEntity.of(restaurantRepository.findById(id));
    }

    @DeleteMapping("/manager/restaurant/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("delete {} for restaurant {}", id, authUser.id());
        checkSingleModification(restaurantRepository.delete(id, authUser.id()), "Restaurant id=" + id + ", user id=" + authUser.id() + " missed");
    }

    @GetMapping("/user/restaurant")
    public List<Restaurant> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("getAll restaurants for user {}", authUser.id());
        return restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    @PutMapping(value = "/manager/restaurant/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} restaurant {}", restaurant, authUser.id());
        assureIdConsistent(restaurant, id);
        ValidationUtil.checkNotFoundWithId(restaurantRepository.get(id, authUser.id()),
                "Restaurant id=" + id + " doesn't belong to user id=" + authUser.id());
        restaurant.setUser(userRepository.getOne(authUser.id()));
        restaurantRepository.save(restaurant);
    }

    @PostMapping(value = "/manager/restaurant", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestBody Restaurant restaurant) {
        log.info("create {} for user {}", restaurant, authUser.id());
        checkNew(restaurant);
        restaurant.setUser(userRepository.getOne(authUser.id()));
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/user/restaurant/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping(value = "/user/restaurant/{id}/with-dishes")
    public ResponseEntity<Restaurant> getWithDishes(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        return ResponseEntity.of(restaurantRepository.getWithDishes(id));
    }
}
