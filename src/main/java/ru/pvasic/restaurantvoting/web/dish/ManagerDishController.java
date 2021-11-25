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
import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.repository.dish.DishRepository;
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;
import ru.pvasic.restaurantvoting.to.DishTo;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.pvasic.restaurantvoting.util.DishUtil.fromTo;
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
        repository.checkBelong(id, userId);
        repository.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody DishTo dishTo, @PathVariable int id) {
        int userId = authUser.id();
        log.info("update {} for user {}", dishTo, userId);
        assureIdConsistent(dishTo, id);
        repository.checkBelong(id, userId);

        // TODO add check unique date-name
        repository.save(fromTo(dishTo, userId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody DishTo dishTo) {
        int userId = authUser.id();
        log.info("create {} for user {}", dishTo, userId);
        checkNew(dishTo);
        restaurantRepository.checkBelong(dishTo.getRestaurantId(), userId);

        // TODO add check unique date-name
        Dish created = repository.save(fromTo(dishTo, userId));
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
}