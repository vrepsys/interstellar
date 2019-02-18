package cc.interstellar.db;

import cc.interstellar.App;
import cc.interstellar.BlockstackIdentity;
import cc.interstellar.InterstellarException;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

public class InterstellarDao {

    private ConnectionSource connectionSource;

    public InterstellarDao(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    public void saveIdentities(List<BlockstackIdentity> identities) {
        String SQL = "INSERT INTO identities (username, created_at, profile_updated_at) " +
                     "VALUES(?, ?, ?) ON CONFLICT(username) DO NOTHING";
        try (Connection conn = connectionSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            for (BlockstackIdentity identity : identities) {
                statement.setString(1, identity.getUsername());
                statement.setObject(2, identity.getCreatedAt().atOffset(ZoneOffset.UTC));
                if (identity.getProfileUpdatedAt() != null) {
                    statement.setObject(3, identity.getProfileUpdatedAt().atOffset(ZoneOffset.UTC));
                } else {
                    statement.setNull(3, Types.TIMESTAMP);
                }
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new InterstellarException(e);
        }
    }

    public List<BlockstackIdentity> getAllIdentities() {
        String SQL = "SELECT username, created_at, profile_updated_at FROM identities";
        try(Connection conn = connectionSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL)) {
            List<BlockstackIdentity> identities = new ArrayList<>();
            while(rs.next()) {
                String username = rs.getString("username");
                Instant createdAt = rs.getTimestamp("created_at").toInstant();
                java.sql.Timestamp profileUpdatedAtTimestamp = rs.getTimestamp("profile_updated_at");
                Instant profileUpdatedAt = profileUpdatedAtTimestamp != null ? profileUpdatedAtTimestamp.toInstant() : null;
                identities.add(new BlockstackIdentity(username, createdAt, profileUpdatedAt));
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
