package com.team3.coffee_order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class CoffeeOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeeOrderApplication.class, args);
	}

}
