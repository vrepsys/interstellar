package cc.interstellar;

import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(WireMockExtension.class)
public class StringDownloaderTest {

    @Test
    public void exampleTest() {
        stubFor(get(urlEqualTo("/download?page=1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/json")
                        .withBody("['one', 'two', 'three']")));

        StringDownloader downloader = new StringDownloader();

        String s = downloader.downloadPage("http://localhost:8080/download", 1);

        assertEquals(s, "['one', 'two', 'three']");
    }

}
