package org.nmfw.foodietree.domain.store.controller;

import java.util.List;
import org.nmfw.foodietree.domain.auth.security.TokenProvider.TokenUserInfo;
import org.nmfw.foodietree.domain.customer.entity.FavStore;
import org.nmfw.foodietree.domain.store.dto.request.FavStoreRequestDto;
import org.nmfw.foodietree.domain.store.service.StoreList.FavStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    /**
     *
     * @method   toggleFavorite
     * @param    dto // { storeId: "String" }
     * @param    tokenUserInfo
     * @return   ResponseEntity<?> type
     * @author   hoho
     * @date     2024 08 15 09:58

     */
    @PostMapping
    public ResponseEntity<?> toggleFavorite(@RequestBody Map<String, String> dto, @AuthenticationPrincipal TokenUserInfo tokenUserInfo) {
        String customerId = tokenUserInfo.getUsername();
        favStoreService.toggleFavorite(customerId, dto.get("storeId"));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Favorite toggled successfully");
        return ResponseEntity.ok().body(response);
    }

    /**
     *
     * @method   getFavorites
     * @param    tokenUserInfo
     * @return   ResponseEntity<?> type
     * @author   hoho
     * @date     2024 08 15 09:58

     */
    @GetMapping
    public ResponseEntity<?> getFavorites(@AuthenticationPrincipal TokenUserInfo tokenUserInfo) {
        String customerId = tokenUserInfo.getUsername();
        List<FavStore> favoritesByCustomerId;
        try {
            favoritesByCustomerId = favStoreService.getFavoritesByCustomerId(customerId);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(favoritesByCustomerId);
    }
}