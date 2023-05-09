package com.wccgroup.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistanceResponse {

    Postcode postcode1;
    Postcode postcode2;
    String verbose;
    Double distance;
    String unit = "km";

    public Postcode getPostcode1() {return postcode1;}
    public Postcode getPostcode2() {return postcode2;}
    public String getVerbose() {return verbose;}
    public Double getDistance() {
        return distance;
    }
    public String getUnit() {
        return unit;
    }

    public DistanceResponse(){}
    public DistanceResponse(String verbose) {
        this(verbose, null, null, null, Units.KM.label);
    }

    public DistanceResponse(Postcode pc1, Postcode pc2, Double distance) {
        this(null, pc1, pc2, distance, Units.KM.label);
    }

    public DistanceResponse(String verbose, Postcode pc1, Postcode pc2, Double distance, String unit) {
        this.verbose = verbose;
        this.postcode1 = pc1;
        this.postcode2 = pc2;
        this.distance = distance;
        this.unit = unit;
    }
}
