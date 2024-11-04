package com.example.soap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL="jdbc:mysql://localhost:3306/soap";
    private static final String USERNAME="root";
    private static final String PASSWORD="SQL22++";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
