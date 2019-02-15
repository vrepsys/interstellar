package cc.interstellar.db;

import cc.interstellar.InterstellarException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSchema {

    private ConnectionSource connectionSource;

    public DatabaseSchema(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    public void createSchema() {
        String[] sqls =
                {"CREATE TABLE identities (username text not NULL, PRIMARY KEY ( username ))",
                 "CREATE TABLE apps (username text not NULL, app_name text not null, gaia_url text not null, PRIMARY KEY (username, app_name))"};
        try(Connection conn = connectionSource.getConnection();
            Statement stmt = conn.createStatement()) {
            for(String sql: sqls) {
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            throw new InterstellarException(e);
        }
    }

}
