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
    }

    @Override
    public String getPathToSoundFiles() {
        return pathToSoundFiles;
    }

}
