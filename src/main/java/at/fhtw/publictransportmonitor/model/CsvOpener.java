package at.fhtw.publictransportmonitor.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CsvOpener {

    /**
     * Parses a .csv file "locations.csv" in the root directory into an array of Locations
     * Makes use of a Buffered Reader.
     * If the file does not exist an IOException is thrown.
     * First column of the file holds name, second column id of the location, the rest is ignored
     *
     * @param  filename the file to be parsed
     * @return      an array list of Locations
     * @throws java.io.IOException throws an exception if file cannot be found
     */
    public ArrayList<Location> parseCsv(String filename) throws IOException {
        ArrayList<Location> locations = new ArrayList<Location>();
        BufferedReader br = null;
        // create a reader
        try {
            br = new BufferedReader(new FileReader(filename));


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
