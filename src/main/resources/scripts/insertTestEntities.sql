INSERT OR REPLACE INTO User VALUES (1,'admin','IBgsrTK5HXsfkqQgF5G17HRnMlTbS9uF7petJQnUMug=','admin@mindcards.com',1);
INSERT OR REPLACE INTO User VALUES (2,'dave','rOf6oXswOr2P91kRJM55hdSpz3Z2qBTkxKIQm4jjX3M=','dave@somemail.com',0);

INSERT INTO Image (imagePath) VALUES ("https://th.bing.com/th/id/R.44696ab5ce4f5169042dfa03cdcf1d2d?rik=62fOQDK6OpzmoQ&riu=http%3a%2f%2fimages4.fanpop.com%2fimage%2fphotos%2f23700000%2fFunny-random-23797915-1000-981.jpg&ehk=IZ97ZuH3cXlcNGM583D4z3sPQm43pNt0Z4SrHaBg8F4%3d&risl=&pid=ImgRaw&r=0");

INSERT INTO Deck (ownerId, title, imageId, description, isPrivate, timeCreated) VALUES (2,"First Deck Ever",1,"Testing decks and stuff",0,0);

INSERT INTO Mindcard (deckId, title, imageId, description) VALUES (1,'TestCard123',1,'This is the first mindcard!');
INSERT INTO Mindcard (deckId, title, imageId, description) VALUES (1,'This Card is Cool',NULL,"It's the coolest card yet!");
INSERT INTO Mindcard (deckId, title, imageId, description) VALUES (1,'Bad card',NULL,"Very very bad card...");

INSERT INTO Infocard (mindcardId, imageId, description) VALUES (2,1,"This card's very cool!");
INSERT INTO Infocard (mindcardId, imageId, description) VALUES (3,NULL,"Bad cards don't deserve to be read");
INSERT INTO Infocard (mindcardId, imageId, description) VALUES (3,1,"This card was made with bad intentions...");