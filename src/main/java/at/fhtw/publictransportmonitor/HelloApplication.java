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
import java.io.PrintWriter;
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
        TextArea disruptions = new TextArea();
        BorderPane borderPane = new BorderPane();
        VBox innerVBox = new VBox();
        ListView<String> lvLocations = new ListView<>();

        Button btnDisplay = new Button("Display Departures");
        Button btnSwitchLocation = new Button("Switch Location");
        Button btnDisplayDisruptions = new Button("Display Disruptions");
        Button btnGenerateReport = new Button("Generate Report");

        outerVBox.getChildren().addAll(departures, disruptions, borderPane);
        borderPane.setLeft(innerVBox);
        borderPane.setCenter(lvLocations);
        innerVBox.getChildren().addAll(btnDisplay, btnSwitchLocation, btnDisplayDisruptions, btnGenerateReport);

        departures.setPrefHeight(200);
        btnDisplay.setPrefWidth(200);
        btnSwitchLocation.setPrefWidth(200);
        btnDisplayDisruptions.setPrefWidth(200);
        btnGenerateReport.setPrefWidth(200);
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
        // select default location
        lvLocations.getSelectionModel().select(0);

        // for switching the selected locations
        btnSwitchLocation.setOnAction(e -> {
            lvLocations.getSelectionModel().getSelectedItem();
        });

        // writing a report to a txt file. StringToTxtWriter uses Threads
        btnGenerateReport.setOnAction(e -> {
            StringToTxtWriter writer = new StringToTxtWriter("report.txt", new String[]{departures.getText(), disruptions.getText()});
            writer.run();
        });

        btnDisplayDisruptions.setOnAction(e -> {
            disruptions.clear();
            String url = "https://www.wienerlinien.at/ogd_realtime/trafficInfoList";
            try {
                // retrieve TrafficInfo with get request
                TrafficInfoRequest disruptionRequest = new TrafficInfoRequest();
                String disruptionInfo = disruptionRequest.getURL(url);

                // read JSON Tree
                ObjectMapper mapper = new ObjectMapper();
                List<Disruption> disruptionList = new ArrayList<Disruption>();
                JsonNode jsonNode = mapper.readTree(disruptionInfo);
                JsonNode disruptionsArrayNode = jsonNode.at("/data/trafficInfos");

                // iterate over disruption array
                for (int i = 0; i < disruptionsArrayNode.size(); i++){
                    if (disruptionsArrayNode.get(i).at("/refTrafficInfoCategoryId").asInt() != 3) {
                        JsonNode title = disruptionsArrayNode.get(i).at("/title");
                        JsonNode description = disruptionsArrayNode.get(i).at("/description");
                        JsonNode reason = disruptionsArrayNode.get(i).at("/attributes/reason");
                        Disruption dis = new Disruption(title.asText(), description.asText(), reason.asText());
                        disruptionList.add(dis);
                        // append the retrieved disruption to the TextArea for displaying
                        disruptions.appendText(dis.toString());
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }

        });

        btnDisplay.setOnAction(e -> {
            departures.clear();
            String input = lvLocations.getSelectionModel().getSelectedItem();
            Integer id = 1450; // default
            String numberOnly = input.replaceAll("[^0-9]", ""); // leaves only digits
            id = Integer.parseInt(numberOnly);
            // request for one id and all stops with same diva id
            String url = "https://www.wienerlinien.at/ogd_realtime/monitor?stopId=" + id.toString() +"&aArea=1";
            try {
                TrafficInfoRequest req = new TrafficInfoRequest();
                String trafficInfo = req.getURL(url);

                ObjectMapper mapper = new ObjectMapper();
                LocationStop locationStop = new LocationStop();

                JsonNode jsonNode = mapper.readTree(trafficInfo);
                JsonNode arrayNode = jsonNode.at("/data/monitors");
                // iterate over all locations stops in the response
                for (int h = 0; h < arrayNode.size(); h++) {
                    // retrieve the title of the stop
                    JsonNode locationStopNode = arrayNode.get(h).at("/locationStop/properties/title");
                    locationStop.setTitle(locationStopNode.toString());
                    if( h== 0) {
                        // only display the stop name once on first iteration
                        departures.appendText(locationStopNode.toString());
                    }

                    // retrieve the array of lines for that location Stop
                    // iterate over the lines to gather name, towards and departures
                    JsonNode linesNode = arrayNode.get(h).at("/lines");
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
                            }
                            line.setDepartures(deps);
                            lines.add(line);
                        }
                        locationStop.setLines(lines);
                    }
                    // display information in the textArea
                    departures.appendText(locationStop.toString());
                }

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

    public static void main(String[] args) {
        launch();
    }
}

