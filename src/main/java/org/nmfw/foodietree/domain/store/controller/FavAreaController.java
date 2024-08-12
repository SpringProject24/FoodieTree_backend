package org.nmfw.foodietree.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateAreaDto;
import org.nmfw.foodietree.domain.customer.entity.FavArea;
import org.nmfw.foodietree.domain.customer.service.FavAreaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavAreaController {

    private final FavAreaService favAreaService;

    // 유저의 FavArea 리스트 조회
    @GetMapping("/areas")
    public ResponseEntity<List<FavArea>> getFavAreas(@AuthenticationPrincipal TokenProvider.TokenUserInfo userInfo) {
        String customerId = userInfo.getUsername();
        List<FavArea> favAreas = favAreaService.getFavAreas(customerId);
        return ResponseEntity.ok().body(favAreas);
    }

}
