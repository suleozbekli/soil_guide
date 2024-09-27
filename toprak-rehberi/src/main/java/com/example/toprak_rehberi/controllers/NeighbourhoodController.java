package com.example.toprak_rehberi.controllers;

import com.example.toprak_rehberi.dto.NeighbourhoodDto;
import com.example.toprak_rehberi.services.NeighbourhoodService;
import com.example.toprak_rehberi.entities.Neighbourhood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/neighbourhoods")
public class NeighbourhoodController {

    @Autowired
    private NeighbourhoodService neighbourhoodService;

    @GetMapping("/byDistrict/{districtId}")
    public ResponseEntity<List<NeighbourhoodDto>> getNeighbourhoodsByDistrict(@PathVariable Long districtId) {
        List<Neighbourhood> neighbourhoods = neighbourhoodService.findNeighbourhoodsByDistrictId(districtId);
        List<NeighbourhoodDto> neighbourhoodDTOs = neighbourhoods.stream()
                .map(neighbourhood -> new NeighbourhoodDto(neighbourhood.getId(), neighbourhood.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(neighbourhoodDTOs);
    }
}
