package ru.pvasic.restaurantvoting.repository.vote;

import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.model.Vote;

import javax.persistence.EntityManager;
import java.util.List;

public interface CustomVoteRepository {
    public EntityManager getEntityManager();

    public List<Vote> getHistoryAll(int userId);
}
