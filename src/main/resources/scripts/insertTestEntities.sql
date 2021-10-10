INSERT OR REPLACE INTO User VALUES (1,'admin','admin1234!','admin@mindcards.com',1,0);
INSERT OR REPLACE INTO User VALUES (2,'dave','password','dave@somemail.com',0,1);

INSERT INTO Image (name,imagePath) VALUES ("monkey","/images/monkey.png");
INSERT INTO Image (name,imagePath) VALUES ("dog","https://th.bing.com/th/id/R.44696ab5ce4f5169042dfa03cdcf1d2d?rik=62fOQDK6OpzmoQ&riu=http%3a%2f%2fimages4.fanpop.com%2fimage%2fphotos%2f23700000%2fFunny-random-23797915-1000-981.jpg&ehk=IZ97ZuH3cXlcNGM583D4z3sPQm43pNt0Z4SrHaBg8F4%3d&risl=&pid=ImgRaw&r=0");

INSERT INTO CardPack (ownerId, title, imageId, description) VALUES (2,"First Pack Ever",2,"Testing packs and stuff");

INSERT INTO Mindcard (packId, title, imageId, description) VALUES (1,'TestCard123',1,'This is the first mindcard!');
INSERT INTO Mindcard (packId, title, imageId, description) VALUES (1,'This Card is Cool',NULL,"It's the coolest card yet!");
INSERT INTO Mindcard (packId, title, imageId, description) VALUES (1,'Bad card',NULL,"Very very bad card...");

INSERT INTO Infocard (mindcardId, imageId, description) VALUES (2,2,"This card's very cool!");
INSERT INTO Infocard (mindcardId, imageId, description) VALUES (3,NULL,"Bad cards don't deserve to be read");
INSERT INTO Infocard (mindcardId, imageId, description) VALUES (3,1,"This card was made with bad intentions...");