package ru.pvasic.restaurantvoting.repository;

import javax.persistence.EntityManager;

public interface CustomDishRepository {
    public EntityManager getEntityManager();
}
