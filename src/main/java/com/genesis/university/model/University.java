package com.genesis.university.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "university_details")
@Data
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String alphaTwoCode;
    private String webPages;
    private String stateProvince;
    private String name;
    private String domains;
    private String country;
}
