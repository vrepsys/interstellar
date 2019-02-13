package cc.interstellar;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;


public class StringDownloader {

    public String downloadPage(String url, int page) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(String.format("%s?page=%d", url, page)))
                .build();
        try {
            HttpResponse<String> resp = client.send(request, BodyHandlers.ofString());
            return resp.body();
        } catch (Exception e) {
            throw new InterstellarException(e);
        }
    }

}
