package cc.interstellar;

import cc.interstellar.db.ConnectionSource;
import cc.interstellar.db.InterstellarDao;
import cc.interstellar.db.PostgreSQLConnectionSource;

import java.util.List;

public class BlockstackImporter {

    public void importAll() {

        StringDownloader downloader = new StringDownloader();
        BlockstackCoreClient client = new BlockstackCoreClient("", downloader);

        List<String> names = client.downloadAllNames();
        List<String> subdomains = client.downloadAllSubdomains();


        ConnectionSource connectionSource = new PostgreSQLConnectionSource(
                "jdbc:postgresql://localhost/interstellar",
                "interstellar",
                "interstellar");

        InterstellarDao dao = new InterstellarDao(connectionSource);

        dao.saveIdentities(names);
        dao.saveIdentities(subdomains);

        List<String> identities = dao.getIdentities();

        for (String username : identities) {
            UserDetails details = client.downloadUserDetails(username);
            List<App> apps = client.getAppsFromProfile(details.getProfileUrl());
        }


    }

}
