package ru.pvasic.restaurantvoting.repository.dish;

import ru.pvasic.restaurantvoting.model.Dish;

import javax.persistence.EntityManager;
import java.util.List;

public interface CustomDishRepository {
    public EntityManager getEntityManager();

    public List<Dish> getHistoryAll(int restaurantId);
}
