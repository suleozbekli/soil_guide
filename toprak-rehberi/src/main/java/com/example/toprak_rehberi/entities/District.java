package com.example.toprak_rehberi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "districts")
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "district")
    private List<Neighbourhood> neighbourhoods;
    public District() {
    }

    public District(Long id, String name, City city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public District(Long id, String name, City city, List<Neighbourhood> neighbourhoods) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.neighbourhoods = neighbourhoods;
    }
}
