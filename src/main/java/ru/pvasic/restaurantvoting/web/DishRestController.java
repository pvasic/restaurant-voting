package ru.pvasic.restaurantvoting.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.repository.DishRepository;
import ru.pvasic.restaurantvoting.repository.RestaurantRepository;
import ru.pvasic.restaurantvoting.repository.UserRepository;
import ru.pvasic.restaurantvoting.util.ValidationUtil;

import java.net.URI;
import java.util.List;

import static ru.pvasic.restaurantvoting.util.ValidationUtil.assureIdConsistent;
import static ru.pvasic.restaurantvoting.util.ValidationUtil.checkNew;
import static ru.pvasic.restaurantvoting.util.ValidationUtil.checkSingleModification;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class DishRestController {
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final int userId = 1;
    private final int managerId = 3;

    @GetMapping("/rest/user/dish/{id}")
    public ResponseEntity<Dish> get(@PathVariable int id) {
        log.info("get dish {}", id);
        return ResponseEntity.of(dishRepository.findById(id));
    }

    @DeleteMapping("/rest/manager/restaurant/dish/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {} for user {}", id, managerId);
        checkSingleModification(dishRepository.delete(id, managerId), "Dish id=" + id + ", manager id=" + managerId + " missed");
    }

    @GetMapping("/rest/user/restaurant/{restaurantId}/dish")
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("getAll for user {}", restaurantId);
        return dishRepository.getAll(restaurantId);
    }


    @PutMapping(value = "/rest/manager/restaurant/{restaurantId}/dish/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Dish dish, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update {} for restaurant {}", dish, restaurantId);
        assureIdConsistent(dish, id);
        ValidationUtil.checkNotFoundWithId(dishRepository.getWithCheck(id, restaurantId, managerId),
                "Meal id=" + id + ", restaurant id=" + restaurantId + " doesn't belong to user id=" + managerId);
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        dishRepository.save(dish);
    }

    @PostMapping(value = "/rest/manager/restaurant/{restaurantId}/dish", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@RequestBody Dish dish, @PathVariable int restaurantId) {
        log.info("create {} for user {}", dish, managerId);
        checkNew(dish);
        dish.setUserId(managerId);
        dish.setRestaurant(ValidationUtil.checkNotFoundWithId(restaurantRepository.getWithCheck(restaurantId, managerId),
                "Restaurant id=" + restaurantId + " doesn't belong to user id=" + managerId));
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/rest/user/dish/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/rest/user/restaurant/{restaurantId}/dish/history")
    public List<Dish> getHistoryAll(@PathVariable int restaurantId) {
        log.info("getHistoryAll for user {}", restaurantId);
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