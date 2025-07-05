package ru.itmo.socket.server.db.dao;

import ru.itmo.socket.common.entity.Coordinates;
import ru.itmo.socket.common.entity.Route;
import ru.itmo.socket.common.entity.Location;
import ru.itmo.socket.server.concurrent.DbUserContext;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Route entity
 */
public class RouteDao {
    private final CoordinatesDao coordinatesDao = new CoordinatesDao();
    private final LocationDao locationDao = new LocationDao();

    /**
     * Inserts a new route using helper methods to get ids for Coordinates and Location
     */
    public Route insert(Route route, int userId) {
        try {
            // save related entities
            Coordinates savedCoords = coordinatesDao.insert(route.getCoordinates());
            Location savedFrom = route.getFrom() != null ? locationDao.insert(route.getFrom()) : null;
            Location savedTo   = route.getTo()   != null ? locationDao.insert(route.getTo())   : null;

            String sql = """
                INSERT INTO routes (name, coordinates_id, creation_date, from_location_id, to_location_id, distance, user_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING id
            """;
            try (PreparedStatement stmt = DbUserContext.getConnection().prepareStatement(sql)) {
                stmt.setString(1, route.getName());
                stmt.setInt(2, getIdFromSavedCoordinates(savedCoords));
                stmt.setTimestamp(3, Timestamp.valueOf(route.getCreationDate()));
                if (savedFrom != null) stmt.setInt(4, getIdFromSavedLocation(savedFrom)); else stmt.setNull(4, Types.INTEGER);
                if (savedTo   != null) stmt.setInt(5, getIdFromSavedLocation(savedTo));     else stmt.setNull(5, Types.INTEGER);
                stmt.setInt(6, route.getDistance());
                stmt.setInt(7, userId);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    route.setId(rs.getLong("id"));
                    return route;
                } else {
                    throw new SQLException("Route insert failed");
                }
            }
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    /**
     * Updates an existing route and its related entities, returns success flag
     */
    public boolean update(Route route, int userId) {
        try {
            // update coordinates
            int coordId = getIdFromSavedCoordinates(route.getCoordinates());
            coordinatesDao.update(coordId, route.getCoordinates());

            // update from/to locations
            if (route.getFrom() != null) {
                int fromId = getIdFromSavedLocation(route.getFrom());
                locationDao.update(fromId, route.getFrom());
            }
            if (route.getTo() != null) {
                int toId = getIdFromSavedLocation(route.getTo());
                locationDao.update(toId, route.getTo());
            }

            String sql = """
                UPDATE routes SET name = ?, distance = ? WHERE id = ? AND user_id = ?
            """;
            try (PreparedStatement stmt = DbUserContext.getConnection().prepareStatement(sql)) {
                stmt.setString(1, route.getName());
                stmt.setInt(2, route.getDistance());
                stmt.setLong(3, route.getId());
                stmt.setInt(4, userId);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public Route findById(long id) {
        return findById(DbUserContext.getConnection(), id);
    }

    public Route findById(Connection connection, long id) {
        String sql = "SELECT name, coordinates_id, creation_date, from_location_id, to_location_id, distance FROM routes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return null;

            Route route = new Route();
            route.setId(id);
            route.setName(rs.getString("name"));
            route.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
            route.setCoordinates(coordinatesDao.findById(connection, rs.getInt("coordinates_id")));

            int fromId = rs.getInt("from_location_id");
            if (!rs.wasNull()) route.setFrom(locationDao.findById(connection, fromId));
            int toId = rs.getInt("to_location_id");
            if (!rs.wasNull()) route.setTo(locationDao.findById(connection, toId));

            route.setDistance(rs.getInt("distance"));
            return route;
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public List<Route> findAllByUserId(Connection connection, int userId) {
        String sql = "SELECT id FROM routes WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            List<Route> result = new ArrayList<>();
            while (rs.next()) {
                Route r = findById(connection, rs.getLong("id"));
                if (r != null) result.add(r);
            }
            return result;
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public boolean remove(long id) {
        String sql = "DELETE FROM routes WHERE id = ?";
        try (PreparedStatement stmt = DbUserContext.getConnection().prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    // Helper methods to retrieve generated IDs for entities without id fields
    private int getIdFromSavedCoordinates(Coordinates coordinates) throws SQLException {
        String sql = "SELECT id FROM coordinates WHERE x = ? AND y = ?";
        try (PreparedStatement stmt = DbUserContext.getConnection().prepareStatement(sql)) {
            stmt.setDouble(1, coordinates.getX());
            stmt.setInt(2, coordinates.getY());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
            throw new SQLException("Can't find coordinates ID");
        }
    }

    private int getIdFromSavedLocation(Location location) throws SQLException {
        String sql = "SELECT id FROM locations WHERE name = ? AND x = ? AND y = ? AND z = ?";
        try (PreparedStatement stmt = DbUserContext.getConnection().prepareStatement(sql)) {
            stmt.setString(1, location.getName());
            stmt.setInt(2, location.getX());
            stmt.setDouble(3, location.getY());
            stmt.setInt(4, location.getZ());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
            throw new SQLException("Can't find location ID");
        }
    }
}