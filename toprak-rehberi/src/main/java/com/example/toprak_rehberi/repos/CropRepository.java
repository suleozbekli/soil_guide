package com.example.toprak_rehberi.repos;

import com.example.toprak_rehberi.entities.Crop;
import com.example.toprak_rehberi.entities.Lands;
import com.example.toprak_rehberi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CropRepository extends JpaRepository<Crop, Long> {
    List<Crop> findByUser(User user);
    List<Crop> findByUserId(Long userId);
    List<Crop> findByUser_Username(String username);
    Crop findByLand_IdAndProduct_Id(Long landId, Long productId);
}
