package cc.interstellar;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;


public class StringDownloader {

    public String download(String url, int page) {
        return download(String.format("%s?page=%d", url, page));
    }


    public String download(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();
        try {
            HttpResponse<String> resp = client.send(request, BodyHandlers.ofString());
            return resp.body();
        } catch (Exception e) {
            throw new InterstellarException(e);
        }
    }

}
