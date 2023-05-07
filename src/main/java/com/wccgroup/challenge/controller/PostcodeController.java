package com.wccgroup.challenge.controller;

import com.wccgroup.challenge.domain.model.DistanceRequest;
import com.wccgroup.challenge.domain.model.Postcode;
import com.wccgroup.challenge.service.IDistanceService;
import com.wccgroup.challenge.service.IPostcodeService;
import com.wccgroup.challenge.service.PostcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostcodeController {


    @Autowired
    IPostcodeService postcodeService;

    @Autowired
    IDistanceService distanceService;

    //TODO:
    @GetMapping("/postcodedistance")
    public Double getDistanceById(String pc1, String pc2) {
        //TODO: null check
        Postcode postcode1 = postcodeService.getPostcode(pc1);
        Postcode postcode2 = postcodeService.getPostcode(pc2);
        return distanceService.calculateDistance(postcode1.coordinate.latitude, postcode1.coordinate.longitude,
                postcode2.coordinate.latitude, postcode2.coordinate.longitude);
    }

    @PostMapping(path = "/calculateDistance", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Double getRawDistance(@RequestBody DistanceRequest distanceRequest
    ) {
        return distanceService.calculateDistance(distanceRequest.coord1.latitude, distanceRequest.coord1.longitude,
                distanceRequest.coord2.latitude, distanceRequest.coord2.longitude);
    }

}
