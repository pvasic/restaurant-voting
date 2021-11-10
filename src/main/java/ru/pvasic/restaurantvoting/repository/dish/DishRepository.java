package ru.pvasic.restaurantvoting.repository.dish;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.pvasic.restaurantvoting.error.IllegalRequestDataException;
import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish>, CustomDishRepository {

    @Query("SELECT d FROM Dish d WHERE d.id = :id AND d.restaurant.id = :restaurantId")
    Optional<Dish> get(int id, int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId ORDER BY d.dateTime DESC")
    List<Dish> getAll(int restaurantId);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant WHERE d.id = ?1 and d.restaurant.id = ?2")
    Optional<Dish> getWithRestaurant(int id, int restaurantId);

    default Dish checkBelong(int id, int restaurantId) {
        return get(id, restaurantId).orElseThrow(
                () -> new IllegalRequestDataException("Dish id=" + id + " doesn't belong to restaurant id=" + restaurantId));
    }
}