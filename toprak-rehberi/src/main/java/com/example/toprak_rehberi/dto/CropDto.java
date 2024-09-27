package com.example.toprak_rehberi.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CropDto {

    private Long id;
    private Long landId;
    private String landName;
    private Long productId;
    private String productName;
    private double areaInSquareMeters;
    private LocalDate plantingDate;
    private Long userId;


    public CropDto() {
    }

    public CropDto(Long id, Long landId, Long productId, double areaInSquareMeters, LocalDate plantingDate,Long userId)
    {
        this.id = id;
        this.landId = landId;
        this.productId = productId;
        this.areaInSquareMeters = areaInSquareMeters;
        this.plantingDate = plantingDate;
        this.userId = userId;
    }

    public CropDto(Long id, Long landId, String landName, Long productId, String productName, double areaInSquareMeters, LocalDate plantingDate, Long userId) {
        this.id = id;
        this.landId = landId;
        this.landName = landName;
        this.productId = productId;
        this.productName = productName;
        this.areaInSquareMeters = areaInSquareMeters;
        this.plantingDate = plantingDate;
        this.userId = userId;
    }


}

