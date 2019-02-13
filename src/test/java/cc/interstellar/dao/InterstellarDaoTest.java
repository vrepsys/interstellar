package cc.interstellar.dao;

import cc.interstellar.db.InterstellarDao;
import cc.interstellar.db.PostgreSQLConnectionSource;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import static cc.interstellar.util.ExtendedAssertions.assertEqualsUnordered;

public class InterstellarDaoTest {

    @Test
    public void daoTest() throws SQLException {
        PostgreSQLConnectionSource connectionSource = new PostgreSQLConnectionSource(
                "jdbc:postgresql://localhost/interstellar-test",
                "interstellar_test",
                "interstellar_test");

        Connection conn = connectionSource.getConnection();
        Statement stmt = conn.createStatement();

        String sql = "CREATE TABLE identities (username VARCHAR(255) not NULL, PRIMARY KEY ( username ))";

        stmt.executeUpdate(sql);

        InterstellarDao dao = new InterstellarDao(connectionSource);

        dao.saveIdentities(Arrays.asList("1.id", "2.id"));

        assertEqualsUnordered(Arrays.asList("1.id", "2.id"), dao.getIdentities());

        conn.close();
    }

}
