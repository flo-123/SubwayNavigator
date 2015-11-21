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
        int indexStart = stops.indexOf(startStop);
        int indexDest =  stops.indexOf(destStop);
        if(!inbound) {
            if(indexDest > indexStart) {
                for (Stop s : stops.subList(0,indexStart+1)) {
                    stopsInBetween.add(s);
                }
                Collections.reverse(stopsInBetween);

                for (Stop s : stops.subList(indexDest,stops.size()-1)) {
                    stopsInBetween.add(s);
                }
                Collections.reverse(stopsInBetween);
            }
            else {
                for (int i = indexStart; i >= indexDest; i--) {
                    stopsInBetween.add(stops.get(i));
                }
            }
        } else {
            if(indexDest > indexStart) {
                for (int i = indexStart+1; i <= indexDest; i++) {
                    stopsInBetween.add(stops.get(i));
                }
            }
            else {
                for (Stop s : stops.subList(indexStart,stops.size())) {
                    stopsInBetween.add(s);
                }

                for (Stop s : stops.subList(0 ,indexDest)) {
                    stopsInBetween.add(s);
                }

            }
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
