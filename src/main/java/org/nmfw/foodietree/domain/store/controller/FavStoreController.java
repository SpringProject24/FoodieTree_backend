package org.nmfw.foodietree.domain.store.controller;

import org.nmfw.foodietree.domain.store.dto.request.FavStoreRequestDto;
import org.nmfw.foodietree.domain.store.service.StoreList.FavStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/favorites")
public class FavStoreController {

    @Autowired
    private FavStoreService favStoreService;

    @PostMapping("/{storeId}")
    public ResponseEntity<Map<String, String>> toggleFavorite(@PathVariable String storeId, @RequestBody FavStoreRequestDto requestDto) {
        String customerId = requestDto.getCustomerId();
        favStoreService.toggleFavorite(customerId, storeId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Favorite toggled successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getFavorites(@PathVariable String customerId) {
        try {
            return ResponseEntity.ok(favStoreService.getFavoritesByCustomerId(customerId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving favorites");
        }
    }
}