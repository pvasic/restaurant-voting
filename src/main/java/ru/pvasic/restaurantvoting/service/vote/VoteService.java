package ru.pvasic.restaurantvoting.service.vote;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pvasic.restaurantvoting.model.Vote;
import ru.pvasic.restaurantvoting.repository.vote.VoteRepository;
import ru.pvasic.restaurantvoting.to.VoteTo;

import java.time.LocalDate;
import java.util.Optional;

import static ru.pvasic.restaurantvoting.util.VoteUtil.createNewFromTo;
import static ru.pvasic.restaurantvoting.util.VoteUtil.createUpdateFromTo;
import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkVoteDateTime;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;

    @Transactional
    public Vote update(VoteTo voteTo, LocalDate oldDate, int userId) {
        checkVoteDateTime(voteTo.getId(), oldDate);
        Vote voteUpdated = createUpdateFromTo(voteTo, userId);
        return voteRepository.save(voteUpdated);
    }

    @Transactional
    public Vote save(VoteTo voteTo, Optional<Vote> oOldVote, int userId) {
        if (oOldVote.isPresent()) {
            Vote oldVote = oOldVote.get();
            checkVoteDateTime(oldVote.getId(), oldVote.getDateTime().toLocalDate());
            voteTo.setId(oldVote.getId());
            return voteRepository.save(createUpdateFromTo(voteTo, userId));
        } else {
            return voteRepository.save(createNewFromTo(voteTo, userId));
        }
    }

    @Transactional
    public void delete(int id, LocalDate oldDate) {
        checkVoteDateTime(id, oldDate);
        voteRepository.delete(id);
    }
}
