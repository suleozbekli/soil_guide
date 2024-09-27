package com.example.toprak_rehberi.entities;

import jakarta.persistence.*;
import com.example.toprak_rehberi.HarvestQuality;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "harvest")
public class Harvest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "land_id", nullable = false)
    private Lands land;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "harvest_date", nullable = false)
    private LocalDate harvestDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "harvest_quality", nullable = false)
    private HarvestQuality harvestQuality;



    public Harvest(Lands land, Product product, LocalDate harvestDate, HarvestQuality harvestQuality, Double harvestQuantity) {
        this.land = land;
        this.product = product;
        this.harvestDate = harvestDate;
        this.harvestQuality = harvestQuality;

    }

    public Harvest() {
    }

    public Harvest(Long id, Lands land, Product product, User user, LocalDate harvestDate, HarvestQuality harvestQuality, Double harvestQuantity) {
        this.id = id;
        this.land = land;
        this.product = product;
        this.user = user;
        this.harvestDate = harvestDate;
        this.harvestQuality = harvestQuality;

    }
}
