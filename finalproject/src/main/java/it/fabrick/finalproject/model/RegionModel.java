package it.fabrick.finalproject.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RegionModel {

    @JsonAlias({"regione"})
    private String name;

    public void setName(String name) {
        this.name = name.toLowerCase();
    }
}
