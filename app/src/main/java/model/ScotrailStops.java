package model;

import java.io.Serializable;

/**
 * Created by Flo on 05/11/15.
 */
public class ScotrailStops extends Stops implements Serializable {

    private String pathToSoundFiles = "scotrail";

    public ScotrailStops() {
        super();
    }


    protected void generateStops() {
        stops.add(new Stop("Glasgow Central"));
        stops.add(new Stop("Paisley Gilmour Street"));
        stops.add(new Stop("Johnstone"));
        stops.add(new Stop("Milliken Park"));
        stops.add(new Stop("Howwood"));
        stops.add(new Stop("Lochwinnoch"));
        stops.add(new Stop("Glengarnock"));
        stops.add(new Stop("Dalry"));
        stops.add(new Stop("Kilwinning"));
        stops.add(new Stop("Irvine"));
        stops.add(new Stop("Barassie"));
        stops.add(new Stop("Troon"));
        stops.add(new Stop("Prestwick Int. Airport"));
        stops.add(new Stop("Prestwick Town"));
        stops.add(new Stop("Newton-On-Ayr"));
        stops.add(new Stop("Ayr Station"));

        stopsIn.add(new Stop("Ayr Station"));
        stopsIn.add(new Stop("Newton-On-Ayr"));
        stopsIn.add(new Stop("Prestwick Town"));
        stopsIn.add(new Stop("Prestwick Int. Airport"));
        stopsIn.add(new Stop("Troon"));
        stopsIn.add(new Stop("Barassie"));
        stopsIn.add(new Stop("Irvine"));
        stopsIn.add(new Stop("Kilwinning"));
        stopsIn.add(new Stop("Dalry"));
        stopsIn.add(new Stop("Glengarnock"));
        stopsIn.add(new Stop("Lochwinnoch"));
        stopsIn.add(new Stop("Howwood"));
        stopsIn.add(new Stop("Milliken Park"));
        stopsIn.add(new Stop("Johnstone"));
        stopsIn.add(new Stop("Paisley Gilmour Street"));
        stopsIn.add(new Stop("Glasgow Central"));
    }

    @Override
    public String getPathToSoundFiles() {
        return pathToSoundFiles;
    }

}
