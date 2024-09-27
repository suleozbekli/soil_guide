package com.example.toprak_rehberi.controllers;

import com.example.toprak_rehberi.dto.DistrictDto;
import com.example.toprak_rehberi.dto.NeighbourhoodDto;
import com.example.toprak_rehberi.entities.District;
import com.example.toprak_rehberi.services.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/districts")
public class DistrictController {

    @Autowired
    private DistrictService districtService;

    @GetMapping("/byCity/{cityId}")
    public ResponseEntity<List<DistrictDto>> getDistrictsByCity(@PathVariable Long cityId) {
        System.out.println("Received cityId: " + cityId);
        List<District> districts = districtService.findDistrictsByCityId(cityId);
        List<DistrictDto> districtDTOs = districts.stream()
                .map(district -> {
                    DistrictDto districtDTO = new DistrictDto();
                    districtDTO.setId(district.getId());
                    districtDTO.setName(district.getName());
                    districtDTO.setNeighbourhoods(district.getNeighbourhoods().stream()
                            .map(neighbourhood -> new NeighbourhoodDto(neighbourhood.getId(), neighbourhood.getName()))
                            .collect(Collectors.toList()));
                    return districtDTO;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(districtDTOs);
    }
}
