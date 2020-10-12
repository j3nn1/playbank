package fi.jenniriikonen.playbank;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fi.jenniriikonen.playbank.domain.User;
import fi.jenniriikonen.playbank.domain.Category;
import fi.jenniriikonen.playbank.domain.CategoryRepository;
import fi.jenniriikonen.playbank.domain.Play;
import fi.jenniriikonen.playbank.domain.PlayRepository;
import fi.jenniriikonen.playbank.domain.UserRepository;

@SpringBootApplication
public class PlaybankApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaybankApplication.class, args);
	}
	//Adding some demo data to database
	@Bean
	public CommandLineRunner demo(PlayRepository prepository, CategoryRepository crepository, UserRepository urepository) {
	return (args) -> {
		
		//Demo users (user/user, admin/admin)
		User user1 = new User("user", "$2y$10$D6pdyMRziV8mqtwPWn8FSez/o1KtvFYHIADoEMZlQoxVvo4ObMBaa", "USER");
		User user2 = new User("admin", "$2y$10$PHwjzsWRFM7cFn0x6131geYiawHD9nujwT1UuWyS7Um5kBNRH0GTK", "ADMIN");
		urepository.save(user1);
		urepository.save(user2);
		
		crepository.save(new Category("exercise"));
		crepository.save(new Category("brains"));

		

		prepository.save(new Play("Arvaa mitä piirrän?", 
				"Piirretään toisen selkään kuvioita", 
				2, 
				3, 
				crepository.findByName("brains").get(0)));

		
		prepository.save(new Play("Aku-Ankka-hippa", 
				"Pelastuu sanomalla ankkalinnan asukkaan nimen", 
				3, 
				7, 
				crepository.findByName("exercise").get(0)));
		

		};
	};
}
