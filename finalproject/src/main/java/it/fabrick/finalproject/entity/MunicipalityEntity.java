package it.fabrick.finalproject.entity;


import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "municipality")
@Table(name = "municipality")
public class MunicipalityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 40, nullable = false)
    private String name;

    @Column(name = "resident_number", nullable = false)
    private int numberOfResident;

    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @Column(name = "longitude", nullable = false)
    private Float longitude;

    @ManyToOne
    @JoinColumn(nullable = false, name = "region_id", referencedColumnName = "id")
    private RegionEntity region;

    @ManyToOne
    @JoinColumn(nullable = false, name = "province_id", referencedColumnName = "id")
    private ProvinceEntity province;


}
