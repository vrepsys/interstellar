package cc.interstellar.drivers;

import cc.interstellar.InterstellarException;
import cc.interstellar.db.ConnectionSource;
import cc.interstellar.db.DatabaseSchema;
import cc.interstellar.db.PostgreSQLConnectionSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresqlDriver {

    public static ConnectionSource CONNECTION_SOURCE = new PostgreSQLConnectionSource(
            "jdbc:postgresql://localhost/interstellar_test",
            "postgres");

    public static void recreateDatabase() {
        ConnectionSource source = new PostgreSQLConnectionSource(
                "jdbc:postgresql://localhost/",
                "postgres");

        try(Connection conn = source.getConnection();
            Statement stmt = conn.createStatement()) {
            String sql = "DROP DATABASE if exists interstellar_test; CREATE DATABASE interstellar_test;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new InterstellarException(e);
        }

        DatabaseSchema schema = new DatabaseSchema(CONNECTION_SOURCE);
        schema.createSchema();
    }

}
