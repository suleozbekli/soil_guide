package com.example.toprak_rehberi.repos;


import com.example.toprak_rehberi.entities.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findByCityId(Long cityId);
    District findByName(String name);
}
