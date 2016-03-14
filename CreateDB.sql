CREATE DATABASE IF NOT EXISTS socsenti_rift;
CONNECT socsenti_rift;

CREATE TABLE IF NOT EXISTS `Post` (
       `id` INT AUTO_INCREMENT PRIMARY KEY,
       `timestamp` DATETIME NOT NULL,
       `content` VARCHAR(300) NOT NULL,
       `sentiment` INT NOT NULL,
       `latitude` FLOAT,
       `longitude` FLOAT,
       `source` VARCHAR(10)
);     

CREATE TABLE IF NOT EXISTS `Bound` (
       `id` INT AUTO_INCREMENT PRIMARY KEY,
       `topLeft` FLOAT,
       `topRight` FLOAT,
       `bottomLeft` FLOAT,
       `bottomRight` FLOAT
);

CREATE TABLE IF NOT EXISTS `Query` (
       `id` INT AUTO_INCREMENT PRIMARY KEY,
       `source` VARCHAR(10),
       `timestamp` DATETIME NOT NULL,
       `boundId` INT,
       FOREIGN KEY(`boundId`) REFERENCES Bound(`id`)
);

CREATE TABLE IF NOT EXISTS `Users` (
       `id` INT AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS `UserQuery` (
       `id` INT AUTO_INCREMENT PRIMARY KEY,  
       `timestamp` DATETIME NOT NULL,
       `userId` INT,
       `queryId` INT,
       FOREIGN KEY(`userId`) REFERENCES Users(id),
       FOREIGN KEY(`queryId`) REFERENCES Query(id)
);

CREATE TABLE IF NOT EXISTS `SpannedSentimentQuery` (
       `id` INT AUTO_INCREMENT PRIMARY KEY,
       `keywords` VARCHAR(50),
       `increment` VARCHAR(10)
);
              
              
