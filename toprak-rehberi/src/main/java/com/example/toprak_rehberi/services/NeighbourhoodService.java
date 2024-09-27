package com.example.toprak_rehberi.services;
import com.example.toprak_rehberi.entities.Neighbourhood;
import com.example.toprak_rehberi.repos.NeighbourhoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NeighbourhoodService {
    @Autowired
    private NeighbourhoodRepository neighbourhoodRepository;

    public Neighbourhood findById(Long id) {
        return neighbourhoodRepository.findById(id).orElse(null);
    }
    public Neighbourhood saveNeighbourhood(Neighbourhood neighbourhood){
        return neighbourhoodRepository.save(neighbourhood);
    }
    public List<Neighbourhood> findNeighbourhoodsByDistrictId(Long districtId){
        return neighbourhoodRepository.findByDistrictId(districtId);
    }
    public Neighbourhood findByName(String name) {
        return neighbourhoodRepository.findByName(name);
    }
}