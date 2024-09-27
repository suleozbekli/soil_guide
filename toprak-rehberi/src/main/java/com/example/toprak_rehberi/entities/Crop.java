package com.example.toprak_rehberi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "land_id", nullable = false)  // Bu, 'Lands' entity'sindeki id'yi referans alır
    private Lands land;

    @ManyToOne
    @JoinColumn(name = "product_id")  // Bu, 'Product' entity'sindeki id'yi referans alır
    private Product product;

    private double areaInSquareMeters;
    @Column(name = "planting_date")
    private LocalDate plantingDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Crop() {
    }

    public Crop(Long id, Lands land, Product product, double areaInSquareMeters, LocalDate plantingDate, User user) {
        this.id = id;
        this.land = land;
        this.product = product;
        this.areaInSquareMeters = areaInSquareMeters;
        this.plantingDate = plantingDate;
        this.user = user;
    }
}
