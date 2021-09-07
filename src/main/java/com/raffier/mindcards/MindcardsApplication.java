package com.raffier.mindcards;

import com.raffier.mindcards.data.table.Image;
import com.raffier.mindcards.data.table.Infocard;
import com.raffier.mindcards.data.table.Mindcard;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.raffier.mindcards.data.AppDatabase;

@SpringBootApplication
public class MindcardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindcardsApplication.class, args);
		System.out.println("A new pepepeppepe has been created.");

		AppSettings.database = new AppDatabase("testDatabase");

		Mindcard testCard1 = Mindcard.addMindcard(AppSettings.database,0,"TestCard123");
		testCard1.updateDescription("This is the first mindcard!");
		Image image1 = Image.addImage(AppSettings.database, "/images/monkey.png");
		testCard1.updateImage(1);

		Mindcard testCard2 = Mindcard.addMindcard(AppSettings.database,1,"ThisCardIsCool");
		testCard2.updateDescription("It's the coolest card yet!");
		Infocard infocard1 = Infocard.addInfocard(AppSettings.database,2);
		infocard1.updateDescription("This cool card is very cool!");
		Image image2 = Image.addImage(AppSettings.database, "https://th.bing.com/th/id/R.44696ab5ce4f5169042dfa03cdcf1d2d?rik=62fOQDK6OpzmoQ&riu=http%3a%2f%2fimages4.fanpop.com%2fimage%2fphotos%2f23700000%2fFunny-random-23797915-1000-981.jpg&ehk=IZ97ZuH3cXlcNGM583D4z3sPQm43pNt0Z4SrHaBg8F4%3d&risl=&pid=ImgRaw&r=0");
		infocard1.updateImage(2);

		Mindcard testCard3 = Mindcard.addMindcard(AppSettings.database,4,"badCard");
		testCard3.updateDescription("Very very bad card...");
		Infocard infocard2 = Infocard.addInfocard(AppSettings.database,3);
		infocard2.updateDescription("Bad cards don't deserve to be read");
		Infocard infocard3 = Infocard.addInfocard(AppSettings.database,3);
		infocard3.updateDescription("This bad card was made with bad intentions");
		infocard3.updateImage(1);

	}

}
