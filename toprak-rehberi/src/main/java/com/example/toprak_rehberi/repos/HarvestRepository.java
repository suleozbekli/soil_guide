package com.example.toprak_rehberi.repos;

import com.example.toprak_rehberi.entities.Harvest;
import com.example.toprak_rehberi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HarvestRepository extends JpaRepository<Harvest, Long> {
    List<Harvest> findByUserId(Long userId);


}
