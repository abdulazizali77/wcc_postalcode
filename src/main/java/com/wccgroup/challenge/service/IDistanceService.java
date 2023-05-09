package com.wccgroup.challenge.service;

import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

//TODO: implement other algos
public interface IDistanceService {
    double calculateDistance(double latitude, double longitude, double latitude2, double longitude2);

    @Async
    default CompletableFuture<Double> calculateDistanceAsync(double latitude, double longitude, double latitude2, double longitude2){
        return CompletableFuture.completedFuture(calculateDistance(latitude, longitude, latitude2, longitude2));
    };
}
