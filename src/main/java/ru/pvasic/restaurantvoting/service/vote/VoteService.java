package ru.pvasic.restaurantvoting.service.vote;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pvasic.restaurantvoting.model.Vote;
import ru.pvasic.restaurantvoting.repository.vote.VoteRepository;
import ru.pvasic.restaurantvoting.to.VoteTo;

import java.time.LocalDate;
import java.util.Optional;

import static ru.pvasic.restaurantvoting.util.VoteUtil.voteFromTo;
import static ru.pvasic.restaurantvoting.util.validation.ValidationUtil.checkVoteDateTime;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository repository;

    @Transactional
    public Vote update(VoteTo voteTo, LocalDate oldDate, int userId) {
        checkVoteDateTime(voteTo.getId(), oldDate);
        return repository.save(voteFromTo(voteTo, userId));
    }

    @Transactional
    public Vote save(VoteTo voteTo, int userId) {
        Optional<Vote> oOldVote = repository.getByUserId(userId);
        if (oOldVote.isPresent()) {
            Vote oldVote = oOldVote.get();
            checkVoteDateTime(oldVote.getId(), oldVote.getDateTime().toLocalDate());
            repository.delete(oldVote.id());
        }
        return repository.save(voteFromTo(voteTo, userId));
    }

    @Transactional
    public void delete(int id, LocalDate oldDate) {
        checkVoteDateTime(id, oldDate);
        repository.delete(id);
    }
}
