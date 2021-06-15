package ru.pvasic.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.pvasic.restaurantvoting.model.Dish;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer>, CustomDishRepository{
    @Modifying
    @Transactional
    @Query("DELETE FROM Dish d WHERE d.id=:id AND d.userId=:userId")
    int delete(int id, int userId);

    @Query("SELECT d FROM Dish d WHERE d.id = :id AND d.restaurant.id=:restaurantId AND d.userId = :userId")
    Optional<Dish> getWithCheck(int id, int restaurantId, int userId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId ORDER BY d.name ASC")
    List<Dish> getAll(int restaurantId);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant WHERE d.id = ?1 and d.restaurant.id = ?2")
    Optional<Dish> getWithRestaurant(int id, int restaurantId);
}