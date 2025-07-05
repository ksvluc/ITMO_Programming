package ru.itmo.socket.server.db.dao;

import ru.itmo.socket.common.entity.Coordinates;
import ru.itmo.socket.common.entity.Location;
import ru.itmo.socket.common.entity.Route;
import ru.itmo.socket.server.concurrent.DbUserContext;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Coordinates entity (now with id field)
 */
public class CoordinatesDao {
    /**
     * Inserts coordinates, sets generated ID, and returns the updated entity
     */
    public Coordinates insert(Coordinates coordinates) {
        String sql = "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id";
        try (PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql)) {
            st.setDouble(1, coordinates.getX());
            st.setInt(2, coordinates.getY());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                return coordinates;
            } else {
                throw new SQLException("Coordinates insert failed: " + coordinates);
            }
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public Coordinates findById(long id) {
        return findById(DbUserContext.getConnection(), id);
    }

    public Coordinates findById(Connection conn, long id) {
        String sql = "SELECT x, y FROM coordinates WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) return null;
            Coordinates coords = new Coordinates();
            coords.setX(rs.getDouble("x"));
            coords.setY(rs.getInt("y"));
            return coords;
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public void update(long id, Coordinates coordinates) {
        String sql = "UPDATE coordinates SET x = ?, y = ? WHERE id = ?";
        try (PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql)) {
            st.setDouble(1, coordinates.getX());
            st.setInt(2, coordinates.getY());
            st.setLong(3, id);
            if (st.executeUpdate() == 0) {
                throw new SQLException("Coordinates update failed, id=" + id);
            }
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }
}



