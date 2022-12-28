BIC3 ODE Semesterproject, Thorina Boenke, ic21b060

# PublicTransportMonitor
Repository Url: https://github.com/thorinaboenke/PublicTransportMonitor

Die Applikation ist ein Display das für drei Orte die nächstgelegenen Haltestellen der Wiener Linien und stadtweite aktuelle Störungen des öffentlichen Nahverkehrs anzeigt.
Es wird die API der Wiener Linien verwendet:

[Wienerlinien Echtzeitdaten Dokumentation](https://www.wienerlinien.at/ogd_realtime/doku/ogd/wienerlinien-echtzeitdaten-dokumentation.pdf)


## Project Scope
### Must Have Features

- [x] Es gibt einen hardcoded Liste von Standorten die aus einer Datei eingelesen werden.
- [x] Ein Standort ist als Headquarter deklariert und per default als aktueller Standort ausgewählt
- [x] In der Applikation kann ein anderer Standort aus der Liste ausgewählt werden und über einen Buttonklick zum aktuellen Standort deklariert werden
- [x] Es werden die Haltestellen und Linien der umgebenden Haltestellen angezeigt 
- [x] Es gibt einen Button um eine neue Anfrage abzusenden/ die Anzeige zu aktualisieren

### Should Have Features

- [x] Es werden außerdem die jeweiligen Endstationen und die nächsten Abfahrtszeiten angezeigt
- [x] Es kann ein Report (.txt file) der aktuellen Anzeige generiert werden

### Nice to Have Features
- [x] Es werden außerdem Informationen zu aktuellen (stadtweiten) Störungen angezeigt
- [ ] Statt durch einen Buttonklick getriggert aktualisiert sich die Anzeige in regelmäßigen Intervallen (z.B. 1 mal pro minute)

### Overkill
- [ ] Es kann als aktueller Standort der Standort des Endgerätes ausgewählt werden
- [ ] Statt der wählbaren Locations wird eine (google maps o.ä.) Karte eingebunden auf der ein Pin gesetzt werden kann um den aktuellen Standort zu wählen und sich nahegelegene Haltestellen anzeigen zu lassen.


## Implementation
Die verschiedenen Orte und ihre IDs werden aus dem file "locations.csv" eingelesen.

Ein Graphical User Interface wurde mit JavaFx generiert.

Bei Klick auf "Display Departures" wird ein Http GET request für den aktuell ausgewählten Ort an "https://www.wienerlinien.at/ogd_realtime/monitor?stopId=<ID>&aArea=1"
gestellt. Der zurückerhaltene Json String wird in Objekte für LocationsStops, Lines und Deaprtures geparsed.

Bei Klick auf "Change Locations" wird der ausgewählte Ort gewechselt ohne eine neue Anfrage zustellen.

Bei Klick auf "Display Disruptions" wird ein Http GET request an "https://www.wienerlinien.at/ogd_realtime/trafficInfoList" gestellt. Der zurückerhaltene Json String wird in Disruption Objekts geparsed.

Bei Klick auf "Generate Report" wird die aktuelle Anzeige von Linien und Störungen im file "report.txt" abgespeichert. Dies geschieht in einem separaten Thread.

Javadocs can be found in the folder "docs". Open "index.html" in a browser to look at the project documentation.

![The Graphical User Inferface](https://github.com/thorinaboenke/PublicTransportMonitor/blob/main/src/main/resources/at/fhtw/publictransportmonitor/Screenshot%202022-12-28%20at%2014.59.27.png "GUI")




