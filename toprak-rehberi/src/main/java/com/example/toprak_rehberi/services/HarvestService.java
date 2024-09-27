package com.example.toprak_rehberi.services;

import com.example.toprak_rehberi.HarvestQuality;
import com.example.toprak_rehberi.entities.*;
import com.example.toprak_rehberi.dto.HarvestDto;
import com.example.toprak_rehberi.repos.HarvestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HarvestService {

    @Autowired
    private HarvestRepository harvestRepository;
    @Autowired
    private LandsService landsService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CropService cropService;

    public HarvestDto saveHarvest(HarvestDto harvestDto) {

        Lands land = landsService.findLandById(harvestDto.getLandId());
        Product product = productService.findById(harvestDto.getProductId());
        User user = userService.findByUserId(harvestDto.getUserId());

        if (land == null || product == null) {
            throw new IllegalArgumentException("Land or Product not found");
        }

        // Land ve Product ID'sine göre Crop bulunur
        Crop crop = cropService.findCropByLandAndProduct(land.getId(), product.getId());

        if (crop == null) {
            throw new IllegalArgumentException("No planting found for this land and product.");
        }

        // Ekim tarihi ile hasat tarihini karşılaştır
        if (!harvestDto.getHarvestDate().isAfter(crop.getPlantingDate())) {
            throw new IllegalArgumentException("Harvest date must be after planting date.");
        }

        Harvest harvest = new Harvest();
        harvest.setLand(land);
        harvest.setProduct(product);
        harvest.setHarvestDate(harvestDto.getHarvestDate());
        harvest.setHarvestQuality(HarvestQuality.valueOf(harvestDto.getHarvestQuality()));
        harvest.setUser(user);

        Harvest savedHarvest = harvestRepository.save(harvest);

        return new HarvestDto(
                savedHarvest.getId(),
                savedHarvest.getLand().getId(),
                savedHarvest.getProduct().getId(),
                savedHarvest.getHarvestDate(),
                savedHarvest.getHarvestQuality().name(),
                savedHarvest.getUser().getId(),
                savedHarvest.getLand().getNeighbourhood().getName(),
                savedHarvest.getProduct().getName()
        );
    }

    public List<Harvest> getHarvestsByUserId(Long userId) {
         return harvestRepository.findByUserId(userId);
    }

}



