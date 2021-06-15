package ru.pvasic.restaurantvoting;

import lombok.AllArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.repository.DishRepository;
import ru.pvasic.restaurantvoting.repository.RestaurantRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class RestaurantVotingApplication implements ApplicationRunner {

	private final RestaurantRepository restaurantRepository;

	private final DishRepository dishRepository;

	public static void main(String[] args) {
		SpringApplication.run(RestaurantVotingApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
//		System.out.println(restaurantRepository.getWithDishes(1));
		System.out.println(dishRepository.getAll(2));
		Dish dishUpdated = new Dish(1, "Суп", 500, LocalDateTime.of(2030, 1,1,1,1), 3);
		dishUpdated.setRestaurant(restaurantRepository.getOne(1));
		dishRepository.save(dishUpdated);

		Dish dishUpdated2 = new Dish(1, "Плов", 757587, LocalDateTime.of(2050, 5,5,5,5), 3);
		dishUpdated2.setRestaurant(restaurantRepository.getOne(1));
		dishRepository.save(dishUpdated2);

		System.out.println(dishRepository.findById(1).get());

		EntityManager em = dishRepository.getEntityManager();
		AuditReader reader = AuditReaderFactory.get(em);
		List<Dish> revisions = reader.createQuery().forRevisionsOfEntity(
				Dish.class,
				true, // false returns an array of entity and audit data
				true // selects the deleted audit rows
		).getResultList();
		revisions.forEach(System.out::println);
	}
}
