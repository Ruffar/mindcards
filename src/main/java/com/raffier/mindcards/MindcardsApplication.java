package com.raffier.mindcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.*;

@SpringBootApplication //Spring will inject here
public class MindcardsApplication {
	public static void main(String[] args) {

		//Create list to store new arguments as args array can't be added to
		List<String> newArgsList = new ArrayList<>();
		Collections.addAll(newArgsList, args);

		//Command line scanner
		Scanner scanner = new Scanner(System.in);

		//Port input
		String port = null;
		//Keep asking for port until the user inputs a valid non-empty port (only has numbers which result in a valid port (0-65535) OR "default")
		while(port == null || !(port.equals("default") || (port.matches("[0-9]+") && Integer.parseInt(port)<=65535))) {
			System.out.println("Enter server port (or use 'default'): ");
			port = scanner.nextLine();
		}
		//If default is chosen, then the port is 8080
		if (port.equals("default")) {
			port = "8080";
		}
		newArgsList.add("--server.port="+port); //Add port to arguments

		//Directory of dynamic content (database and images) input, there is no invalid input
		System.out.println("Enter directory for database and images (or use 'default'): ");
		String dir = scanner.nextLine();
		//If default is chosen, set it to "./MindcardsData/mainData"
		if (dir.equals("default")) {
			dir = "./MindcardsData/mainData";
		}
		newArgsList.add("--dynamicContentDirectory="+dir); //Add directory to arguments

		scanner.close(); //Inputting is finished, close the scanner

		//Convert List into Array as the second parameter of run accepts String[] array and not list
		String[] newArgs = new String[newArgsList.size()];
		newArgsList.toArray(newArgs);

		//Start Spring application
		SpringApplication.run(MindcardsApplication.class, newArgs);

	}
}
