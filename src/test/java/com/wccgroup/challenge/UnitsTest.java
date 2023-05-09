package com.wccgroup.challenge;

import com.wccgroup.challenge.domain.model.Units;
import org.junit.jupiter.api.Test;
public class UnitsTest {

    @Test
    void testUnits() {
        String units1 = "km";
        String units2 = "m";
        String units3 = "cm";
        String units4 = "mm";
        Double value = 11.0;//11km
        assert(Units.getUnit(units1).label.equals(units1));
        assert((Units.getUnit(units1).multiplier * value) == 11.0);
        assert(Units.getUnit(units2).label.equals(units2));
        assert((Units.getUnit(units2).multiplier * value) == 110.0);
        assert(Units.getUnit(units3).label.equals(units3));
        assert((Units.getUnit(units3).multiplier * value) == 1100.0);
        assert(Units.getUnit(units4).label.equals(units4));
        assert((Units.getUnit(units4).multiplier * value) == 11000.0);
    }
}
