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

    public void writeStringToTxt(String ... input){
        try (PrintWriter out = new PrintWriter(filename)) {
            for (String s : input) {
                out.println(s);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    };
    public void run(){
        writeStringToTxt(this.s);
    }
}
