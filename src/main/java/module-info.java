module at.fhtw.publictransportmonitor {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires eu.hansolo.tilesfx;
    requires java.net.http;
    requires com.google.gson;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens at.fhtw.publictransportmonitor to javafx.fxml;
    exports at.fhtw.publictransportmonitor;
}