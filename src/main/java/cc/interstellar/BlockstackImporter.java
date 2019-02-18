package cc.interstellar;

import cc.interstellar.db.ConnectionSource;
import cc.interstellar.db.InterstellarDao;
import cc.interstellar.db.PostgreSQLConnectionSource;

import java.time.Duration;
import java.util.List;

public class BlockstackImporter {

    public void importAll() {
        StringDownloader downloader = new StringDownloader(3, Duration.ofSeconds(10));
        BlockstackCoreClient client = new BlockstackCoreClient("https://core.blockstack.org/v1", downloader);

        List<String> names = client.downloadAllNames();
        List<String> subdomains = client.downloadAllSubdomains();

        ConnectionSource connectionSource = new PostgreSQLConnectionSource(
                "jdbc:postgresql://localhost/interstellar",
                "interstellar",
                "interstellar");

        InterstellarDao dao = new InterstellarDao(connectionSource);

//        dao.saveIdentities(names);
//        dao.saveIdentities(subdomains);
//
//        List<BlockstackIdentity> identities = dao.getAllIdentities();
//
//        for (String username : identities) {
//            UserDetails details = client.downloadUserDetails(username);
//            List<App> apps = client.getAppsFromProfile(details.getProfileUrl());
//            dao.rewriteApps(details.getUsername(), apps);
//        }
    }

    public static void main(String[] args) {
        BlockstackImporter importer = new BlockstackImporter();
        importer.importAll();
    }

}
