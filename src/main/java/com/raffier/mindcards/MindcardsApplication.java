package com.raffier.mindcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import com.raffier.mindcards.data.AppDatabase;

@SpringBootApplication
public class MindcardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindcardsApplication.class, args);
		//AppDatabase testDatabase = new AppDatabase("testDatabase");
	}

}
