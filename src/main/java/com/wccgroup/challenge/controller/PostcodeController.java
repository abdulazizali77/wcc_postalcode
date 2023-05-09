package com.wccgroup.challenge.controller;

import com.wccgroup.challenge.domain.dao.PostcodeRepository;
import com.wccgroup.challenge.domain.model.DistanceRequest;
import com.wccgroup.challenge.domain.model.DistanceResponse;
import com.wccgroup.challenge.domain.model.Postcode;
import com.wccgroup.challenge.domain.model.Units;
import com.wccgroup.challenge.service.IDistanceService;
import com.wccgroup.challenge.service.IPostcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.servlet.annotation.ServletSecurity;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

@RestController
public class PostcodeController {
    @Autowired
    MessageSource messageSource;

    @Autowired
    IPostcodeService postcodeService;

    @Autowired
    IDistanceService distanceService;

    @Autowired
    PostcodeRepository postcodeRepository;

    //TODO: should cache/memoize requests

    @GetMapping("/postcodedistance")
    @ResponseBody
    public ResponseEntity<DistanceResponse> getDistanceById(final HttpServletRequest request,
                                                            @RequestParam String pc1, @RequestParam String pc2,
                                                            @RequestParam(required = false) String unit) {

        Postcode postcode1 = postcodeService.getPostcode(pc1);
        Postcode postcode2 = postcodeService.getPostcode(pc2);
        Units convertedUnit = Units.getUnit(unit);
        if (postcode1 == null || postcode2 == null) {
            //TODO: templatize messages
            return ResponseEntity.badRequest().body(new DistanceResponse(messageSource.getMessage("message.error.postcode.doesnotexist", null, request.getLocale())));
        } else {
            Double v = distanceService.calculateDistance(postcode1.coordinate.getLatitude(), postcode1.coordinate.getLongitude(),
                    postcode2.coordinate.getLatitude(), postcode2.coordinate.getLongitude()) * convertedUnit.multiplier;
            return ResponseEntity.ok().body(new DistanceResponse(null, postcode1, postcode2, v, convertedUnit.label));
        }
    }

    @PostMapping("/addpostcode")
    public ResponseEntity addPostcode(final HttpServletRequest request, @RequestBody Postcode postcode) {
        //FIXME: duplicates
        //FIXME: empty coordinates
        //FIXME: flushing perf hit

        Postcode pc = postcodeRepository.getReferenceByPostcode(postcode.postcode);
        //TODO: templatize messages
        if (pc != null) {
            return ResponseEntity.badRequest().body(messageSource.getMessage("message.error.postcode.exists", null, request.getLocale()));
        } else if (postcode.coordinate == null) {
            return ResponseEntity.badRequest().body(messageSource.getMessage("message.error.coordinates.null", null, request.getLocale()));
        } else {
            return ResponseEntity.ok().body(postcodeRepository.saveAndFlush(postcode));
        }
    }

    @PutMapping("/updatepostcode")
    public ResponseEntity updatePostcode(final HttpServletRequest request, @RequestBody Postcode postcode) {
        Postcode pc = postcodeRepository.getReferenceByPostcode(postcode.postcode);
        if (pc == null) {
            return ResponseEntity.badRequest().body(messageSource.getMessage("message.error.postcode.doesnotexist", null, request.getLocale()));
        } else if (postcode.coordinate == null) {
            return ResponseEntity.badRequest().body(messageSource.getMessage("message.error.coordinates.null", null, request.getLocale()));
        } else {
            //FIXME: BAD
            //TODO: templatize messages
            pc.coordinate = postcode.coordinate;
            postcodeRepository.saveAndFlush(pc);
            return ResponseEntity.ok().body(pc);
        }
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<Postcode>> getAllPostcodes() {
        return ResponseEntity.ok().body(postcodeRepository.findAll());
    }

    //FIXME: still only getting same values as sync version!
    @GetMapping("/postcodedistanceasync")
    @ResponseBody
    public Mono<ResponseEntity<DistanceResponse>> getDistanceByIdAsync(final HttpServletRequest request, @RequestParam String pc1, @RequestParam String pc2) {
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
                        return CompletableFuture.completedFuture(distanceService.calculateDistance(postcode1.coordinate.getLatitude(), postcode1.coordinate.getLongitude(),
                                postcode2.coordinate.getLatitude(), postcode2.coordinate.getLongitude()));

                    }
                };
        //FIXME: Mono drops null values so not gonna get 400
        return Mono.fromCompletionStage(combineToList.apply(postcode1Async, postcode2Async))
                .map(result -> {
                    String verbose = null;
                    if (result == null) {
                        //TODO: templatize messages
                        verbose = messageSource.getMessage("message.error.postcode.doesnotexist", null, request.getLocale());
                    }
                    //FIXME: OMG bad
                    Postcode postcode1 = null;
                    Postcode postcode2 = null;
                    //FIXME: BAD
                    try {
                        postcode1 = postcode1Async.get();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    try {
                        postcode2 = postcode2Async.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    //FIXME: add units param
                    return ResponseEntity.ok(new DistanceResponse(verbose,
                            postcode1, postcode2,
                            result, Units.KM.label));
                });

    }


    @PostMapping(path = "/calculateDistance", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Double getRawDistance(@RequestBody DistanceRequest distanceRequest) {
        return distanceService.calculateDistance(distanceRequest.coord1.getLatitude(), distanceRequest.coord1.getLongitude(),
                distanceRequest.coord2.getLatitude(), distanceRequest.coord2.getLongitude());
    }

}
