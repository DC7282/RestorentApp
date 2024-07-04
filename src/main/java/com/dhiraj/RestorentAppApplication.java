package com.dhiraj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RestorentAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestorentAppApplication.class, args);
	}

}
