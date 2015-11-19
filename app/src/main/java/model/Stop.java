package model;

import java.io.Serializable;

/**
 * Created by Flo on 05/11/15.
 */
public class Stop implements Serializable {
    private String name;

    public Stop(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Stop))
            return false;
        if (obj == this)
            return true;
        Stop stop = (Stop) obj;
        return this.getName().equals(stop.getName());
    }
}
