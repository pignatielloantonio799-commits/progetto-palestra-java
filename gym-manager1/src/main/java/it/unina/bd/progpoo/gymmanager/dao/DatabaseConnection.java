package it.unina.bd.progpoo.gymmanager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Stringa di connessione (URL) per il database PostgreSQL
	private static final String URL = "jdbc:postgresql://localhost:5432/gymManager";
    // Utente di default di PostgreSQL
    private static final String USER = "postgres";
    // Password che hai scelto durante l'installazione
    private static final String PASSWORD = "Antonio2057";

    public static Connection getConnection() throws SQLException {
        try {
            // Carica il driver JDBC in memoria
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC di PostgreSQL non trovato.");
            throw new SQLException("Driver non trovato", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}