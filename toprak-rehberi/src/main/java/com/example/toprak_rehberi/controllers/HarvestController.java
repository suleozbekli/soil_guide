package com.example.toprak_rehberi.controllers;

import com.example.toprak_rehberi.dto.HarvestDto;
import com.example.toprak_rehberi.entities.Harvest;
import com.example.toprak_rehberi.entities.User;
import com.example.toprak_rehberi.services.HarvestService;
import com.example.toprak_rehberi.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;


import java.util.List;

@RestController
@RequestMapping("/api/harvest")
public class HarvestController {

    @Autowired
    private HarvestService harvestService;

    @Autowired
    private UserService userService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/success-rates")
    public List<Map<String, Object>> getSuccessRates() {
        String sql = "WITH base_harvest AS ("
                + "    SELECT "
                + "        l.id AS land_id, "
                + "        p.id AS product_id, "
                + "        c.name AS il, "
                + "        d.name AS ilce, "
                + "        n.name AS mahalle, "
                + "        p.name AS urun, "
                + "        CASE "
                + "            WHEN h.harvest_quality = 'COK_IYI' THEN 80 "
                + "            WHEN h.harvest_quality = 'NE_IYI_NE_KOTU' THEN 60 "
                + "            ELSE 30 "
                + "        END AS initial_harvest_score "
                + "    FROM "
                + "        harvest AS h "
                + "    JOIN "
                + "        lands AS l ON l.id = h.land_id "
                + "    JOIN "
                + "        product AS p ON p.id = h.product_id "
                + "    JOIN "
                + "        neighbourhoods AS n ON n.id = l.neighbourhood_id "
                + "    JOIN "
                + "        districts AS d ON d.id = n.district_id "
                + "    JOIN "
                + "        cities AS c ON c.id = d.city_id "
                + "), "
                + "updated_harvest AS ( "
                + "    SELECT "
                + "        il, "
                + "        ilce, "
                + "        mahalle, "
                + "        urun, "
                + "        COUNT(1) AS feedback_count, "
                + "        SUM( "
                + "            CASE "
                + "                WHEN h.harvest_quality = 'COK_IYI' THEN 1 "
                + "                WHEN h.harvest_quality = 'NE_IYI_NE_KOTU' THEN 0 "
                + "                ELSE -1 "
                + "            END "
                + "        ) AS total_feedback, "
                + "        MAX(initial_harvest_score) AS initial_score "
                + "    FROM "
                + "        base_harvest AS bh "
                + "    JOIN "
                + "        harvest AS h ON h.land_id = bh.land_id AND h.product_id = bh.product_id "
                + "    GROUP BY "
                + "        il, ilce, mahalle, urun "
                + ") "
                + "SELECT "
                + "    il, "
                + "    ilce, "
                + "    mahalle, "
                + "    urun, "
                + "    ROUND( "
                + "        LEAST( "
                + "            GREATEST(initial_score + total_feedback * 0.5, 5), "
                + "            95 "
                + "        ), "
                + "        2 "
                + "    ) AS final_success_rate "
                + "FROM "
                + "    updated_harvest";

        return jdbcTemplate.queryForList(sql);
    }

    private static final Logger logger = LoggerFactory.getLogger(LandsController.class);


    @PostMapping("/add")
    public ResponseEntity<HarvestDto> createHarvest(@RequestBody HarvestDto harvestDto) {
        try {
            HarvestDto createdHarvest = harvestService.saveHarvest(harvestDto);
            return ResponseEntity.status(201).body(createdHarvest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/by-user-id/{userId}")
    public ResponseEntity<List<HarvestDto>> getHarvestsByUserId(@PathVariable Long userId) {
        logger.info("Received request to get harvests for user ID: {}", userId);

        // find a user with id
        User user = userService.findById(userId);

        if (user == null) {
            logger.error("User not found with ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        logger.info("User found: {}", user);


        List<Harvest> harvests = harvestService.getHarvestsByUserId(userId);
        if (harvests.isEmpty()) {
            logger.info("No harvests found for user with ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        // Convert dtos
        List<HarvestDto> harvestDtos = harvests.stream().map(harvest -> {
            HarvestDto dto = new HarvestDto();
            dto.setId(harvest.getId());
            dto.setLandId(harvest.getLand().getId());
            dto.setProductId(harvest.getProduct().getId());
            dto.setHarvestDate(harvest.getHarvestDate());
            dto.setHarvestQuality(String.valueOf(harvest.getHarvestQuality()));
            dto.setUserId(harvest.getUser().getId());
            dto.setLandName(harvest.getLand().getNeighbourhood().getName());
           dto.setProductName(harvest.getProduct().getName());

            return dto;
        }).collect(Collectors.toList());

        logger.info("Returning {} harvests for user ID: {}", harvestDtos.size(), userId);

        return ResponseEntity.ok(harvestDtos);
    }

}
