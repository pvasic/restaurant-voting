package ru.pvasic.restaurantvoting.repository.vote;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.model.Vote;
import ru.pvasic.restaurantvoting.repository.dish.CustomDishRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class CustomVoteRepositoryImpl implements CustomVoteRepository {

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Override
    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    // TODO not work for "mvn test"
    @Override
    public List<Vote> getHistoryAll(int userId) {
        AuditReader reader = AuditReaderFactory.get(entityManagerFactory.createEntityManager());
//        List<Vote> revisions = reader.createQuery().forRevisionsOfEntity(
//                Vote.class,
//                true, // false returns an array of entity and audit data
//                true // selects the deleted audit rows
//        ).getResultList();
        return null;
    }
}
