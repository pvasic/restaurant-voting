package ru.pvasic.restaurantvoting.service.vote;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pvasic.restaurantvoting.error.IllegalRequestDataException;
import ru.pvasic.restaurantvoting.error.NotFoundException;
import ru.pvasic.restaurantvoting.model.Restaurant;
import ru.pvasic.restaurantvoting.model.Vote;
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;
import ru.pvasic.restaurantvoting.repository.vote.VoteRepository;

import java.util.Optional;

import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkPositive;
import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkVoteTime;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Vote update(Vote vote, Vote voleOld, int userId, int restaurantId) {
        checkVoteTime(vote);
        decrementVoteCount(voleOld.getRestaurantId());
        incrementVoteCount(restaurantId);
        Vote voteUpdated = new Vote(voleOld.getId(), userId, restaurantId, vote.getDateTime());
        return voteRepository.save(voteUpdated);
    }

    @Transactional
    public Vote save(Vote vote, int userId, int restaurantId) {
        Optional<Vote> oVoteOld = voteRepository.get(userId);
        if (oVoteOld.isEmpty()) {
            incrementVoteCount(restaurantId);
            Vote createdVote = new Vote(null, userId, restaurantId, vote.getDateTime());
            return voteRepository.save(createdVote);
        } else {
            throw new IllegalRequestDataException("Vote with userId =" + userId + " already exists");
        }
    }

    @Transactional
    public void delete(int id, Vote vote) {
        checkVoteTime(vote);
        Integer restaurantId = vote.getRestaurantId();
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NotFoundException("Restaurant with id=" + restaurantId + " not found"));
        voteRepository.delete(id);
        decrementVoteCount(vote.getRestaurantId());
        restaurantRepository.save(restaurant);

    }

    private void decrementVoteCount(int restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
        restaurant.setVoteCount(checkPositive(restaurant.getVoteCount()) - 1);
        restaurantRepository.save(restaurant);
    }

    private void incrementVoteCount(int restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
        restaurant.setVoteCount(restaurant.getVoteCount() + 1);
        restaurantRepository.save(restaurant);
    }
}
