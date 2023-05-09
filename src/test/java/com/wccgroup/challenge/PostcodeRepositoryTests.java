package com.wccgroup.challenge;

import com.wccgroup.challenge.domain.dao.PostcodeRepository;
import com.wccgroup.challenge.domain.model.Coordinate;
import com.wccgroup.challenge.domain.model.Postcode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostcodeRepositoryTests {
    @Autowired
    PostcodeRepository postcodeRepository;

    @Disabled
    @Test
    void insertPostcode() {
        //FIXME: not flushing
        //FIXME: get next sequence value
        Postcode pc1 = new Postcode("TEST TEST", 1.0, 1.0);
        postcodeRepository.saveAndFlush(pc1);
        Postcode pc2 = postcodeRepository.getReferenceByPostcode("TEST TEST");
        assert (pc2.postcode.equals(pc1.postcode));
        assert (pc2.coordinate.equals(pc1.coordinate));
    }

    @Disabled
    @Test
    void updatePostcode() {
        //FIXME: not flushing
        //FIXME: get next sequence value
        String postcodeLabel = "AB10 1XG";
        Coordinate coordinate = new Coordinate(1.0, 1.0);
        Postcode pc1 = postcodeRepository.getReferenceByPostcode(postcodeLabel);
//        int res = postcodeRepository.updatePostcodeCoordinates(pc1.postcode, pc1.coordinate);
        pc1.coordinate = coordinate;
        Postcode pc2 = postcodeRepository.saveAndFlush(pc1);

        assert (pc2.postcode.equals(pc1.postcode));
        assert (pc2.coordinate.equals(coordinate));
    }
}
