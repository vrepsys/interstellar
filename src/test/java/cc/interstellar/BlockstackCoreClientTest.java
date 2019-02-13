package cc.interstellar;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class BlockstackCoreClientTest {

    @Test
    public void downloadNamesTest() {
        String coreNodeUrl = "https://any.io:8080";
        String namesUrl = String.format("%s/names", coreNodeUrl);

        StringDownloader downloader = mock(StringDownloader.class);
        when(downloader.downloadPage(namesUrl, 1)).thenReturn("['1.id', '2.id']");
        when(downloader.downloadPage(namesUrl, 2)).thenReturn("['3.id']");
        when(downloader.downloadPage(namesUrl, 3)).thenReturn("[]");

        BlockstackCoreClient coreClient = new BlockstackCoreClient(coreNodeUrl, downloader);

        List<String> names = coreClient.downloadAllNames();
        assertEquals(Arrays.asList("1.id", "2.id", "3.id"), names);
    }

    @Test
    public void downloadSubdomainsTest() {
        String coreNodeUrl = "https://any.io:8080";
        String subdomainsUrl = String.format("%s/subdomains", coreNodeUrl);

        StringDownloader downloader = mock(StringDownloader.class);
        when(downloader.downloadPage(subdomainsUrl, 1)).thenReturn("['1.blockstack.id', '2.blockstack.id']");
        when(downloader.downloadPage(subdomainsUrl, 2)).thenReturn("[]");

        BlockstackCoreClient coreClient = new BlockstackCoreClient(coreNodeUrl, downloader);

        List<String> names = coreClient.downloadAllSubdomains();
        assertEquals(Arrays.asList("1.blockstack.id", "2.blockstack.id"), names);
    }

}
