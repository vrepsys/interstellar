package cc.interstellar.fakes;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


public class FakeBlockstackNode {

    private WireMockServer server;

    public void start() {
        server = new WireMockServer();
        server.start();
    }

    public void reset() {
        WireMock.reset();
    }

    public void stop() {
        server.stop();
    }



    public void givenUsernames(String ... usernames) {
        server.stubFor(get("/names?page=1")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("['one', 'two', 'three']")));
    }
}
