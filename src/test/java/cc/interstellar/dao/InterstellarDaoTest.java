package cc.interstellar.dao;

import cc.interstellar.App;
import cc.interstellar.db.InterstellarDao;
import cc.interstellar.drivers.PostgresqlDriver;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static cc.interstellar.util.ExtendedAssertions.assertEqualsUnordered;

class InterstellarDaoTest {

    @Test
    void saveIdentities() {
        PostgresqlDriver.recreateDatabase();
        InterstellarDao dao = new InterstellarDao(PostgresqlDriver.CONNECTION_SOURCE);
        dao.saveIdentities(Arrays.asList("1.id", "2.id"));

        assertEqualsUnordered(Arrays.asList("1.id", "2.id"), dao.getIdentities());
    }

    @Test
    void saveIdentitiesIgnoreDuplicates() {
        PostgresqlDriver.recreateDatabase();
        InterstellarDao dao = new InterstellarDao(PostgresqlDriver.CONNECTION_SOURCE);
        dao.saveIdentities(Arrays.asList("1.id", "2.id"));

        dao.saveIdentities(Arrays.asList("1.id", "3.id"));

        assertEqualsUnordered(Arrays.asList("1.id", "2.id", "3.id"), dao.getIdentities());
    }

    @Test
    void saveApps() {
        PostgresqlDriver.recreateDatabase();
        InterstellarDao dao = new InterstellarDao(PostgresqlDriver.CONNECTION_SOURCE);

        List<App> apps = Arrays.asList(
                new App("app1", "url1"),
                new App("app2", "url2")
        );

        dao.rewriteApps("valrepsys.id.blockstack", apps);

        assertEqualsUnordered(apps, dao.getAppsForUser("valrepsys.id.blockstack"));
    }

    @Test
    void rewriteApps() {
        PostgresqlDriver.recreateDatabase();
        InterstellarDao dao = new InterstellarDao(PostgresqlDriver.CONNECTION_SOURCE);

        List<App> apps = Arrays.asList(
                new App("app1", "url1"),
                new App("app2", "url2")
        );

        dao.rewriteApps("valrepsys.id.blockstack", apps);

        List<App> apps2 = Arrays.asList(
                new App("app3", "url3"),
                new App("app4", "url4")
        );

        dao.rewriteApps("valrepsys.id.blockstack", apps2);

        assertEqualsUnordered(apps2, dao.getAppsForUser("valrepsys.id.blockstack"));
    }


}
