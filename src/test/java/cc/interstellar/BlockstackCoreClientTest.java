package cc.interstellar;

import static cc.interstellar.util.ExtendedAssertions.assertEqualsUnordered;
import static org.mockito.Mockito.*;

import cc.interstellar.util.TestResources;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class BlockstackCoreClientTest {

    @Test
    public void downloadNames() {
        String coreNodeUrl = "https://any.io:8080";
        String namesUrl = String.format("%s/names", coreNodeUrl);

        StringDownloader downloader = mock(StringDownloader.class);
        when(downloader.download(namesUrl, 1)).thenReturn("['1.id', '2.id']");
        when(downloader.download(namesUrl, 2)).thenReturn("['3.id']");
        when(downloader.download(namesUrl, 3)).thenReturn("[]");

        BlockstackCoreClient coreClient = new BlockstackCoreClient(coreNodeUrl, downloader);

        List<String> names = coreClient.downloadAllNames();
        assertEquals(Arrays.asList("1.id", "2.id", "3.id"), names);
    }

    @Test
    public void downloadSubdomains() {
        String coreNodeUrl = "https://any.io:8080";
        String subdomainsUrl = String.format("%s/subdomains", coreNodeUrl);

        StringDownloader downloader = mock(StringDownloader.class);
        when(downloader.download(subdomainsUrl, 1)).thenReturn("['1.blockstack.id', '2.blockstack.id']");
        when(downloader.download(subdomainsUrl, 2)).thenReturn("[]");

        BlockstackCoreClient coreClient = new BlockstackCoreClient(coreNodeUrl, downloader);

        List<String> names = coreClient.downloadAllSubdomains();
        assertEquals(Arrays.asList("1.blockstack.id", "2.blockstack.id"), names);
    }

    @Test
    void downloadUserDetails() {
        String coreNodeUrl = "https://any.io:8080";
        String nameDetailsUrl = String.format("%s/names/valrepsys.blockstack.id", coreNodeUrl);

        StringDownloader downloader = mock(StringDownloader.class);
        when(downloader.download(nameDetailsUrl)).thenReturn("{\"zonefile\": " +
                "\"$ORIGIN valrepsys.id.blockstack\\n" +
                "$TTL 3600\\n_http._tcp\\tIN\\tURI\\t10\\t1\\t\\" +
                "\"https://gaia.blockstack.org/hub/1G4nASzd9NATfh8v21RzZJGYzCJuxNqKVg" +
                "/profile.json\\\"\\n\\n\"}");

        BlockstackCoreClient coreClient = new BlockstackCoreClient(coreNodeUrl, downloader);

        UserDetails user = coreClient.downloadUserDetails("valrepsys.blockstack.id");
        assertEquals(new UserDetails(
                "valrepsys.blockstack.id",
                "https://gaia.blockstack.org/hub/1G4nASzd9NATfh8v21RzZJGYzCJuxNqKVg/profile.json"),
                user);
    }

    @Test
    void downloadUserApps() {
        String profileUrl = "https://any.io/valrepsys.blockstack.id";

        StringDownloader downloader = mock(StringDownloader.class);
        when(downloader.download(profileUrl)).thenReturn(TestResources.VAL_REPSYS_PROFILE);

        BlockstackCoreClient coreClient = new BlockstackCoreClient("", downloader);

        List<App> apps = coreClient.getAppsFromProfile("valrepsys.blockstack.id", profileUrl);
        assertEqualsUnordered(
                Arrays.asList(
                        new App("valrepsys.blockstack.id",
                                "https://dpage.io",
                                "https://gaia.blockstack.org/hub/18rJAUTgkKDHPqfX2YJSfREXgWguyzVZmZ/"),
                        new App("valrepsys.blockstack.id",
                                "https://www.stealthy.im",
                                "https://gaia.blockstack.org/hub/12sw96PXn1aaSQYYU9rLD6ZCXvMnx9vQT1/")),
                apps);
    }

}
