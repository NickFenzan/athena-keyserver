package com.millervein.athena.keyserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KeyserverApplication implements CommandLineRunner {
	@Autowired TokenService tokenService;

	public static void main(String[] args) {
		SpringApplication.run(KeyserverApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		tokenService.refreshToken();
	}
}
