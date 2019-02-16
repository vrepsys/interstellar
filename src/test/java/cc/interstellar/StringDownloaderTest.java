package cc.interstellar;

import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;


@ExtendWith(WireMockExtension.class)
public class StringDownloaderTest {

    @Test
    public void pageDownload() {
        stubFor(get(urlEqualTo("/download?page=1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/json")
                        .withBody("['one', 'two', 'three']")));

        StringDownloader downloader = new StringDownloader();

        String s = downloader.download("http://localhost:8080/download", 1);

        assertEquals(s, "['one', 'two', 'three']");
    }

    @Test
    public void download() {
        stubFor(get(urlEqualTo("/download"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("test")));

        StringDownloader downloader = new StringDownloader();

        String s = downloader.download("http://localhost:8080/download");

        assertEquals("test", s);
    }

    @Test
    public void downloadFailure() {
        stubFor(get(urlEqualTo("/download")).inScenario("retry at 500")
                .whenScenarioStateIs(Scenario.STARTED)
                .willSetStateTo("one time requested")
                .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

        stubFor(get(urlEqualTo("/download")).inScenario("retry at 500")
                .whenScenarioStateIs("one time requested")
                .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

        StringDownloader downloader = new StringDownloader(2, Duration.ofMillis(1));

        assertThrows(InterstellarException.class, ()-> downloader.download("http://localhost:8080/download"));
    }

    @Test
    public void downloadWithRetries() {
        stubFor(get(urlEqualTo("/download")).inScenario("retry at 500")
                .whenScenarioStateIs(Scenario.STARTED)
                .willSetStateTo("one time requested")
                .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

        stubFor(get(urlEqualTo("/download")).inScenario("retry at 500")
                .whenScenarioStateIs("one time requested")
                .willReturn(aResponse().withBody("test")));



        StringDownloader downloader = new StringDownloader(2, Duration.ofMillis(1));

        String s = downloader.download("http://localhost:8080/download");

        assertEquals("test", s);
    }

}
