package com.carbonaro.ReactiveSimplifiedPicPay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
@EnableReactiveMongoRepositories
public class ReactiveSimplifiedPicPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveSimplifiedPicPayApplication.class, args);
	}

}
