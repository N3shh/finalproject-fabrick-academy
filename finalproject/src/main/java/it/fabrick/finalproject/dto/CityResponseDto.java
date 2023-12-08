package it.fabrick.finalproject.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CityResponseDto {

    private Integer id;

    private String name;

    private Integer numberOfPopulation;
}
