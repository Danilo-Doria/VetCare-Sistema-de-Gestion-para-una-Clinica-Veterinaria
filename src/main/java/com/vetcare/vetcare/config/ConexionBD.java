package com.vetcare.vetcare.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:postgresql://localhost:5434/vetcare";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres_123";

    // Constructor privado para evitar instanciación externa
    private ConexionBD() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
