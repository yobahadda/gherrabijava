package com.ayoub.gherabijava.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class testconnection {
    public static void connect(String query) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/projetjava", "root", ""
            );
            Statement statement = connection.createStatement();

            // Print the query being executed
            System.out.println("Executing query: " + query);

            // Check if the query is a SELECT statement
            if (query.toLowerCase().startsWith("select")) {
                ResultSet resultSet = statement.executeQuery(query);
                // Process the result set
                while (resultSet.next()) {
                    // Process each row of the result set
                    System.out.println(resultSet.getString(1));
                }
                resultSet.close();
            } else {
                // For INSERT, UPDATE, DELETE, etc.
                int rowsAffected = statement.executeUpdate(query);
                System.out.println("Rows affected: " + rowsAffected);
            }

            statement.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void main(String[] args) {
        connect("SELECT * FROM clients");
    }
}
