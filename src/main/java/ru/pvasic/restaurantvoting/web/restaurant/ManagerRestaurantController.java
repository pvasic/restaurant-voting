package ru.pvasic.restaurantvoting.web.restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.pvasic.restaurantvoting.AuthUser;
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;
import ru.pvasic.restaurantvoting.to.RestaurantTo;
import ru.pvasic.restaurantvoting.util.RestaurantUtil;

import javax.validation.Valid;

import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = ManagerRestaurantController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = "restaurants")
public class ManagerRestaurantController {

    static final String URL = "/api/manager/restaurants";

    private final RestaurantRepository repository;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        int userId = authUser.id();
        log.info("delete {} for restaurant {}", id, userId);
        repository.checkBelong(id, userId);
        repository.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody RestaurantTo restaurantTo, @PathVariable int id) {
        int userId = authUser.id();
        log.info("update {} restaurant {}", restaurantTo, userId);
        assureIdConsistent(restaurantTo, id);
        repository.checkBelong(id, userId);
        repository.save(RestaurantUtil.fromTo(restaurantTo, userId));
    }
}
