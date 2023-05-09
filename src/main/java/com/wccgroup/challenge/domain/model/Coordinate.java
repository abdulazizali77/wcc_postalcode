package com.wccgroup.challenge.domain.model;

import javax.persistence.Embeddable;

@Embeddable
public class Coordinate {
    private Double latitude;
    private Double longitude;
    public Coordinate(){};
    public Coordinate(Double latitude, Double longitude){
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
