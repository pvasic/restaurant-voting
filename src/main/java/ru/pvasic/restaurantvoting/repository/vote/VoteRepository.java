package ru.pvasic.restaurantvoting.repository.vote;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.pvasic.restaurantvoting.error.IllegalRequestDataException;
import ru.pvasic.restaurantvoting.model.Vote;
import ru.pvasic.restaurantvoting.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote>, CustomVoteRepository {

    @Query("SELECT v FROM Vote v WHERE v.id = :id AND v.id = :id")
    Optional<Vote> get(int id);

    @Query("SELECT v FROM Vote v WHERE v.restaurantId=:restaurantId ORDER BY v.dateTime ASC")
    List<Vote> getAll(int restaurantId);

    default Vote checkBelong(int userId) {
        return findById(userId).orElseThrow(
                () -> new IllegalRequestDataException("Vote for User id =" + userId + " doesn't exist"));
    }
}
