package it.fabrick.finalproject.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MunicipalityModel {

    private Integer id;

    @JsonAlias({"comune"})
    private String name;

    @JsonAlias({"num_residenti"})
    private int numberOfResident;

    @JsonAlias({"lat"})
    private Float latitude;

    @JsonAlias({"lng"})
    private Float longitude;

    @JsonAlias({"regione"})
    private String regionInternal;

    @JsonAlias({"provincia"})
    private String provinceInternal;

    @JsonAlias({"istat"})
    private Integer istat;

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public void setRegionInternal(String regionInternal) {
        this.regionInternal = regionInternal.toLowerCase();
    }

    public void setProvinceInternal(String provinceInternal) {
        this.provinceInternal = provinceInternal.toLowerCase();
    }
}
