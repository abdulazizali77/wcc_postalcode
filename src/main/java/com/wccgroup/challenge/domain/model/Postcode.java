package com.wccgroup.challenge.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "postcodelatlng")
public class Postcode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String Id;
    @Column(unique = true)
    public String postcode;
    @Embedded
    public Coordinate coordinate;


    public Postcode() {

    }
}
