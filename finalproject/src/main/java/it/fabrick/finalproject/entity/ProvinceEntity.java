package it.fabrick.finalproject.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "province")
@Table(name = "province")
public class ProvinceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "acronym", length = 2, nullable = false)
    private String acronym;

    @OneToMany(mappedBy = "province", fetch = FetchType.EAGER)
    private List<MunicipalityEntity> municipalities;
}
