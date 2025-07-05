package ru.itmo.socket.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:postgresql://localhost:7777/studs?currentSchema=s467392";
    private static final String USER = "s467392";
    private static final String PASSWORD = "GShVHg75pkng0eKr";

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        // Устанавливаем схему по умолчанию для сессии
        connection.createStatement().execute("SET search_path TO s467392, public");
        return connection;
    }

}
