package com.example.toprak_rehberi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HarvestDto {

    private Long id;
    private Long landId;
    private Long productId;
    private LocalDate harvestDate;
    private String harvestQuality;
    private Long userId;
    @Setter
    @Getter
    private String landName;
    @Getter
    @Setter
    private String productName;

    public HarvestDto() {}

    public HarvestDto(Long id, Long landId, Long productId, LocalDate harvestDate, String harvestQuality, Long userId, String landName, String productName) {
        this.id = id;
        this.landId = landId;
        this.productId = productId;
        this.harvestDate = harvestDate;
        this.harvestQuality = harvestQuality;
        this.userId = userId;
        this.landName = landName;
        this.productName = productName;
    }
}
