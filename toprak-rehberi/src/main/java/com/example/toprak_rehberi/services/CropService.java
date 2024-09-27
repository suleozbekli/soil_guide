
package com.example.toprak_rehberi.services;
import com.example.toprak_rehberi.dto.CropDto;
import com.example.toprak_rehberi.entities.Crop;
import com.example.toprak_rehberi.entities.Lands;
import com.example.toprak_rehberi.entities.Product;
import com.example.toprak_rehberi.entities.User;
import com.example.toprak_rehberi.repos.CropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CropService {

    @Autowired
    private CropRepository cropRepository;
    @Autowired
    private LandsService landsService;
    @Autowired
    private ProductService productService;

    public Crop saveCrop(Crop crop) {
        return cropRepository.save(crop);
    }

    public List<Crop> findCropsByUser(User user) {
        return cropRepository.findByUser(user);
    }
    public List<Crop> getCropsByUserId(Long userId) {
        return cropRepository.findByUserId(userId);
    }
    public Crop findCropById(Long id) {
        Optional<Crop> land = cropRepository.findById(id);
        return land.orElse(null);
    }
    public List<Crop> getLandsByUsername(String username) {
        return cropRepository.findByUser_Username(username);
    }
    public Crop findCropByLandAndProduct(Long landId, Long productId) {
        return cropRepository.findByLand_IdAndProduct_Id(landId, productId);
    }






}