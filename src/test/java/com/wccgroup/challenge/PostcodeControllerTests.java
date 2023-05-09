package com.wccgroup.challenge;

import com.wccgroup.challenge.domain.model.Coordinate;
import com.wccgroup.challenge.domain.model.DistanceResponse;
import com.wccgroup.challenge.domain.model.Postcode;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.Isolated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.util.Locale;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostcodeControllerTests {
    //FIXME: should probably do before and after to clean up the db

    @Value(value = "${local.server.port}")
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    MessageSource messageSource;

    private HttpHeaders getRequestHeaders(String user, String pass) {
        return getRequestHeaders(user, pass, Locale.ENGLISH);
    }

    private HttpHeaders getRequestHeaders(String user, String pass, Locale locale) {
        String plainCreds = new StringBuilder().append(user).append(":").append(pass).toString();
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        headers.add("Accept-Language", locale.toLanguageTag());
        return headers;
    }

    @Test
    void testDistanceSuccess() {
        //TODO: templatize
        String pc1 = "AB16 6SZ";
        String pc2 = "AB21 0AL";
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        URI uri = factory.uriString("http://localhost:" + port + "/postcodedistance?pc1={pc1}&pc2={pc2}").build(pc1, pc2);

        HttpEntity<Void> request = new HttpEntity<>(getRequestHeaders("user1", "user1Pass"));
        DistanceResponse distanceResponse = restTemplate.exchange(uri, HttpMethod.GET, request, DistanceResponse.class).getBody();
        assert (distanceResponse.getDistance() == 11.62191361776278);
    }

    @Test
    void testDistanceFailureNoParam() {
        //same as doesnt exist because its string
        String pc1 = "AB16 6SZ";
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        URI uri = factory.uriString("http://localhost:" + port + "/postcodedistance?pc1={pc1}").build(pc1);

        //FIXME: need to get result code
        HttpEntity<Void> request = new HttpEntity<>(getRequestHeaders("user1", "user1Pass"));
        DistanceResponse distanceResponse = restTemplate.exchange(uri, HttpMethod.GET, request, DistanceResponse.class).getBody();
        assert (distanceResponse.getDistance() == null);
        assert (distanceResponse.getVerbose() == null);
    }

    @Test
    void testDistanceFailureParamInvalidValue() {
        //same as doesnt exist because its string
        String pc1 = "AB16 6SZ";
        String pc2 = "12345678";
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        URI uri = factory.uriString("http://localhost:" + port + "/postcodedistance?pc1={pc1}&pc2={pc2}").build(pc1, pc2);

        HttpEntity<Void> request = new HttpEntity<>(getRequestHeaders("user1", "user1Pass"));
        DistanceResponse distanceResponse = restTemplate.exchange(uri, HttpMethod.GET, request, DistanceResponse.class).getBody();
        assert (distanceResponse.getDistance() == null);
        assert (messageSource.getMessage("message.error.postcode.doesnotexist", null, Locale.ENGLISH).equals(distanceResponse.getVerbose()));
    }

    @Test
    void testDistanceFailurePostcodeDoesntExist() {
        String pc1 = "AB16 6SZ";
        String pc2 = "NULL";
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        URI uri = factory.uriString("http://localhost:" + port + "/postcodedistance?pc1={pc1}&pc2={pc2}").build(pc1, pc2);

        HttpEntity<Void> request = new HttpEntity<>(getRequestHeaders("user1", "user1Pass"));
        DistanceResponse distanceResponse = restTemplate.exchange(uri, HttpMethod.GET, request, DistanceResponse.class).getBody();
        assert (distanceResponse.getDistance() == null);
        assert (messageSource.getMessage("message.error.postcode.doesnotexist", null, Locale.ENGLISH).equals(distanceResponse.getVerbose()));
    }

    @Test
    void testDistanceFailureAntipode() {
    }

    @Test
    void testAddSuccess() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        URI uri = factory.uriString("http://localhost:" + port + "/addpostcode").build();

        Postcode p = new Postcode("X", new Coordinate(1.0d, 2.0d));
        HttpEntity<Postcode> request = new HttpEntity(p, getRequestHeaders("admin1", "admin1Pass"));
        Postcode postcode = restTemplate.exchange(uri, HttpMethod.POST, request, Postcode.class).getBody();
        assert (postcode.postcode.equals("X"));
        assert (postcode.coordinate.getLatitude().equals(1.0d));
        assert (postcode.coordinate.getLongitude().equals(2.0d));
    }

    @Test
    void testAddFailDuplicate() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        URI uri = factory.uriString("http://localhost:" + port + "/addpostcode").build();

        Postcode p = new Postcode("AB10 1XG", new Coordinate(1.0d, 2.0d));
        HttpEntity<Postcode> request = new HttpEntity(p, getRequestHeaders("admin1", "admin1Pass"));
        String result = restTemplate.exchange(uri, HttpMethod.POST, request, String.class).getBody();
        assert (result.equals(messageSource.getMessage("message.error.postcode.exists", null, Locale.ENGLISH)));
    }

    //FIXME: fails with others passes by itself
    @Disabled
    @Test
    void testAddFailEmptyCoordinates() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        URI uri = factory.uriString("http://localhost:" + port + "/addpostcode").build();

        Postcode p = new Postcode("X");
        HttpEntity<Postcode> request = new HttpEntity(p, getRequestHeaders("admin1", "admin1Pass"));
        String result = restTemplate.exchange(uri, HttpMethod.POST, request, String.class).getBody();
        System.out.println("testAddFailEmptyCoordinates result = " +result);
        assert (result.equals(messageSource.getMessage("message.error.coordinates.null", null, Locale.ENGLISH)));
    }

    @Test
    void testUpdateSuccess() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        URI uri = factory.uriString("http://localhost:" + port + "/updatepostcode").build();

        //TODO:templatize
        String pcStr = "AB10 1XG";
        Postcode p = new Postcode(pcStr, new Coordinate(1.0d, 2.0d));
        HttpEntity<Postcode> request = new HttpEntity(p, getRequestHeaders("admin1", "admin1Pass"));
        Postcode postcode = restTemplate.exchange(uri, HttpMethod.PUT, request, Postcode.class).getBody();
        assert (postcode.postcode.equals(pcStr));
        assert (postcode.coordinate.getLatitude().equals(1.0d));
        assert (postcode.coordinate.getLongitude().equals(2.0d));
    }

    //FIXME: fails with others passes by itself
    @Disabled
    @Test
    void testUpdateFailDoesntExist() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        URI uri = factory.uriString("http://localhost:" + port + "/updatepostcode").build();

        Postcode p = new Postcode("X");
        HttpEntity<Postcode> request = new HttpEntity(p, getRequestHeaders("admin1", "admin1Pass"));
        String result = restTemplate.exchange(uri, HttpMethod.PUT, request, String.class).getBody();
        System.out.println("testUpdateFailDoesntExist result = " +result);
        assert (result.equals(messageSource.getMessage("message.error.postcode.doesnotexist", null, Locale.ENGLISH)));
    }

    @Test
    void testUpdateFailEmptyCoordinates() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        URI uri = factory.uriString("http://localhost:" + port + "/updatepostcode").build();

        Postcode p = new Postcode("AB10 1XG");
        HttpEntity<Postcode> request = new HttpEntity(p, getRequestHeaders("admin1", "admin1Pass"));
        String result = restTemplate.exchange(uri, HttpMethod.PUT, request, String.class).getBody();
        assert (result.equals(messageSource.getMessage("message.error.coordinates.null", null, Locale.ENGLISH)));
    }

    //TODO: implement list tests
    //TODO: implement list scrolling tests
    //TODO: implement i18n tests
    //TODO: implement load testing
}
