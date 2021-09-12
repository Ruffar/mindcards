package com.raffier.mindcards;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.raffier.mindcards.model.AppDatabase;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class MindcardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindcardsApplication.class, args);

		AppConfig config;

		//AppConfig.getConfig().database = new AppDatabase("testDatabase");

	}

}
