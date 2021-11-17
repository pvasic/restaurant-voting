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
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;
import ru.pvasic.restaurantvoting.repository.vote.VoteRepository;
import ru.pvasic.restaurantvoting.service.vote.VoteService;

import java.net.URI;

import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;
import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = VoteController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {
    static final String URL = "/api/user/votes";

    private final VoteRepository repository;
    private final VoteService service;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Vote> get(@PathVariable int id) {
        log.info("get vote {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        int userId = authUser.id();
        log.info("delete {} for user {}", id, userId);
        Vote vote = repository.checkBelong(id, userId);
        service.delete(id, vote);
    }


    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestBody Vote vote, @PathVariable int id) {
        int userId = authUser.id();
        log.info("update vote {} for user {}", id, userId);
        assureIdConsistent(vote, id);
        Vote voteOld = repository.checkBelong(id, userId);
        service.update(vote, voteOld);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestBody Vote vote) {
        int userId = authUser.id();
        log.info("create vote {} for user {}", vote, userId);
        checkNew(vote);
        restaurantRepository.checkByRestaurantId(vote.getRestaurantId());
        Vote created = service.save(vote, userId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
