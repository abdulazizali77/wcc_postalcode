package com.wccgroup.challenge;

import com.wccgroup.challenge.domain.model.DistanceResponse;
import com.wccgroup.challenge.domain.model.Postcode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostcodeControllerTests {
    @Value(value = "${local.server.port}")
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testSuccessBasicDistance() {
        //TODO: templatize
        String pc1 = "AB16 6SZ";
        String pc2 = "AB21 0AL";
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        URI uri = factory.uriString("http://localhost:" + port + "/postcodedistance?pc1={pc1}&pc2={pc2}").build(pc1, pc2);

        DistanceResponse distanceResponse = restTemplate.getForObject(uri, DistanceResponse.class);
        assert (distanceResponse.getDistance() == 11.62191361776278);
    }

    @Test
    void testFailureNoParam() {
        //same as doesnt exist because its string
        String pc1 = "AB16 6SZ";
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        URI uri = factory.uriString("http://localhost:" + port + "/postcodedistance?pc1={pc1}").build(pc1);

        //FIXME: need to get result code
        DistanceResponse distanceResponse = restTemplate.getForObject(uri, DistanceResponse.class);
        assert (distanceResponse.getDistance() == null);
        assert (distanceResponse.getVerbose() == null);
    }

    @Test
    void testFailureParamInvalidValue() {
        //same as doesnt exist because its string
        String pc1 = "AB16 6SZ";
        String pc2 = "12345678";
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        URI uri = factory.uriString("http://localhost:" + port + "/postcodedistance?pc1={pc1}&pc2={pc2}").build(pc1, pc2);

        DistanceResponse distanceResponse = restTemplate.getForObject(uri, DistanceResponse.class);
        assert (distanceResponse.getDistance() == null);
        assert ("Postcode doesnt exist".equals(distanceResponse.getVerbose()));
    }

    @Test
    void testFailurePostcodeDoesntExist() {
        String pc1 = "AB16 6SZ";
        String pc2 = "NULL";
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        URI uri = factory.uriString("http://localhost:" + port + "/postcodedistance?pc1={pc1}&pc2={pc2}").build(pc1, pc2);

        DistanceResponse distanceResponse = restTemplate.getForObject(uri, DistanceResponse.class);
        assert (distanceResponse.getDistance() == null);
        assert ("Postcode doesnt exist".equals(distanceResponse.getVerbose()));
    }

    @Test
    void testFailureAntipode() {
    }

}
