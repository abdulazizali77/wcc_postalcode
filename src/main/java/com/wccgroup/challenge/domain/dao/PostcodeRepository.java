package com.wccgroup.challenge.domain.dao;

import com.wccgroup.challenge.domain.model.Coordinate;
import com.wccgroup.challenge.domain.model.Postcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface PostcodeRepository extends JpaRepository<Postcode, Long> {
    Postcode getReferenceByPostcode(String postcode);

    //FIXME: not working as i expected, but well just leave this here
    @Transactional
    @Modifying
    @Query("UPDATE Postcode p SET p.coordinate = :coordinate WHERE p.postcode = :postcode")
    int updatePostcodeCoordinates(@Param("postcode") String postcode, @Param("coordinate") Coordinate coordinate);
}
