package ru.itmo.socket.server.db;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@UtilityClass
public class DatabaseInitializer {

    public static void init(Connection connection) throws SQLException {
        System.out.println("[Tech] [INFO] начинаем инициализировать БД");
        try (Statement stmt = connection.createStatement()) {
            // Схема s467392 должна уже существовать, пропускаем создание
            
            // Таблица клиентов
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS s467392.users (
                        id SERIAL PRIMARY KEY,
                        login TEXT UNIQUE NOT NULL,
                        password_hash TEXT NOT NULL
                    );
                """);

            // Координаты
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS s467392.coordinates (
                        id SERIAL PRIMARY KEY,
                        x DOUBLE PRECISION NOT NULL CHECK (x > -68),
                        y INTEGER NOT NULL
                    );
                """);

            // Локации
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS s467392.locations (
                        id SERIAL PRIMARY KEY,
                        name TEXT NOT NULL,
                        x INTEGER NOT NULL,
                        y DOUBLE PRECISION NOT NULL,
                        z INTEGER NOT NULL
                    );
                """);

            // Маршруты
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS s467392.routes (
                        id SERIAL PRIMARY KEY,
                        name TEXT NOT NULL,
                        coordinates_id INT NOT NULL REFERENCES s467392.coordinates(id) ON DELETE CASCADE,
                        creation_date TIMESTAMP NOT NULL DEFAULT now(),
                        from_location_id INT REFERENCES s467392.locations(id) ON DELETE SET NULL,
                        to_location_id INT REFERENCES s467392.locations(id) ON DELETE SET NULL,
                        distance INTEGER NOT NULL CHECK (distance > 1),
                        user_id INT REFERENCES s467392.users(id) ON DELETE SET NULL
                    );
                """);

            System.out.println("[Tech] [INFO] проинициализировали БД, фух...");
        }
    }
}
