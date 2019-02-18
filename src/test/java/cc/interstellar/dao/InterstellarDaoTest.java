package cc.interstellar.dao;

import cc.interstellar.App;
import cc.interstellar.BlockstackIdentity;
import cc.interstellar.db.InterstellarDao;
import cc.interstellar.drivers.PostgresqlDriver;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static cc.interstellar.util.ExtendedAssertions.assertEqualsUnordered;

class InterstellarDaoTest {

    @Test
    void saveIdentities() {
        PostgresqlDriver.recreateDatabase();
        InterstellarDao dao = new InterstellarDao(PostgresqlDriver.CONNECTION_SOURCE);
        Instant now = Instant.now();
        List<BlockstackIdentity> identities = List.of(
            new BlockstackIdentity("1.id", now),
            new BlockstackIdentity("2.id", now, Instant.now())
        );
        dao.saveIdentities(identities);

        assertEqualsUnordered(identities, dao.getAllIdentities());
    }

    @Test
    void saveIdentitiesIgnoreDuplicates() {
        PostgresqlDriver.recreateDatabase();
        InterstellarDao dao = new InterstellarDao(PostgresqlDriver.CONNECTION_SOURCE);
        Instant now = Instant.now();
        BlockstackIdentity id1 = new BlockstackIdentity("1.id", now);
        BlockstackIdentity id2 = new BlockstackIdentity("2.id", now);
        BlockstackIdentity id3 = new BlockstackIdentity("3.id", now);
        List<BlockstackIdentity> identities1 = List.of(id1, id2);
        List<BlockstackIdentity> identities2 = List.of(id2, id3);

        dao.saveIdentities(identities1);

        dao.saveIdentities(identities2);

        assertEqualsUnordered(List.of(id1, id2, id3), dao.getAllIdentities());
    }

    @Test
    void saveApps() {
        PostgresqlDriver.recreateDatabase();
        InterstellarDao dao = new InterstellarDao(PostgresqlDriver.CONNECTION_SOURCE);

        List<App> apps = List.of(
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

        List<App> apps = List.of(
                new App("app1", "url1"),
                new App("app2", "url2")
        );
        dao.rewriteApps("valrepsys.id.blockstack", apps);

        List<App> apps2 = List.of(
                new App("app3", "url3"),
                new App("app4", "url4")
        );

        dao.rewriteApps("valrepsys.id.blockstack", apps2);

        assertEqualsUnordered(apps2, dao.getAppsForUser("valrepsys.id.blockstack"));
    }


}
