package com.wccgroup.challenge.service;

import com.wccgroup.challenge.domain.model.Postcode;
import org.springframework.stereotype.Service;

@Service
public class PostcodeService implements IPostcodeService{
    @Override
    public Postcode getPostcode(String id) {

        return new Postcode(id);
    }

    @Override
    public boolean addPostcode(Postcode postcode) {
        return false;
    }

    @Override
    public boolean setPostcode(Postcode postcode) {
        return false;
    }
}
