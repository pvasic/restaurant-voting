package ru.pvasic.restaurantvoting.web.restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.pvasic.restaurantvoting.AuthUser;
import ru.pvasic.restaurantvoting.model.Restaurant;
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;
import ru.pvasic.restaurantvoting.service.restaurant.RestaurantService;
import ru.pvasic.restaurantvoting.to.RestaurantTo;
import ru.pvasic.restaurantvoting.util.RestaurantUtil;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = "restaurants")
public class RestaurantController {

    static final String URL = "/api/user/restaurants";

    private final RestaurantRepository repository;
    private final RestaurantService service;

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("get restaurant {} for user {}", id, authUser.id());
        return ResponseEntity.of(repository.findById(id));
    }

    @GetMapping
    @Cacheable
    public List<Restaurant> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("getAll restaurants for user {}", authUser.id());
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    @GetMapping(value = "/{id}/with-dishes")
    public ResponseEntity<Restaurant> getWithDishes(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("getAll restaurants with dishes for user {}", authUser.id());
        return ResponseEntity.of(repository.getWithDishes(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<Restaurant> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody RestaurantTo rTo) {
        int userId = authUser.id();
        log.info("create {} for user {}", rTo, userId);
        checkNew(rTo);
        Restaurant restaurant = RestaurantUtil.fromTo(rTo, userId);
        restaurant.setUserId(userId);
        Restaurant created = service.save(restaurant, userId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(RestaurantController.URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
