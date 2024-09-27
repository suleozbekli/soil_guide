package com.example.toprak_rehberi.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "neighbourhoods")
public class Neighbourhood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private District district;

    public Neighbourhood() {
    }

    public Neighbourhood(District district, String name, Long id) {
        this.district = district;
        this.name = name;
        this.id = id;
    }
}
