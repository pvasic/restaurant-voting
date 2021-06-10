package ru.pvasic.restaurantvoting;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.pvasic.restaurantvoting.repository.DishRepository;
import ru.pvasic.restaurantvoting.repository.RestaurantRepository;

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
//		System.out.println(restaurantRepository.getWithMeals(1));
		System.out.println(dishRepository.getAll(2));
	}
}
