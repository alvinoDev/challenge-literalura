package com.alvinodev.challenge_literalura;

import com.alvinodev.challenge_literalura.principal.Menu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeLiteraluraApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeLiteraluraApplication.class, args);
	}

    @Override
    public  void run(String... args) throws Exception {
        Menu menu = new Menu();
        menu.displayMenu();
    }
}
