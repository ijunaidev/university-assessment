package com.genesis.university.controller;

import com.genesis.university.dto.UniversityDTO;
import com.genesis.university.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/university")
public class UniversityController {

    @Autowired
    UniversityService universityService;

    // http://localhost:8085/api/v1/university/getByCountry?country=Pakistan
    @GetMapping("/getByCountry")
    public List<UniversityDTO> getUniversitiesByCountry(@RequestParam String country) {
        return universityService.getUniversities(country);
    }
}
