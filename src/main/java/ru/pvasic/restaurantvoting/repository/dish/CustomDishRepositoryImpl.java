package ru.pvasic.restaurantvoting.repository.dish;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pvasic.restaurantvoting.model.Dish;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class CustomDishRepositoryImpl implements CustomDishRepository {

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Override
    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    // TODO not work for "mvn test"
    @Override
    public List<Dish> getHistoryAll(int restaurantId) {
        AuditReader reader = AuditReaderFactory.get(entityManagerFactory.createEntityManager());
        List<Dish> revisions = reader.createQuery().forRevisionsOfEntity(
                Dish.class,
                true, // false returns an array of entity and audit data
                true // selects the deleted audit rows
        ).getResultList();
        return revisions;
    }
}
