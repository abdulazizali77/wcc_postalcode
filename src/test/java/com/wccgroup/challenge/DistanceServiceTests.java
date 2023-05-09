package com.wccgroup.challenge;

import com.wccgroup.challenge.service.IDistanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DistanceServiceTests {
    @Autowired
    IDistanceService distanceService;

    @Test
    void basicTests() {
        Double res = distanceService.calculateDistance(3.0, 100.0, 4.0, 100.0);
        //FIXME: how much?
        //TODO: load a csv somewhere
        assert(res == 111.19492664455872);
        //assertThat(res, )
    }

    @Test
    void antipodeTest(){
        //TODO:implement
    }
    @Test
    void invalidValues(){
        //TODO:implement
    }
}
