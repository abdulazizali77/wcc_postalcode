package com.wccgroup.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistanceResponse {

    String verbose;
    Double distance;

    public String getVerbose() {
        return verbose;
    }

    public Double getDistance() {
        return distance;
    }

    public DistanceResponse(String verbose) {
        this(verbose, null);
    }

    public DistanceResponse(Double distance) {
        this(null, distance);
    }

    public DistanceResponse(String verbose, Double distance) {
        this.verbose = verbose;
        this.distance = distance;
    }
}
