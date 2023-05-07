package com.wccgroup.challenge.domain.dao;

import com.wccgroup.challenge.domain.model.Postcode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostcodeRepository extends JpaRepository<Postcode, Long> {
    @Override
    Postcode getReferenceById(Long aLong);

    Postcode getReferenceByPostcode(String postcode);


}
