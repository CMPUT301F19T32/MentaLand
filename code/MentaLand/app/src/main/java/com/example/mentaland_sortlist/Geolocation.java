package com.example.mentaland_sortlist;

import android.os.Parcel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/** Model for holding geo location information from Google Maps API.
 *  Used my records to denote a user selected location from a map.
 *
 */
public class Geolocation implements Serializable {
    private double lat;
    private double lon;

    public Geolocation(double latitude, double longitude) {
        this.lat = latitude;
        this.lon = longitude;
    }

    protected Geolocation(Parcel in) {
        lat = in.readDouble();
        lon = in.readDouble();
    }

    public double getLatitude() {
        return lat;
    }

    public void setLatitude(double latitude) {
        this.lat = latitude;
    }

    public double getLongitude() {
        return lon;
    }

    public void setLongitude(double longitude) {
        this.lon = longitude;
    }

    private void readObject(
            ObjectInputStream inputStream
    ) throws ClassNotFoundException, IOException {
        inputStream.defaultReadObject();
    }

    private void writeObject(
            ObjectOutputStream outputStream
    ) throws IOException {
        outputStream.defaultWriteObject();
    }
}
