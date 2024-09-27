package com.example.toprak_rehberi.repos;


import com.example.toprak_rehberi.entities.Neighbourhood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NeighbourhoodRepository extends JpaRepository<Neighbourhood, Long> {
    List<Neighbourhood> findByDistrictId(Long districtId);
    Neighbourhood findByName(String name);
}
