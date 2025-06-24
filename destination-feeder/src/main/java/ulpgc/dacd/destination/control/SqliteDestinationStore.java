package ulpgc.dacd.destination.control;

import ulpgc.dacd.destination.model.Destination;
import java.sql.*;

public class SqliteDestinationStore implements DestinationStore {
    private final String dbPath;

    public SqliteDestinationStore(String dbPath) {
        this.dbPath = dbPath;
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "CREATE TABLE IF NOT EXISTS destination (name TEXT, country TEXT, source_city TEXT, latitude REAL, longitude REAL, population INTEGER, distance REAL, PRIMARY KEY (name, country, source_city))";
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void storeDestination(Destination destination) {
        try (Connection conn = DriverManager.getConnection(dbPath);
             PreparedStatement checkStmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM destination WHERE name = ? AND country = ? AND latitude = ? AND longitude = ? AND population = ? AND distance = ?");
             PreparedStatement insertStmt = conn.prepareStatement(
                     "INSERT OR REPLACE INTO destination (name, country, latitude, longitude, population, distance) VALUES (?, ?, ?, ?, ?, ?)")) {

            checkStmt.setString(1, destination.getName());
            checkStmt.setString(2, destination.getCountry());
            checkStmt.setDouble(3, destination.getLatitude());
            checkStmt.setDouble(4, destination.getLongitude());
            checkStmt.setInt(5, destination.getPopulation());
            checkStmt.setDouble(6, destination.getDistance());

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                insertStmt.setString(1, destination.getName());
                insertStmt.setString(2, destination.getCountry());
                insertStmt.setDouble(3, destination.getLatitude());
                insertStmt.setDouble(4, destination.getLongitude());
                insertStmt.setInt(5, destination.getPopulation());
                insertStmt.setDouble(6, destination.getDistance());
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}