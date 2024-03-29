package com.genesis.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UniversityDTO {
    @JsonProperty("alpha_two_code")
    private String alphaTwoCode;
    @JsonProperty("web_pages")
    private List<String> webPages;
    @JsonProperty("state-province")
    private String stateProvince;
    private String name;
    private List<String> domains;
    private String country;
}
