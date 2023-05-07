package com.wccgroup.challenge.service;

import com.wccgroup.challenge.domain.dao.PostcodeRepository;
import com.wccgroup.challenge.domain.model.Postcode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class PostcodeService implements IPostcodeService{
    @Autowired
    PostcodeRepository postcodeRepository;

    @Override
    @Nullable
    public Postcode getPostcode(String pcStr) {
        Postcode postcode = postcodeRepository.getReferenceByPostcode(pcStr);
        return postcode;
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
