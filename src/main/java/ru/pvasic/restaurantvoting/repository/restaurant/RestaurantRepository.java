package ru.pvasic.restaurantvoting.repository.restaurant;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.pvasic.restaurantvoting.error.IllegalRequestDataException;
import ru.pvasic.restaurantvoting.model.Restaurant;
import ru.pvasic.restaurantvoting.repository.BaseRepository;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r WHERE r.id = :id and r.userId = :userId")
    Optional<Restaurant> get(int id, int userId);

    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r WHERE r.id=?1")
    Optional<Restaurant> getWithDishes(int id);

    default Restaurant checkBelong(int id, int userId) {
        return get(id, userId).orElseThrow(
                () -> new IllegalRequestDataException("Restaurant for id =" + id + " doesn't belong to User id=" + userId));
    }

    default Restaurant checkByRestaurantId(int id) {
        return findById(id).orElseThrow(
                () -> new IllegalRequestDataException("Restaurant for id =" + id + " not found!"));
    }
}
