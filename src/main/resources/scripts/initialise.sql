CREATE TABLE IF NOT EXISTS Mindcard (mindcardId INTEGER PRIMARY KEY AUTOINCREMENT, packId INTEGER NOT NULL, title TEXT,imageId INTEGER,
description TEXT, FOREIGN KEY (packId) references CardPack(packId), FOREIGN KEY (imageId) references Image(imageId) );

CREATE TABLE IF NOT EXISTS Infocard (infocardId INTEGER PRIMARY KEY AUTOINCREMENT, mindcardId INTEGER NOT NULL, imageId INTEGER,
description TEXT, FOREIGN KEY (mindcardId) references Mindcard(mindcardId), FOREIGN KEY (imageId) references Image(imageId) );

CREATE TABLE IF NOT EXISTS CardGroup (cardGroupId INTEGER PRIMARY KEY AUTOINCREMENT, packId INTEGER NOT NULL, title TEXT,
imageId INTEGER, description TEXT, FOREIGN KEY (packId) references CardPack(packId), FOREIGN KEY (imageId) references Image(imageId) );

CREATE TABLE IF NOT EXISTS GroupMindcard (cardGroupId INTEGER NOT NULL, mindcardId INTEGER NOT NULL, PRIMARY KEY (cardGroupId,mindcardId),
FOREIGN KEY (cardGroupId) references CardGroup(cardGroupId), FOREIGN KEY (mindcardId) references Mindcard(mindcardId) );

CREATE TABLE IF NOT EXISTS Deck (deck INTEGER PRIMARY KEY AUTOINCREMENT, ownerId INTEGER, title TEXT, imageId INTEGER,
description TEXT, FOREIGN KEY (ownerId) references User(userId), FOREIGN KEY (imageId) references Image(imageId) );

CREATE TABLE IF NOT EXISTS Image (imageId INTEGER PRIMARY KEY AUTOINCREMENT, imagePath TEXT );

CREATE TABLE IF NOT EXISTS User (userId INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT,
email TEXT NOT NULL, isDeveloper INTEGER Default 0, studyHelp INTEGER Default 1);

INSERT OR REPLACE INTO User VALUES (0,'DeletedUser','deletedpassword1234','deleted@mindcards.com',0,0);
INSERT OR REPLACE INTO Image VALUES (0,'');
INSERT OR REPLACE INTO Deck VALUES (0,0,'DeletedPack',0,'Pack does not exist anymore.';
INSERT OR REPLACE INTO Mindcard VALUES (0,0,'DeletedCard',0,'Card does not exist anymore.');
INSERT OR REPLACE INTO Infocard VALUES (0,0,0,'Card does not exist anymore.');
INSERT OR REPLACE INTO CardGroup VALUES (0,0,'DeletedGroup',0,'Group does not exist anymore.');
INSERT OR REPLACE INTO Tag VALUES (0,'DeletedTag');