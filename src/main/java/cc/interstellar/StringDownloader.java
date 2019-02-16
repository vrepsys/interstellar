package cc.interstellar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;


public class StringDownloader {

    private static HttpClient HTTP = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final int timesToTry;

    private final Duration waitDuration;

    public StringDownloader() {
        this.timesToTry = 1;
        this.waitDuration = Duration.ofSeconds(0);
    }

    public StringDownloader(int timesToRetry, Duration waitDuration) {
        this.timesToTry = timesToRetry;
        this.waitDuration = waitDuration;
    }

    public String download(String url, int page) {
        return download(String.format("%s?page=%d", url, page));
    }

    public String download(String url) {
        return downloadWithRetries(url, this.timesToTry, this.waitDuration.toMillis());
    }


    private String downloadWithRetries(String url, int timesToTry, long waitDurationMillis) {
        logger.info(String.format("Downloading: %s", url));
        while (timesToTry > 0) {
            try {
                return downloadString(url);
            } catch (IOException e) {
                if (timesToTry == 1) {
                    throw new InterstellarException(e);
                }
                logger.warn("Connection problem, will try again", e);
                sleep(waitDurationMillis);
                waitDurationMillis *= 2;
            } catch (InterruptedException e) {
                throw new InterstellarException(e);
            }
            timesToTry--;
        }
        throw new InterstellarException("Impossible to reach");
    }

    private String downloadString(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .timeout(Duration.ofSeconds(20))
                .uri(URI.create(url))
                .build();
        HttpResponse<String> resp = HTTP.send(request, BodyHandlers.ofString());
        return resp.body();
    }

    private void sleep(long wait) {
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            throw new InterstellarException(e);
        }
    }

}
