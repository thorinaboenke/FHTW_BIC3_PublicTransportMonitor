package at.fhtw.publictransportmonitor;

import at.fhtw.publictransportmonitor.model.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInput;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import com.fasterxml.jackson.databind.*;

import static java.time.temporal.ChronoUnit.SECONDS;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        VBox outerVBox = new VBox();
        TextArea departures = new TextArea();
        BorderPane borderPane = new BorderPane();
        VBox innerVBox = new VBox();
        ListView<String> lvLocations = new ListView<>();

        Button btnDisplay = new Button("Display Departures");
        Button btnSwitchLocation = new Button("Switch Location");

        outerVBox.getChildren().addAll(departures, borderPane);
        borderPane.setLeft(innerVBox);
        borderPane.setCenter(lvLocations);
        innerVBox.getChildren().addAll(btnDisplay, btnSwitchLocation);

        departures.setPrefHeight(200);
        btnDisplay.setPrefWidth(200);
        btnSwitchLocation.setPrefWidth(200);
        innerVBox.setSpacing(10);
        outerVBox.setSpacing(10);
        innerVBox.setPadding(new Insets(0,10,0,0));
        outerVBox.setPadding(new Insets(10,10,10,10));

        CsvOpener csvOpener = new CsvOpener();
        ArrayList<Location> loc = csvOpener.parseCsv();
;
        ObservableList<String> locations = FXCollections.observableArrayList();
        for( Location l : loc ){
            locations.add(l.toString());
         }

        lvLocations.setItems(locations);



        btnSwitchLocation.setOnAction(e -> {
            lvLocations.getSelectionModel().getSelectedItem();

        });

        btnDisplay.setOnAction(e -> {
            lvLocations.getSelectionModel().getSelectedItem();
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://www.wienerlinien.at/ogd_realtime/monitor?stopId=1450"))
                        .headers("Content-Type", "application/json")
                        .timeout(Duration.of(10, SECONDS))
                        .GET().build();

                HttpClient client = HttpClient.newHttpClient();
                HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                LocationStop locationStop = new LocationStop();

                JsonNode jsonNode = mapper.readTree(response.body().toString());
                JsonNode arrayNode = jsonNode.at("/data/monitors");
                // retrieve the title of the stop
                JsonNode locationStopNode = arrayNode.get(0).at("/locationStop/properties/title");
                locationStop.setTitle(locationStopNode.toString());

                // retrieve the array of lines for that location Stop
                // iterate over the lines to gather name, towards and departures
                JsonNode linesNode = arrayNode.get(0).at("/lines");
                List<Line> lines = new ArrayList<Line>();
                if (linesNode.isArray()) {
                    ArrayNode lineNodes = (ArrayNode) linesNode;
                    for (int i = 0; i < lineNodes.size(); i++) {
                        Line line = new Line();
                        JsonNode arrayElement = lineNodes.get(i);
                        String name = arrayElement.get("name").asText();
                        String towards = arrayElement.get("towards").asText();
                        line.setName(name);
                        line.setTowards(towards);
                        // iterate over the departures to add a list of departures to the line
                        List<Departure> deps = new ArrayList<Departure>();
                        JsonNode departureNode = arrayElement.at("/departures/departure");
                        ArrayNode departureNodes = (ArrayNode) departureNode;
                        for (int j = 0; j < departureNodes.size(); j++) {
                            Departure dep = new Departure();
                            String timePlanned = String.valueOf(departureNodes.get(j).at("/departureTime/timePlanned"));
                            String timeReal = String.valueOf(departureNodes.get(j).at("/departureTime/timeReal"));
                            Integer countdown = departureNodes.get(j).at("/departureTime/countdown").asInt();
                            dep.setTimePlanned(timePlanned);
                            dep.setTimeReal(timeReal);
                            dep.setCountdown(countdown);
                            deps.add(dep);
                            System.out.println(dep.getCountdown());
                        }

                        line.setDepartures(deps);
                        lines.add(line);
                    }
                    locationStop.setLines(lines);
                }

                departures.appendText(locationStop.toString());

            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        Scene scene = new Scene(outerVBox);
        stage.setTitle("Public Transport Monitor");
        stage.setScene(scene);
        stage.show();
    }

    // adapted from https://jenkov.com/tutorials/java-json/jackson-jsonnode.html
    public static void traverse(JsonNode root){

        if(root.isObject()){
            Iterator<String> fieldNames = root.fieldNames();

            while(fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode fieldValue = root.get(fieldName);
                traverse(fieldValue);
            }
        } else if(root.isArray()){
            ArrayNode arrayNode = (ArrayNode) root;
            for(int i = 0; i < arrayNode.size(); i++) {
                JsonNode arrayElement = arrayNode.get(i);
                traverse(arrayElement);
            }
        } else {
            // JsonNode root represents a single value field - do something with it.

        }
    }

    public static void main(String[] args) {
        launch();
    }
}

