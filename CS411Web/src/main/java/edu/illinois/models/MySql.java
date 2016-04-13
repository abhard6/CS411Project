package edu.illinois.models;

import com.mysql.jdbc.MySQLConnection;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Created by nprince on 4/12/16.
 */
// Singleton
public class MySql {
    public static MySql instance = null;
    public Connection connection;

    public static MySql getInstance() {
        if(instance == null) {
            instance = new MySql();
        }
        return instance;
    }

    public MySql() {
        this.connection = new MySQLConnect().connect();
    }

    public ResultSet executeQuery(String query) {
        System.out.println("EXECUTING: " + query);
        try {
            return connection.prepareStatement(query).executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void executeUpdate(String query) {
        System.out.println("EXECUTING: " + query);
        try {
            connection.prepareStatement(query).executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
