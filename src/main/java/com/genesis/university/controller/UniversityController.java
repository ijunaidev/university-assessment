package com.genesis.university.controller;

import com.genesis.university.dto.UniversityDTO;
import com.genesis.university.service.UniversityService;
import com.genesis.university.utils.CountryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/university")
public class UniversityController {

    @Autowired
    UniversityService universityService;

    // http://localhost:8085/api/v1/university/getByCountry?country=Pakistan
    @GetMapping("/getByCountry")
    public List<UniversityDTO> getUniversitiesByCountry(@RequestParam(required = true) String country) {
        if (country != null && country.isEmpty()) {
            return Collections.emptyList();
        }

        country = CountryUtils.capitalizeFirstCharacterOfCountry(country);

        return universityService.getUniversitiesByCountry(country);
    }

    @PutMapping("/{country}/{id}")
    public ResponseEntity<String> updateUniversity(@PathVariable String country, @PathVariable Long id, @RequestBody UniversityDTO university) {
        if (country != null && !country.isEmpty()) {
            country = CountryUtils.capitalizeFirstCharacterOfCountry(country);
        }

        boolean universityExists = universityService.existsByCountryAndUniversityId(country, id);
        if (!universityExists) {
            return new ResponseEntity<>("University not found for the given country and ID", HttpStatus.NOT_FOUND);
        }

        universityService.updateUniversity(country, id, university);
        return new ResponseEntity<>("University updated successfully", HttpStatus.OK);
    }


}
