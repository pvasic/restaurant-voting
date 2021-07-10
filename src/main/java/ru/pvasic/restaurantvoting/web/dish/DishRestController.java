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
import ru.pvasic.restaurantvoting.repository.RestaurantRepository;
import ru.pvasic.restaurantvoting.util.validation.ValidationUtil;

import java.net.URI;
import java.util.List;

import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;
import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkNew;
import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkSingleModification;

@RestController
@RequestMapping(value = DishRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class DishRestController {
    static final String REST_URL = "/api/rest";

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/user/dish/{id}")
    public ResponseEntity<Dish> get(@PathVariable int id) {
        log.info("get dish {}", id);
        return ResponseEntity.of(dishRepository.findById(id));
    }

    @DeleteMapping("/manager/dish/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("delete {} for user {}", id, authUser.id());
        checkSingleModification(dishRepository.delete(id, authUser.id()), "Dish id=" + id + ", user id=" + authUser.id() + " missed");
    }

    @GetMapping("/user/restaurant/{restaurantId}/dish")
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("getAll for restaurant {}", restaurantId);
        return dishRepository.getAll(restaurantId);
    }


    @PutMapping(value = "/manager/restaurant/{restaurantId}/dish/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestBody Dish dish, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update {} for restaurant {}", dish, authUser.id());
        assureIdConsistent(dish, id);
        ValidationUtil.checkNotFoundWithId(dishRepository.get(id, restaurantId, authUser.id()),
                "Meal id=" + id + ", restaurant id=" + restaurantId + " doesn't belong to user id=" + authUser.id());
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        dish.setUserId(authUser.id());
        dishRepository.save(dish);
    }

    @PostMapping(value = "/manager/restaurant/{restaurantId}/dish", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestBody Dish dish, @PathVariable int restaurantId) {
        log.info("create {} for user {}", dish, authUser.id());
        checkNew(dish);
        dish.setUserId(authUser.id());
        dish.setRestaurant(ValidationUtil.checkNotFoundWithId(restaurantRepository.getWithCheck(restaurantId, authUser.id()),
                "Restaurant id=" + restaurantId + " doesn't belong to user id=" + authUser.id()));
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/user/dish/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/manager/history/restaurant/{restaurantId}")
    public List<Dish> getHistoryAll(@AuthenticationPrincipal AuthUser authUser, @PathVariable int restaurantId) {
        log.info("getHistoryAll for user {}", authUser.id());
        ValidationUtil.checkNotFoundWithId(restaurantRepository.getWithCheck(restaurantId, authUser.id()),
                "Restaurant id=" + restaurantId + " doesn't belong to user id=" + authUser.id());
        return dishRepository.getHistoryAll(restaurantId);
    }

//    @GetMapping("/filter")
//    public List<Dish> getBetween(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//                                 @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//
//        int userId = authUser.id();
//        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
//        List<Dish> dishsDateFiltered = dishRepository.getBetweenHalfOpen(atStartOfDayOrMin(startDate), atStartOfNextDayOrMax(endDate), userId);
//        return DishsUtil.getFilteredTos(dishsDateFiltered, authUser.getUser().getCaloriesPerDay(), startTime, endTime);
//    }
}