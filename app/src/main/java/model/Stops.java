package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Flo on 05/11/15.
 */
public abstract class Stops implements Serializable {
    protected List<Stop> stops;
    protected boolean inbound;

    public Stops() {
        stops = new ArrayList<Stop>();
        generateStops();
    }

    protected abstract void generateStops();

    public List<Stop> getStops() {
        return stops;
    }

    public String[] getStringArrayOfStopsStartingFrom(Stop stop) {
        String[] stringArrayOfStops =  new String[stops.size()];
        int indexOfStartStop = 0;
        for(int i = 0; i < stops.size(); i++) {
            if (stops.get(i).equals(stop)) {
                indexOfStartStop = i;
            }
        }
        for(int i = 0; i < stops.size(); i++) {
            int j = (i + indexOfStartStop) % stops.size();
            stringArrayOfStops[i] = stops.get(j).getName();
        }
        return stringArrayOfStops;
    }

    public void reverseStopList () {
        List<Stop> newStops = new ArrayList<Stop>(stops.size());
        for(int i = 1; i <= stops.size(); i++) {
            newStops.add(stops.get(stops.size()-i));
        }
        stops = newStops;
    }

    public List<Stop> stopsInBetween(Stop startStop, Stop destStop) {
        List<Stop> stopsInBetween = new ArrayList<Stop>();
        if(!inbound) {
            reverseStopList();
        }
        int indexStart = stops.indexOf(startStop);
        int indexDest =  stops.indexOf(destStop);
        if(indexDest > indexStart) {
            for (int i = indexStart+1; i <= indexDest; i++) {
                stopsInBetween.add(stops.get(i));
            }
        }
        else {
            for (int i = indexDest; i > indexStart; i = (i-1) % stops.size()) {
                stopsInBetween.add(stops.get(i));
            }
            Collections.reverse(stopsInBetween);
        }

        return stopsInBetween;
    }

    public boolean isInbound() {
        return inbound;
    }

    public void setInbound(boolean inbound) {
        this.inbound = inbound;
    }

    public abstract String getPathToSoundFiles();

}
