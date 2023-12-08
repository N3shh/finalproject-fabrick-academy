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
public class ProvinceModel {

    @JsonAlias({"provincia"})
    private String name;

    @JsonAlias({"sigla"})
    private String acronym;

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym.toLowerCase();
    }
}
