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
    protected List<Stop> stopsIn;
    protected boolean inbound;

    public Stops() {
        stops = new ArrayList<Stop>();
        stopsIn = new ArrayList<Stop>();
        generateStops();
    }

    protected abstract void generateStops();

    public List<Stop> getStops() {
        return stops;
    }
    public List<Stop> getStopsIn() {
        return stopsIn;
    }

    public String[] getStringArrayOfStopsStartingFrom(Stop stop, boolean inbound) {
        if(!inbound) {
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
        } else {
            String[] stringArrayOfStops =  new String[stopsIn.size()];
            int indexOfStartStop = 0;
            for(int i = 0; i < stopsIn.size(); i++) {
                if (stopsIn.get(i).equals(stop)) {
                    indexOfStartStop = i;
                }
            }
            for(int i = 0; i < stopsIn.size(); i++) {
                int j = (i + indexOfStartStop) % stopsIn.size();
                stringArrayOfStops[i] = stopsIn.get(j).getName();
            }
            return stringArrayOfStops;
        }
    }

    public List<Stop> stopsInBetween(Stop startStop, Stop destStop) {
        List<Stop> stopsInBetween = new ArrayList<Stop>();
        if(!inbound) {
            int indexStart = getIndexOfStopByName(startStop.getName(), false);
            int indexDest =  getIndexOfStopByName(destStop.getName(),false);
            if(indexDest > indexStart) {
                for (int i = indexStart+1; i <= indexDest; i++) {
                    stopsInBetween.add(stops.get(i));
                }
            }
            else {
                return null;
            }
        } else {
            int indexStart = getIndexOfStopByName(startStop.getName(), true);
            int indexDest =  getIndexOfStopByName(destStop.getName(),true);
            if(indexDest > indexStart) {
                for (int i = indexStart+1; i <= indexDest; i++) {
                    stopsInBetween.add(stopsIn.get(i));
                }
            }
            else {
                return null;
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

    private int getIndexOfStopByName(String stopName, boolean inbound) {
        int index = -1;
        if(inbound) {
            for(int i = 0; i < stopsIn.size(); i++) {
                if(stopsIn.get(i).getName().equals(stopName)) {
                    index = i;
                }
            }
        } else {
            for(int i = 0; i < stops.size(); i++) {
                if(stops.get(i).getName().equals(stopName)) {
                    index = i;
                }
            }
        }
        return index;
    }

}
