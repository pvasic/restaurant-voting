package ru.pvasic.restaurantvoting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.model.Restaurant;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(int id);

    @Query("SELECT r FROM Restaurant r ORDER BY r.name ASC")
    List<Dish> getAll();

    @Query("SELECT r FROM Restaurant r WHERE r.id=:restaurantId AND r.user.id=:userId")
    Optional<Restaurant> getWithCheck(int restaurantId, int userId);

    //    https://stackoverflow.com/a/46013654/548473
    //TODO fix getWithDishes
    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r WHERE r.id=?1")
    Optional<Restaurant> getWithDishes(int id);

    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r WHERE r.id=?1")
    Optional<Restaurant> getWithUser(int id);
}
