package ru.itmo.socket.server.db.dao;

import ru.itmo.socket.common.entity.Location;
import ru.itmo.socket.server.concurrent.DbUserContext;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO for Location entity (now with id field)
 */
public class LocationDao {
    /**
     * Inserts location, sets generated ID, and returns the updated entity
     */
    public Location insert(Location loc) {
        String sql = "INSERT INTO locations (name, x, y, z) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql)) {
            st.setString(1, loc.getName());
            st.setInt(2, loc.getX());
            st.setDouble(3, loc.getY());
            st.setInt(4, loc.getZ());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                return loc;
            } else {
                throw new SQLException("Location insert failed: " + loc);
            }
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public Location findById(long id) {
        return findById(DbUserContext.getConnection(), id);
    }

    public Location findById(Connection conn, long id) {
        String sql = "SELECT name, x, y, z FROM locations WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) return null;
            Location loc = new Location();
            loc.setName(rs.getString("name"));
            loc.setX(rs.getInt("x"));
            loc.setY(rs.getDouble("y"));
            loc.setZ(rs.getInt("z"));
            return loc;
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public void update(long id, Location loc) {
        String sql = "UPDATE locations SET name = ?, x = ?, y = ?, z = ? WHERE id = ?";
        try (PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql)) {
            st.setString(1, loc.getName());
            st.setInt(2, loc.getX());
            st.setDouble(3, loc.getY());
            st.setInt(4, loc.getZ());
            st.setLong(5, id);
            if (st.executeUpdate() == 0) {
                throw new SQLException("Location update failed, id=" + id);
            }
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }
}
