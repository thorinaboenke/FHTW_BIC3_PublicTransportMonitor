package at.fhtw.publictransportmonitor.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CsvOpener {
    public static ArrayList<Location> parseCsv() throws IOException {
        ArrayList<Location> locations = new ArrayList<Location>();
        BufferedReader br = null;
        // create a reader
        try {
            br = new BufferedReader(new FileReader("locations.csv"));


            // CSV file delimiter
            String DELIMITER = ",";

            // read the file line by line
            String line;
            String[] location;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.isEmpty()){
                    break;
                }
                // convert line into columns
                location = line.split(DELIMITER);

                // add location to array list
                locations.add(new Location(location[0], location[1]));
            }
            // return list of locations

        } catch (IOException ex) {
        System.out.println("No file found");
            ex.printStackTrace();
        }

        return locations;


    }
}
