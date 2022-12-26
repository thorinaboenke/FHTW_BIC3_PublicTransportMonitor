package at.fhtw.publictransportmonitor.model;

import java.io.IOException;
import java.io.PrintWriter;

public class StringToTxtWriter extends Thread{
    String filename;
    String[] s;

    public StringToTxtWriter(String filename, String[] s) {
        this.filename = filename;
        this.s = s;
    }


    /**
     * Writes an array of Strings into a .txt file in the root directory.
     * @param  input an array of string to be written to a file
     *
     */
    public void writeStringToTxt(String ... input){
        try (PrintWriter out = new PrintWriter(filename)) {
            for (String s : input) {
                out.println(s);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return;
    };
    public void run(){
        writeStringToTxt(this.s);
    }
}
