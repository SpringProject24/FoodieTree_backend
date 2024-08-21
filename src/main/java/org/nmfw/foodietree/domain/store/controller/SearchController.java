package org.nmfw.foodietree.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.request.SearchDto;
import org.nmfw.foodietree.domain.store.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/search")
@Slf4j
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;


    /**
     *
     * @method   search
     * @param    dto { "pageNo": "Number", "keyword": "String" }
     * @return   ResponseEntity<?> type
     * @author   hoho
     * @date     2024 08 15 10:01

     */
    @GetMapping
    public ResponseEntity<?> search(SearchDto dto) throws InterruptedException {
        Map<String, Object> result = searchService.searchStores(dto);
        Thread.sleep(1000);
        return ResponseEntity.ok().body(result);
    }
}
