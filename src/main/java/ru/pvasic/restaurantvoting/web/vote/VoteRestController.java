package ru.pvasic.restaurantvoting.web.vote;

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
import ru.pvasic.restaurantvoting.model.Vote;
import ru.pvasic.restaurantvoting.repository.UserRepository;
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;
import ru.pvasic.restaurantvoting.repository.vote.VoteRepository;
import ru.pvasic.restaurantvoting.util.validation.ValidationUtil;

import java.net.URI;
import java.util.List;

import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;
import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkNew;
import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkSingleModification;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteRestController {
    static final String REST_URL = "/api/rest";

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;

    @GetMapping("/user/vote/{id}")
    public ResponseEntity<Vote> get(@PathVariable int id) {
        log.info("get vote {}", id);
        return ResponseEntity.of(voteRepository.findById(id));
    }

    @DeleteMapping("/user/vote/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("delete {} for user {}", id, authUser.id());
        checkSingleModification(voteRepository.delete(id, authUser.id()), "Vote id=" + id + ", user id=" + authUser.id() + " missed");
    }

    @GetMapping("/user/restaurant/{restaurantId}/vote")
    public List<Vote> getAll(@PathVariable int restaurantId) {
        log.info("getAll for restaurant {}", restaurantId);
        return voteRepository.getAll(restaurantId);
    }


    @PutMapping(value = "/user/restaurant/{restaurantId}/vote/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestBody Vote vote, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update {} for restaurant {}", vote, authUser.id());
        assureIdConsistent(vote, id);
        ValidationUtil.checkNotFoundWithId(voteRepository.get(id, authUser.id()),
                "Vote id=" + id + ", restaurant id=" + restaurantId + " doesn't belong to user id=" + authUser.id());
        vote.setRestaurant(restaurantRepository.getOne(restaurantId));
        vote.setUser(userRepository.getOne(authUser.id()));
        voteRepository.save(vote);
    }

    @PostMapping(value = "/user/restaurant/{restaurantId}/vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestBody Vote vote, @PathVariable int restaurantId) {
        log.info("create {} for user {}", vote, authUser.id());
        checkNew(vote);
        vote.setUser(userRepository.getOne(authUser.id()));
        vote.setRestaurant(ValidationUtil.checkNotFoundWithId(restaurantRepository.get(restaurantId, authUser.id()),
                "Restaurant id=" + restaurantId + " doesn't belong to user id=" + authUser.id()));
        Vote created = voteRepository.save(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/user/vote/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
