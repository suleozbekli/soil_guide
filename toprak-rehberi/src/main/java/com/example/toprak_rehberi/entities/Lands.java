package com.example.toprak_rehberi.entities;

import com.example.toprak_rehberi.LandType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "lands")
public class Lands {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "land_type")
    private LandType landType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "neighbourhood_id")
    private Neighbourhood neighbourhood;

    @Column(name = "area")
    private Double area;

    @Column(name = "ada_parcel")
    private String adaParcel;

    @OneToMany(mappedBy = "land", cascade = CascadeType.ALL)
    private List<Crop> crops;

    public Lands(Long id, User user, LandType landType, Neighbourhood neighbourhood, List<Crop> crops, Double area, String adaParcel) {
        this.id = id;
        this.user = user;
        this.landType = landType;
        this.neighbourhood = neighbourhood;
        this.crops = crops;
        this.area = area;
        this.adaParcel = adaParcel;
    }

    public Lands() {
    }

    public Lands(Long id) {
        this.id = id;
    }
}
