package com.example.toprak_rehberi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NeighbourhoodDto {
    private Long id;
    private String name;


    public NeighbourhoodDto() {
    }

    public NeighbourhoodDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
