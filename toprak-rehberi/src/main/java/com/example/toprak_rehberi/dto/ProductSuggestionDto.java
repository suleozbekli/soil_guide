package com.example.toprak_rehberi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSuggestionDto {
    private Long productId;
    private String name;
    private Double averageSuccessRate;

    public ProductSuggestionDto(Long productId, String name, Double averageSuccessRate) {
        this.productId = productId;
        this.name = name;
        this.averageSuccessRate = averageSuccessRate;
    }
}
