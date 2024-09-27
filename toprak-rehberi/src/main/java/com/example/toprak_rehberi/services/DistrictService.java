package com.example.toprak_rehberi.services;
import com.example.toprak_rehberi.entities.District;
import com.example.toprak_rehberi.repos.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {
    @Autowired
    private DistrictRepository districtRepository;

    public District findById(Long id) {
        return districtRepository.findById(id).orElse(null);
    }
    public District saveDistrict(District district){
        return districtRepository.save(district);
    }

    public List<District> findDistrictsByCityId(Long cityId){
        List<District> districts = districtRepository.findByCityId(cityId);
        System.out.println("Districts found: " + districts.size());  // Kaç tane district bulunduğunu yazdır
        return districts;

    }
    public District findByName(String name) {
        return districtRepository.findByName(name);
    }
}
