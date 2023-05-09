package com.wccgroup.challenge.controller;

import com.wccgroup.challenge.domain.model.DistanceRequest;
import com.wccgroup.challenge.domain.model.DistanceResponse;
import com.wccgroup.challenge.domain.model.Postcode;
import com.wccgroup.challenge.service.IDistanceService;
import com.wccgroup.challenge.service.IPostcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

@RestController
public class PostcodeController {


    @Autowired
    IPostcodeService postcodeService;

    @Autowired
    IDistanceService distanceService;


    @GetMapping("/postcodedistance")
    @ResponseBody
    public ResponseEntity<DistanceResponse> getDistanceById(@RequestParam String pc1, @RequestParam String pc2) {

        Postcode postcode1 = postcodeService.getPostcode(pc1);
        Postcode postcode2 = postcodeService.getPostcode(pc2);
        if (postcode1 == null || postcode2 == null) {
            //TODO: i8n
            return ResponseEntity.badRequest().body(new DistanceResponse("Postcode doesnt exist"));
        } else {
            return ResponseEntity.ok().body(new DistanceResponse(
                    distanceService.calculateDistance(postcode1.coordinate.latitude, postcode1.coordinate.longitude,
                            postcode2.coordinate.latitude, postcode2.coordinate.longitude)
            ));
        }
    }

    //FIXME: still only getting same values as sync version
    @GetMapping("/postcodedistanceasync")
    @ResponseBody
    public Mono<ResponseEntity<DistanceResponse>> getDistanceByIdAsync(@RequestParam String pc1, @RequestParam String pc2) {
        CompletableFuture<Postcode> postcode1Async = postcodeService.getPostcodeAsync(pc1);
        CompletableFuture<Postcode> postcode2Async = postcodeService.getPostcodeAsync(pc2);

        BiFunction<CompletableFuture<Postcode>, CompletableFuture<Postcode>, CompletableFuture<Double>> combineToList =
                (p1, p2) -> {
                    Postcode postcode1 = null;
                    Postcode postcode2 = null;
                    //FIXME: BAD
                    try {
                        postcode1 = p1.get();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    try {
                        postcode2 = p2.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (postcode1 == null || postcode2 == null) {
                        //TODO: i8n
                        return CompletableFuture.completedFuture(null);
                    } else {
                        return CompletableFuture.completedFuture(distanceService.calculateDistance(postcode1.coordinate.latitude, postcode1.coordinate.longitude,
                                postcode2.coordinate.latitude, postcode2.coordinate.longitude));

                    }
                };
        //FIXME: Mono drops null values so not gonna get 400
        return Mono.fromCompletionStage(combineToList.apply(postcode1Async, postcode2Async))
                .map(result -> {
                    String verbose = null;
                    if (result == null) {
                        verbose = "Postcode doesnt exist";
                    }
                    return ResponseEntity.ok(new DistanceResponse(verbose, result));
                });

    }


    @PostMapping(path = "/calculateDistance", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Double getRawDistance(@RequestBody DistanceRequest distanceRequest) {
        return distanceService.calculateDistance(distanceRequest.coord1.latitude, distanceRequest.coord1.longitude,
                distanceRequest.coord2.latitude, distanceRequest.coord2.longitude);
    }

}
