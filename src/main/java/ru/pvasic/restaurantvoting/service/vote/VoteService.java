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

import java.time.LocalDateTime;
import java.util.Optional;

import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkPositive;
import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkVoteDateTime;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Vote update(Vote vote, Vote oldVote, int userId) {
        checkVoteDateTime(vote.id(), vote.getDateTime(), oldVote.getDateTime().toLocalDate());
        decrementVoteCount(findRestaurant(oldVote.getRestaurantId()));
        int voteRestaurantId = vote.getRestaurantId();
        incrementVoteCount(findRestaurant(voteRestaurantId));
        vote.setUserId(userId);
        return voteRepository.save(vote);
    }

    @Transactional
    public Vote save(Vote vote, int userId, Integer restaurantId) {
        Optional<Vote> oOldVote = voteRepository.findById(userId);
        if (oOldVote.isEmpty()) {
            Restaurant restaurant = restaurantRepository.checkByRestaurantId(restaurantId);
            incrementVoteCount(restaurant);
            Vote createdVote = new Vote(null, userId, restaurantId, vote.getDateTime());
            return voteRepository.save(createdVote);
        } else {
            throw new IllegalRequestDataException("Vote with userId =" + userId +
                    " already exists, execute PUT method instead POST");
        }
    }

    @Transactional
    public void delete(int id, Vote vote, Restaurant restaurant) {
        checkVoteDateTime(id, LocalDateTime.now(), vote.getDateTime().toLocalDate());
        voteRepository.delete(id);
        decrementVoteCount(restaurant);
    }

    private Restaurant findRestaurant(Integer restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NotFoundException("Restaurant with id=" + restaurantId + " not found"));
    }

    private void decrementVoteCount(Restaurant restaurant) {
        restaurant.setVoteCount(checkPositive(restaurant.getVoteCount()) - 1);
        restaurantRepository.save(restaurant);
    }

    private void incrementVoteCount(Restaurant restaurant) {
        restaurant.setVoteCount(restaurant.getVoteCount() + 1);
        restaurantRepository.save(restaurant);
    }
}
