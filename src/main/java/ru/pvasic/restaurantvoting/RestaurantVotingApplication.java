package ru.pvasic.restaurantvoting;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.pvasic.restaurantvoting.model.Dish;
import ru.pvasic.restaurantvoting.repository.DishRepository;
import ru.pvasic.restaurantvoting.repository.RestaurantRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@SpringBootApplication
@AllArgsConstructor
public class RestaurantVotingApplication implements ApplicationRunner {

	private final RestaurantRepository restaurantRepository;

	private final DishRepository dishRepository;

	private final EntityManager entityManager;

	public static void main(String[] args) {
		SpringApplication.run(RestaurantVotingApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
//		System.out.println(restaurantRepository.getWithDishes(1));
		System.out.println(dishRepository.getAll(2));
		Dish dishUpdated = new Dish(1, "Суп", 500, LocalDateTime.now());
		dishUpdated.setRestaurant(restaurantRepository.getById(1));
		dishRepository.save(dishUpdated);

		Dish dishUpdated2 = new Dish(1, "Плов", 757587, LocalDateTime.now());
		dishUpdated.setRestaurant(restaurantRepository.getById(1));
		dishRepository.save(dishUpdated);

		System.out.println(dishRepository.findById(1).get());

//		Dish dish = dishRepository.findById(1).orElseThrow(IllegalArgumentException::new);
//		dishRepository.findRevision(1,1L).forEach(System.out::println);
	}
}
