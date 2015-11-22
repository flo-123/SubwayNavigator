package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flo on 05/11/15.
 */
public class GlasgowSubwayStops extends Stops implements Serializable {

    private String pathToSoundFiles = "glasgow_sub";

    public GlasgowSubwayStops() {
        super();
    }


    protected void generateStops() {
        stops.add(new Stop("Partick"));
        stops.add(new Stop("Kelvinhall"));
        stops.add(new Stop("Hillhead"));
        stops.add(new Stop("Kelvinbridge"));
        stops.add(new Stop("St George's Cross"));
        stops.add(new Stop("Cowcaddens"));
        stops.add(new Stop("Buchanan Street"));
        stops.add(new Stop("St Enoch"));
        stops.add(new Stop("Bridge Street"));
        stops.add(new Stop("West Street"));
        stops.add(new Stop("Shields Road"));
        stops.add(new Stop("Kinning Park"));
        stops.add(new Stop("Cessnock"));
        stops.add(new Stop("Ibrox"));
        stops.add(new Stop("Govan"));

        stopsIn.add(new Stop("Govan"));
        stopsIn.add(new Stop("Ibrox"));
        stopsIn.add(new Stop("Cessnock"));
        stopsIn.add(new Stop("Kinning Park"));
        stopsIn.add(new Stop("Shields Road"));
        stopsIn.add(new Stop("West Street"));
        stopsIn.add(new Stop("Bridge Street"));
        stopsIn.add(new Stop("St Enoch"));
        stopsIn.add(new Stop("Buchanan Street"));
        stopsIn.add(new Stop("Cowcaddens"));
        stopsIn.add(new Stop("St George's Cross"));
        stopsIn.add(new Stop("Kelvinbridge"));
        stopsIn.add(new Stop("Hillhead"));
        stopsIn.add(new Stop("Kelvinhall"));
        stopsIn.add(new Stop("Partick"));
    }

    @Override
    public String getPathToSoundFiles() {
        return pathToSoundFiles;
    }

}
