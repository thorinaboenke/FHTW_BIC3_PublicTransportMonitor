package at.fhtw.publictransportmonitor.model;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class TrafficInfoRequest {
    public String getURL(String url) throws IOException, InterruptedException, URISyntaxException {
    HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(url))
            .headers("Content-Type", "application/json")
            .timeout(Duration.of(10, SECONDS))
            .GET().build();

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
    return (String) response.body();
    }
}
