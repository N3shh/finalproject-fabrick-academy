package it.fabrick.finalproject.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MunicipalityResponseDto {

    private Integer id;

    private String name;

    private int numberOfResident;

}
