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
import ru.pvasic.restaurantvoting.repository.vote.VoteRepository;
import ru.pvasic.restaurantvoting.service.vote.VoteService;

import java.net.URI;

import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;
import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {
    static final String REST_URL = "/api/rest";

    private final VoteRepository repository;
    private final VoteService service;

    @GetMapping("/user/vote/{id}")
    public ResponseEntity<Vote> get(@PathVariable int id){
        log.info("get vote {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @DeleteMapping("/user/vote/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        int userId = authUser.id();
        log.info("delete {} for user {}", id, userId);
        Vote vote = repository.checkBelong(id, userId);
        service.delete(id, vote);
    }


    @PutMapping(value = "/user/restaurant/{restaurantId}/vote/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestBody Vote vote, @PathVariable int restaurantId, @PathVariable int id) {
        int userId = authUser.id();
        log.info("update {} for restaurant {}", id, userId);
        assureIdConsistent(vote, id);
        Vote voteOld = repository.checkBelong(id, userId);
        service.update(vote, voteOld, userId, restaurantId);
    }

    @PostMapping(value = "/user/restaurant/{restaurantId}/vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestBody Vote vote, @PathVariable int restaurantId) {
        int userId = authUser.id();
        log.info("create {} for user {}", vote, authUser.id());
        checkNew(vote);
        Vote created = service.save(vote, userId, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/user/vote/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
