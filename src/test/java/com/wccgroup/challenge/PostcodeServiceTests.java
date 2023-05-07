package com.wccgroup.challenge;

import com.wccgroup.challenge.domain.dao.PostcodeRepository;
import com.wccgroup.challenge.domain.model.Postcode;
import com.wccgroup.challenge.service.PostcodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostcodeServiceTests {

    @Autowired
    PostcodeService postcodeService;

    @Test
    void getSuccess() {
        //'AB16 7NX', '57.168438','-2.161636'
        Postcode pc = postcodeService.getPostcode("AB16 7NX");
        assert (pc.coordinate.latitude == 57.168438);
        assert (pc.coordinate.longitude == -2.161636);
    }

    @Test
    void getFailure() {
        Postcode pc = postcodeService.getPostcode("NULL");
        assert (pc == null);
    }
}
