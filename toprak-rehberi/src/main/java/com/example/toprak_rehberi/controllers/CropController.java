package com.example.toprak_rehberi.controllers;

import com.example.toprak_rehberi.entities.Lands;
import com.example.toprak_rehberi.entities.Product;
import com.example.toprak_rehberi.entities.User;
import com.example.toprak_rehberi.services.CropService;
import com.example.toprak_rehberi.dto.CropDto;
import com.example.toprak_rehberi.entities.Crop;
import com.example.toprak_rehberi.services.LandsService;
import com.example.toprak_rehberi.services.ProductService;
import com.example.toprak_rehberi.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/crops")

 public class CropController{

    @Autowired
    private CropService cropService;

    @Autowired
    private UserService userService;

    @Autowired
    private LandsService landsService;

    @Autowired
    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(LandsController.class);
    @PostMapping("/add")
    public ResponseEntity<?> addLand(@RequestBody CropDto cropDto) {
        User user = userService.findById(cropDto.getUserId());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found for username: " + cropDto.getUserId());
        }

        // Find the land by ID
        Lands land = landsService.findLandById(cropDto.getLandId());
        if (land == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Land not found for ID: " + cropDto.getLandId());
        }

        // Find the product by ID
        Product product = productService.findById(cropDto.getProductId());
        if (product == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found for ID: " + cropDto.getProductId());
        }


        Crop crop = new Crop();
        crop.setLand(land);
        crop.setProduct(product);
        crop.setAreaInSquareMeters(cropDto.getAreaInSquareMeters());
        crop.setPlantingDate(cropDto.getPlantingDate());
        crop.setUser(user);

        Crop savedCrop = cropService.saveCrop(crop);

        // Convert saved Crop to CropDto
        CropDto savedCropDto = new CropDto();
        savedCropDto.setId(savedCrop.getId());
        savedCropDto.setLandId(savedCrop.getLand().getId());
        savedCropDto.setLandName(savedCrop.getLand().getNeighbourhood().getName());
        savedCropDto.setProductId(savedCrop.getProduct().getId());
        savedCropDto.setProductName(savedCrop.getProduct().getName());
        savedCropDto.setAreaInSquareMeters(savedCrop.getAreaInSquareMeters());
        savedCropDto.setPlantingDate(savedCrop.getPlantingDate());
        savedCropDto.setUserId(savedCrop.getUser().getId());

        return ResponseEntity.ok(savedCropDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CropDto>> getCropsByUserId(@PathVariable Long userId) {
        logger.info("Received request to get crops for user ID: {}", userId);

        // Kullanıcıyı id ile bul
        User user = userService.findById(userId);

        if (user == null) {
            logger.error("User not found with ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        logger.info("User found: {}", user);


        List<Crop> crops = cropService.getCropsByUserId(userId);
        if (crops.isEmpty()) {
            logger.info("No crops found for user with ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }


        List<CropDto> cropDtos = crops.stream().map(crop -> {
            CropDto dto = new CropDto();
            dto.setId(crop.getId());
            dto.setLandId(crop.getLand().getId());
            dto.setProductId(crop.getProduct().getId());
            dto.setAreaInSquareMeters(crop.getAreaInSquareMeters());
            dto.setPlantingDate(crop.getPlantingDate());
            dto.setUserId(crop.getUser().getId());
            dto.setProductName(crop.getProduct().getName());
            dto.setLandName(crop.getLand().getNeighbourhood().getDistrict().getName() + "," + crop.getLand().getNeighbourhood().getName() );
            return dto;
        }).collect(Collectors.toList());

        logger.info("Returning {} crops for user ID: {}", cropDtos.size(), userId);

        return ResponseEntity.ok(cropDtos);
    }

}

