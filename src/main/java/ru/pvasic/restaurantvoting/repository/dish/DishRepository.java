package ru.pvasic.restaurantvoting.repository.dish;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.pvasic.restaurantvoting.error.IllegalRequestDataException;
import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.id = :id AND d.userId = :userId")
    Optional<Dish> get(int id, int userId);

    @Query("SELECT d FROM Dish d WHERE d.restaurantId=:restaurantId ORDER BY d.date DESC")
    List<Dish> getAll(int restaurantId);

    default Dish checkBelong(int id, int userId) {
        return get(id, userId).orElseThrow(
                () -> new IllegalRequestDataException("Dish id=" + id + " doesn't belong to user id=" + userId));
    }
}
