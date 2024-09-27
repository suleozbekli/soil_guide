package com.example.toprak_rehberi.services;
import com.example.toprak_rehberi.entities.City;
import com.example.toprak_rehberi.repos.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    public City findById(Long id) {
        return cityRepository.findById(id).orElse(null);
    }
    public City saveCity(City city){
        return cityRepository.save(city);
    }
    public List<City> findAllCities(){
        return cityRepository.findAll();
    }
    public City findByName(String name) {
        return cityRepository.findByName(name);
    }
}
