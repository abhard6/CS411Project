RIFT
====
Importing the Project
--------------------- 
This project should be importeted as a maven project on whichever IDE you use. 
     
Instalation
----------
(in the command line on your computer, if it's a mac or linux)

    mysql -h localhost -u root -p
    CREATE USER 'socsenti_rift'@'localhost' IDENTIFIED BY 'cs411';
    CREATE DATABASE socsenti_rift;
    GRANT ALL PRIVILEGES ON socsenti_rift.* TO 'socsenti_rift'@'localhost';

In your IDE, run DemoApplication.java. Or, in the command line run

    mvn clean install # This only needs to be run once
    mvn spring-boot:run # Run this every time you want to start the server
        
