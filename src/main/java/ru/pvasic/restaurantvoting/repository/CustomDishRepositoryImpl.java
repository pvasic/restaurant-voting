package ru.pvasic.restaurantvoting.repository;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class CustomDishRepositoryImpl implements CustomDishRepository{
    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Override
    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
