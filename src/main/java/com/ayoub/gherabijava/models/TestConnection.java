package com.ayoub.gherabijava.models;

import java.sql.*;

public class TestConnection {
    public Statement statement;
    public Statement getStatement() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/projet java", "root", ""
            );
            statement = connection.createStatement();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return statement;
    }
}
