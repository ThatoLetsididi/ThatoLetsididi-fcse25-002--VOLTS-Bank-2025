package com.VOLTSBankApp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/volts_bank_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        if (conn == null) {
            throw new SQLException("failed to connect to database");
        }
        return conn;

    }

    public static boolean testConnection() {

        return true;
    }

    public static void closeConnection() {

    }
}
