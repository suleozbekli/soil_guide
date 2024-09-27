package com.example.toprak_rehberi.controllers;

import com.example.toprak_rehberi.LandType;
import com.example.toprak_rehberi.dto.CropDto;
import com.example.toprak_rehberi.dto.LandDto;
import com.example.toprak_rehberi.entities.*;
import com.example.toprak_rehberi.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lands")
public class LandsController {

    private static final Logger logger = LoggerFactory.getLogger(LandsController.class);

    @Autowired
    private LandsService landsService;

    @Autowired
    private UserService userService;

    @Autowired
    private CropService cropService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CityService cityService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private NeighbourhoodService neighbourhoodService;

    @PostMapping("/add")
    public ResponseEntity<?> addLand(@RequestBody LandDto landDto) {
        logger.info("Received request to add land for user: {}", landDto.getUsername());

        User user = userService.findByUsername(landDto.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found for username: " + landDto.getUsername());
        }

        Neighbourhood neighbourhood = neighbourhoodService.findById(landDto.getNeighbourhoodId());
        if (neighbourhood == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid neighbourhood data.");
        }

        Lands land = new Lands();
        land.setLandType(LandType.valueOf(landDto.getLandType()));
        land.setNeighbourhood(neighbourhood);
        land.setUser(user);
        land.setArea(landDto.getArea());
        land.setAdaParcel(landDto.getAda_parcel());

        Lands savedLand = landsService.saveLand(land);

        LandDto savedLandDto = new LandDto();
        savedLandDto.setId(savedLand.getId());
        savedLandDto.setLandType(savedLand.getLandType().name());
        savedLandDto.setNeighbourhoodName(savedLand.getNeighbourhood().getName());
        savedLandDto.setDistrictName(savedLand.getNeighbourhood().getDistrict().getName());
        savedLandDto.setCityName(savedLand.getNeighbourhood().getDistrict().getCity().getName());
        savedLandDto.setUsername(savedLand.getUser().getUsername());
        savedLandDto.setArea(savedLand.getArea());
        savedLandDto.setAda_parcel(savedLand.getAdaParcel());

        return ResponseEntity.ok(savedLandDto);
    }

    @GetMapping("/by-user-id/{userId}")
    public ResponseEntity<List<LandDto>> getLandsByUserId(@PathVariable Long userId) {
        logger.info("Received request to get lands for user ID: {}", userId);

        User user = userService.findById(userId);
        if (user == null) {
            logger.error("User not found with ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        logger.info("User found: {}", user);

        List<Lands> lands = landsService.getLandsByUserId(userId);
        if (lands.isEmpty()) {
            logger.info("No lands found for user with ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        List<LandDto> landDtos = lands.stream().map(land -> {
            LandDto dto = new LandDto();
            dto.setLandType(land.getLandType().name());
            dto.setArea(land.getArea()); // Arazi alanı
            dto.setAda_parcel(land.getAdaParcel());
            dto.setNeighbourhoodId(land.getNeighbourhood().getId());

            // `Neighbourhood` üzerinden `District` ve `City` bilgileri çekilir
            Neighbourhood neighbourhood = land.getNeighbourhood();
            if (neighbourhood != null) {
                dto.setNeighbourhoodName(neighbourhood.getName());
                District district = neighbourhood.getDistrict(); // İlçeyi Neighbourhood üzerinden alınır
                if (district != null) {
                    dto.setDistrictName(district.getName());
                    City city = district.getCity(); // ŞehirS de District üzerinden alınır
                    if (city != null) {
                        dto.setCityName(city.getName());
                    } else {
                        dto.setCityName("Unknown City");
                    }
                } else {
                    dto.setDistrictName("Unknown District");
                }
            } else {
                dto.setNeighbourhoodName("Unknown Neighbourhood");
            }

            dto.setUsername(user.getUsername());
            dto.setId(land.getId());

            return dto;
        }).collect(Collectors.toList());

        logger.info("Returning {} lands for user ID: {}", landDtos.size(), userId);

        return ResponseEntity.ok(landDtos);
    }

    @PostMapping("/add-crop")
    public ResponseEntity<CropDto> addCropToLand(@RequestBody CropDto cropDto) {
        Lands land = landsService.findLandById(cropDto.getLandId());
        Product product = productService.findById(cropDto.getProductId());

        Crop crop = new Crop();
        crop.setLand(land);
        crop.setProduct(product);
        crop.setAreaInSquareMeters(cropDto.getAreaInSquareMeters());

        Crop savedCrop = cropService.saveCrop(crop);

        CropDto savedCropDto = new CropDto();
        savedCropDto.setLandId(savedCrop.getLand().getId());
        savedCropDto.setProductId(savedCrop.getProduct().getId());
        savedCropDto.setAreaInSquareMeters(savedCrop.getAreaInSquareMeters());

        return ResponseEntity.ok(savedCropDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lands> getLandById(@PathVariable Long id) {
        Lands land = landsService.findLandById(id);
        if (land == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(land);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLand(@PathVariable Long id) {
        logger.info("Received request to delete land with ID: {}", id);

        Lands land = landsService.findLandById(id);
        if (land == null) {
            logger.error("Land not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Land not found with ID: " + id);
        }

        landsService.deleteLand(id);
        logger.info("Land deleted successfully with ID: {}", id);
        return ResponseEntity.ok("Land deleted successfully.");
    }

}
