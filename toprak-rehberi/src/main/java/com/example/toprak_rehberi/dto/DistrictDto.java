package com.example.toprak_rehberi.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter

public class DistrictDto {
    private Long id;
    private String name;
    private List<NeighbourhoodDto> neighbourhoods;

    public DistrictDto() {
    }
    public DistrictDto(Long id, String name, List<NeighbourhoodDto> neighbourhoods) {
        this.id = id;
        this.name = name;
        this.neighbourhoods = neighbourhoods;
    }
}

