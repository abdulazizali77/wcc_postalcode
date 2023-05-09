package com.wccgroup.challenge.domain.model;

public enum Units {
    KM("km", 1.0),
    M("m", 10.0),
    CM("cm", 100.0),
    MM("mm", 1000.0);

    public final String label;
    public final Double multiplier;

    Units(String label, Double multiplier) {
        this.label = label;
        this.multiplier = multiplier;
    }

    //UGLY!
    static public Units getUnit(String unit){
        if(unit == null)
            return KM;
        switch(unit){
            case "m":
                return M;
            case "cm":
                return CM;
            case "mm":
                return MM;
            default:
            case "km":
                return KM;
        }
    }
}
