package it.fabrick.finalproject.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CityModel {

    private Integer id;

    @JsonAlias({"capoluogo"})
    private String name;

    @JsonAlias({"popolazione"})
    private Integer numberOfPopulation;

    @JsonAlias({"latitudine"})
    private Float latitude;

    @JsonAlias({"longitudine"})
    private Float longitude;

    public void setName(String name) {
        this.name = name.toLowerCase();
    }
}
