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


    /**
     * Sends a Http GET request to a provided url to retrieve a json object. Utilizes HttpClient.
     *
     * @param  url the url to be requested
     * @return      the body of the response as a string
     * @throws java.io.IOException
     */
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
