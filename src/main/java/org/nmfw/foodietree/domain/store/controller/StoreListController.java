package org.nmfw.foodietree.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.nmfw.foodietree.domain.store.service.StoreList.StoreListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/storeLists")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class StoreListController {

    private final StoreListService storeListService;

    // Store 전체 조회 요청!
    @GetMapping
    public ResponseEntity<List<StoreListDto>> getAllStores() {
        List<StoreListDto> storeListDto = storeListService.getAllStores();
        return ResponseEntity.ok().body(storeListDto);
    }
}
