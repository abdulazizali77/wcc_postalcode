package com.wccgroup.challenge.service;

import com.wccgroup.challenge.domain.model.Postcode;

public interface IPostcodeService {
    Postcode getPostcode(String id);
    boolean addPostcode(Postcode postcode);
    boolean setPostcode(Postcode postcode);
}
