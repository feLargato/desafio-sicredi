package com.pauta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class PautaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PautaApiApplication.class, args);
	}

}
