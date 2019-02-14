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
        try(Connection conn = connectionSource.getConnection();
            Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE identities (username VARCHAR(255) not NULL, PRIMARY KEY ( username ));";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new InterstellarException(e);
        }
    }

}
