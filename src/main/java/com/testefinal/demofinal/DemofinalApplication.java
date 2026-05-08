package com.testefinal.demofinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DemofinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemofinalApplication.class, args);
	}

}
