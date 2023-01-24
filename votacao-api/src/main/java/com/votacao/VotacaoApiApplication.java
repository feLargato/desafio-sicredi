package com.votacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VotacaoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotacaoApiApplication.class, args);
	}

}
