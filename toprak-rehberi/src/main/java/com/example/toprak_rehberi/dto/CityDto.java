package com.example.toprak_rehberi.dto;
import java.util.List;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CityDto {

    private Long id;
    private String name;
    private List<DistrictDto> districts;

    public CityDto(Long id, String name, List<DistrictDto> districts) {
        this.id = id;
        this.name = name;
        this.districts = districts;
    }

    public CityDto() {
    }
}