package com.genesis.university.service;

import com.genesis.university.dto.UniversityDTO;
import com.genesis.university.model.University;
import com.genesis.university.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    @Value("${university.base-url}")
    private String universityApiBaseUrl;

    public List<UniversityDTO> getUniversitiesByCountry(String country) {
        // If found in local database, return List from local database
        if(universityRepository.existsByCountry(country)) {
            List<University> universityList = universityRepository.findAllByCountry(country);
            return populateUniversityDTOList(universityList);
        }

        // If not found in local database, make a call to external API to fetch and persist in database
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder url = UriComponentsBuilder.fromUriString(universityApiBaseUrl + "/search")
                .queryParam("country", country);

        ResponseEntity<List<UniversityDTO>> response = restTemplate.exchange(
                url.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

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

    private List<UniversityDTO> populateUniversityDTOList(List<University> universityList) {
        List<UniversityDTO> universityDTOList = new ArrayList<>();
        for(University university : universityList) {
            UniversityDTO universityDTO = convertUniversityIntoUniversityDTO(university);
            universityDTOList.add(universityDTO);
        }
        return universityDTOList;
    }

    public List<University> populateUniversityList(List<UniversityDTO> universityDTOList) {
        List<University> universityList = new ArrayList<>();
        for(UniversityDTO universityDTO : universityDTOList) {
            University university = new University();
            universityList.add(convertUniversityDTOIntoUniversity(universityDTO, university));
        }
        return universityList;
    }

    public University convertUniversityDTOIntoUniversity(UniversityDTO universityDTO, University university) {
        university.setAlphaTwoCode(universityDTO.getAlphaTwoCode());
        university.setWebPages(universityDTO.getWebPages() != null ? String.join(",", universityDTO.getWebPages()) : null);
        university.setStateProvince(universityDTO.getStateProvince());
        university.setName(universityDTO.getName());
        university.setDomains(universityDTO.getDomains() != null ? String.join(",", universityDTO.getDomains()) : null);
        university.setCountry(universityDTO.getCountry());

        return university;
    }

    public UniversityDTO convertUniversityIntoUniversityDTO(University university) {
        return UniversityDTO.builder()
                .alphaTwoCode(university.getAlphaTwoCode())
                .webPages(university.getWebPages().isEmpty() ? new ArrayList<>() : List.of(university.getWebPages().split(",")))
                .stateProvince(university.getStateProvince())
                .name(university.getName())
                .domains(university.getDomains().isEmpty() ? new ArrayList<>() : List.of(university.getDomains().split(",")))
                .country(university.getCountry())
                .build();
    }

    private void persistDataInDb(List<UniversityDTO> universityDTOList) {
        List<University> universityList = populateUniversityList(universityDTOList);

        int batchSize = 100;
        for (int i = 0; i < universityList.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, universityList.size());
            List<University> batch = universityList.subList(i, endIndex);
            universityRepository.saveAll(batch);
        }
    }

    public void updateUniversity(String country, Long id, UniversityDTO university) {
        University existingUniversity = universityRepository.findByCountryAndUniversityId(country, id);
        if (existingUniversity != null) {
            convertUniversityDTOIntoUniversity(university, existingUniversity);
            universityRepository.save(existingUniversity);
        }
    }

    public boolean existsByCountryAndUniversityId(String country, Long id) {
        return universityRepository.existsByCountryAndUniversityId(country, id);
    }

    public UniversityDTO findByName(String name) {
        University university = universityRepository.findByName(name);
        return convertUniversityIntoUniversityDTO(university);
    }
}
