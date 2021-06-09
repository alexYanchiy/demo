package ru.nc.demo.parser.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MapPoint {
    private final double latitude;
    private final double longitude;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MapPoint(@JsonProperty("latitude") double latitude,
                    @JsonProperty("longitude") double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @JsonIgnore
    public double getY() {
        return this.getLongitude();
    }

    @JsonIgnore
    public double getX() {
        return this.getLatitude();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MapPoint point = (MapPoint) o;
        return Double.compare(point.latitude, latitude) == 0
                && Double.compare(point.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return "MapPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public static MapPoint of(double latitude, double longitude) {
        return new MapPoint(latitude, longitude);
    }

    public static MapPoint ofXY(double x, double y) {
        return new MapPoint(x, y);
    }

}
