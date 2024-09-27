package com.example.toprak_rehberi.dto;

import com.example.toprak_rehberi.entities.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LandDto {
    private Long id;
    private String landType;
    private Long cityId;
    private Long districtId;
    private Long neighbourhoodId;
    private String cityName;
    private String districtName;
    private String neighbourhoodName;
    private String username;
    private Double area;
    private String ada_parcel;

    public LandDto() {}

    public LandDto(Long id, String landType, Long cityId, Long districtId, Long neighbourhoodId, String cityName, String districtName, String neighbourhoodName, String username, Double area, String ada_parcel) {
        this.id = id;
        this.landType = landType;
        this.cityId = cityId;
        this.districtId = districtId;
        this.neighbourhoodId = neighbourhoodId;
        this.cityName = cityName;
        this.districtName = districtName;
        this.neighbourhoodName = neighbourhoodName;
        this.username = username;
        this.area = area;
        this.ada_parcel = ada_parcel;
    }
}

