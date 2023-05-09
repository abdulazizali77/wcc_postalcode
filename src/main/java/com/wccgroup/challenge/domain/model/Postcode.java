package com.wccgroup.challenge.domain.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "postcodelatlng")
public class Postcode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long Id;
    @Column(unique = true)
    public String postcode;
    @Embedded
    public Coordinate coordinate;


    public Postcode() {
    }

    public Postcode(String postcode, Coordinate coordinate) {
    }

    public Postcode(String postcode, Double longitude, Double latitude) {
    }

}
