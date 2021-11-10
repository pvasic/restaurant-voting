package ru.pvasic.restaurantvoting.repository.vote;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.pvasic.restaurantvoting.error.IllegalRequestDataException;
import ru.pvasic.restaurantvoting.model.Vote;
import ru.pvasic.restaurantvoting.repository.BaseRepository;

import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote>, CustomVoteRepository {

    @Query("SELECT v FROM Vote v WHERE v.id = :id and v.userId = :userId")
    Optional<Vote> get(int id, int userId);

    default Vote checkBelong(int id, int userId) {
        return get(id, userId).orElseThrow(
                () -> new IllegalRequestDataException("Vote id =" + id + " doesn't belong to User id=" + userId));
    }
}
