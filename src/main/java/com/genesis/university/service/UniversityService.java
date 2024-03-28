package com.genesis.university.service;

import com.genesis.university.dto.UniversityDTO;
import com.genesis.university.model.University;
import com.genesis.university.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;
    private static final String API_URL = "http://universities.hipolabs.com/search?country=";

    public List<UniversityDTO> getUniversities(String country) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<UniversityDTO>> response = restTemplate.exchange(
                API_URL + country,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UniversityDTO>>() {});

        if (response.getStatusCode() == HttpStatus.OK) {
            List<UniversityDTO> responseBody = response.getBody();
            if (responseBody != null && !responseBody.isEmpty()) {
                persistDataInDb(responseBody);
            }
            return responseBody;
        } else {
            return Collections.emptyList();
        }
    }

    private void persistDataInDb(List<UniversityDTO> universityDTOList) {
        List<University> universityList = new ArrayList<>();
        for(UniversityDTO universityDTO : universityDTOList) {
            University university = new University();

            university.setAlphaTwoCode(universityDTO.getAlphaTwoCode());
            university.setWebPages(universityDTO.getWebPages() != null ? String.join(",", universityDTO.getWebPages()) : null);
            university.setStateProvince(universityDTO.getStateProvince());
            university.setName(universityDTO.getName());
            university.setDomains(universityDTO.getDomains() != null ? String.join(",", universityDTO.getDomains()) : null);
            university.setCountry(universityDTO.getCountry());

            universityList.add(university);
        }

        int batchSize = 100;
        for (int i = 0; i < universityList.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, universityList.size());
            List<University> batch = universityList.subList(i, endIndex);
            universityRepository.saveAll(batch);
        }
    }
}
