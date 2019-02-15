package cc.interstellar.db;

import cc.interstellar.App;
import cc.interstellar.InterstellarException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InterstellarDao {

    private ConnectionSource connectionSource;

    public InterstellarDao(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    public void saveIdentities(List<String> identities) {
        String SQL = "INSERT INTO identities (username) VALUES(?) ON CONFLICT(username) DO NOTHING";
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


    public void rewriteApps(String username, List<App> apps) {
        try (Connection conn = connectionSource.getConnection()) {
            conn.setAutoCommit(false);
            try {
                removeApps(conn, username);
                insertApps(conn, username, apps);
            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                throw new InterstellarException(e);
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new InterstellarException(e);
        }
    }

    private static void removeApps(Connection conn, String username) throws SQLException {
        String removeAppsSql = "DELETE FROM apps WHERE username=?";
        PreparedStatement removeStmt = conn.prepareStatement(removeAppsSql);
        removeStmt.setString(1, username);
        removeStmt.executeUpdate();
    }

    private static void insertApps(Connection conn, String username, List<App> apps) throws SQLException {
        String insertAppsSql = "INSERT INTO apps (username, app_name, gaia_url) VALUES(?, ?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertAppsSql);
        for (App app : apps) {
            insertStmt.setString(1, username);
            insertStmt.setString(2, app.getName());
            insertStmt.setString(3, app.getGaiaUrl());
            insertStmt.addBatch();
        }
        insertStmt.executeBatch();
    }

    public Collection getAppsForUser(String username) {
        String sql = "SELECT app_name, gaia_url FROM apps WHERE username=?";
        try(Connection conn = connectionSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            List<App> apps = new ArrayList<>();
            while(rs.next()) {
                String appName = rs.getString("app_name");
                String gaiaUrl = rs.getString("gaia_url");
                apps.add(new App(appName, gaiaUrl));
            }
            return apps;
        } catch (SQLException e) {
            throw new InterstellarException(e);
        }
    }
}
