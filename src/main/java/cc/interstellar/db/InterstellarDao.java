package cc.interstellar.db;

import cc.interstellar.InterstellarException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterstellarDao {

    private ConnectionSource connectionSource;

    public InterstellarDao(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    public void saveIdentities(List<String> identities) {
        String SQL = "INSERT INTO identities (username) VALUES(?)";
        try (Connection conn = connectionSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {

            for (String username : identities) {
                statement.setString(1, username);
                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException e) {
            throw new InterstellarException(e);
        }
    }

    public List<String> getIdentities() {
        String SQL = "SELECT username FROM identities";
        try(Connection conn = connectionSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL)) {
            List<String> identities = new ArrayList<>();
            while(rs.next()) {
                identities.add(rs.getString("username"));
            }
            return identities;
        } catch (SQLException e) {
            throw new InterstellarException(e);
        }
    }

}
