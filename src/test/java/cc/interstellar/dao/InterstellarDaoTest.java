package cc.interstellar.dao;

import cc.interstellar.InterstellarException;
import cc.interstellar.db.InterstellarDao;
import cc.interstellar.drivers.PostgresqlDriver;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static cc.interstellar.util.ExtendedAssertions.assertEqualsUnordered;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InterstellarDaoTest {

    @Test
    public void daoTest() {
        PostgresqlDriver.recreateDatabase();
        InterstellarDao dao = new InterstellarDao(PostgresqlDriver.CONNECTION_SOURCE);
        dao.saveIdentities(Arrays.asList("1.id", "2.id"));

        assertEqualsUnordered(Arrays.asList("1.id", "2.id"), dao.getIdentities());
    }

    @Test
    public void namesShouldBeUnique() {
        PostgresqlDriver.recreateDatabase();
        InterstellarDao dao = new InterstellarDao(PostgresqlDriver.CONNECTION_SOURCE);

        dao.saveIdentities(Arrays.asList("1.id", "2.id"));

        assertThrows(InterstellarException.class, () ->
                dao.saveIdentities(Collections.singletonList("1.id"))
        );
    }

}
