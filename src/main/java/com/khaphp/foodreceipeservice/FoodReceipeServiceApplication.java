package com.khaphp.foodreceipeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FoodReceipeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodReceipeServiceApplication.class, args);
	}

}
