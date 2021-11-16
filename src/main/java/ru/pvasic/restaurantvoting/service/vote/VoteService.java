package ru.pvasic.restaurantvoting.service.vote;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pvasic.restaurantvoting.error.IllegalRequestDataException;
import ru.pvasic.restaurantvoting.model.Vote;
import ru.pvasic.restaurantvoting.repository.restaurant.RestaurantRepository;
import ru.pvasic.restaurantvoting.repository.vote.VoteRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkVoteDateTime;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;

    @Transactional
    public Vote update(Vote vote, Vote oldVote) {
        checkVoteDateTime(vote.id(), vote.getDateTime(), oldVote.getDateTime().toLocalDate());
        vote.setUserId(oldVote.getUserId());
        return voteRepository.save(vote);
    }

    @Transactional
    public Vote save(Vote vote, int userId) {
        Optional<Vote> oOldVote = voteRepository.findById(userId);
        if (oOldVote.isEmpty()) {
            vote.setUserId(userId);
            return voteRepository.save(vote);
        } else {
            throw new IllegalRequestDataException("Vote with userId =" + userId +
                    " already exists, execute PUT method instead POST");
        }
    }

    @Transactional
    public void delete(int id, Vote vote) {
        checkVoteDateTime(id, LocalDateTime.now(), vote.getDateTime().toLocalDate());
        voteRepository.delete(id);
    }
}
