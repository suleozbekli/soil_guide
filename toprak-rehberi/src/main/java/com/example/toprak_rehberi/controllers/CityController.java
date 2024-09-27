package com.example.toprak_rehberi.controllers;

import com.example.toprak_rehberi.dto.CityDto;
import com.example.toprak_rehberi.dto.DistrictDto;
import com.example.toprak_rehberi.dto.NeighbourhoodDto;
import com.example.toprak_rehberi.entities.City;
import com.example.toprak_rehberi.entities.District;
import com.example.toprak_rehberi.entities.Neighbourhood;
import com.example.toprak_rehberi.services.CityService;
import com.example.toprak_rehberi.services.DistrictService;
import com.example.toprak_rehberi.services.NeighbourhoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private NeighbourhoodService neighbourhoodService;

    @PostMapping("/add-with-districts")
    public ResponseEntity<CityDto> addCityWithDistricts(@RequestBody City city) {
        City savedCity = cityService.saveCity(city);

        for (District district : city.getDistricts()) {
            district.setCity(savedCity);
            District savedDistrict = districtService.saveDistrict(district);

            for (Neighbourhood neighbourhood : district.getNeighbourhoods()) {
                neighbourhood.setDistrict(savedDistrict);
                neighbourhoodService.saveNeighbourhood(neighbourhood);
            }
        }

        // Map savedCity to CityDTO
        CityDto cityDTO = new CityDto();
        cityDTO.setId(savedCity.getId());
        cityDTO.setName(savedCity.getName());
        cityDTO.setDistricts(savedCity.getDistricts().stream()
                .map(district -> {
                    DistrictDto districtDTO = new DistrictDto();
                    districtDTO.setId(district.getId());
                    districtDTO.setName(district.getName());
                    districtDTO.setNeighbourhoods(district.getNeighbourhoods().stream()
                            .map(neighbourhood -> new NeighbourhoodDto(neighbourhood.getId(), neighbourhood.getName()))
                            .collect(Collectors.toList()));
                    return districtDTO;
                })
                .collect(Collectors.toList()));

        return ResponseEntity.status(HttpStatus.CREATED).body(cityDTO);
    }
    @PutMapping("/add-with-districts")
    public ResponseEntity<CityDto> addOrUpdateCityWithDistricts(@RequestBody City city) {
        City existingCity = cityService.findByName(city.getName());

        if (existingCity != null) {
            existingCity.setDistricts(city.getDistricts());
            city = existingCity;
        }

        City savedCity = cityService.saveCity(city);

        for (District district : city.getDistricts()) {
            district.setCity(savedCity);
            District savedDistrict = districtService.saveDistrict(district);

            for (Neighbourhood neighbourhood : district.getNeighbourhoods()) {
                neighbourhood.setDistrict(savedDistrict);
                neighbourhoodService.saveNeighbourhood(neighbourhood);
            }
        }

        // Map savedCity to CityDTO
        CityDto cityDTO = new CityDto();
        cityDTO.setId(savedCity.getId());
        cityDTO.setName(savedCity.getName());
        cityDTO.setDistricts(savedCity.getDistricts().stream()
                .map(district -> {
                    DistrictDto districtDTO = new DistrictDto();
                    districtDTO.setId(district.getId());
                    districtDTO.setName(district.getName());
                    districtDTO.setNeighbourhoods(district.getNeighbourhoods().stream()
                            .map(neighbourhood -> new NeighbourhoodDto(neighbourhood.getId(), neighbourhood.getName()))
                            .collect(Collectors.toList()));
                    return districtDTO;
                })
                .collect(Collectors.toList()));

        return ResponseEntity.status(HttpStatus.OK).body(cityDTO);
    }

    @GetMapping
    public ResponseEntity<List<CityDto>> getAllCities() {
        List<City> cities = cityService.findAllCities();
        List<CityDto> cityDTOs = cities.stream()
                .map(city -> {
                    CityDto dto = new CityDto();
                    dto.setId(city.getId());
                    dto.setName(city.getName());
                    dto.setDistricts(city.getDistricts().stream()
                            .map(district -> {
                                DistrictDto districtDTO = new DistrictDto();
                                districtDTO.setId(district.getId());
                                districtDTO.setName(district.getName());
                                districtDTO.setNeighbourhoods(district.getNeighbourhoods().stream()
                                        .map(neighbourhood -> new NeighbourhoodDto(neighbourhood.getId(), neighbourhood.getName()))
                                        .collect(Collectors.toList()));
                                return districtDTO;
                            })
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(cityDTOs);
    }
}
