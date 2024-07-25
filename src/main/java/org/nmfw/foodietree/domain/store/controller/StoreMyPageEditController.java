package org.nmfw.foodietree.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateDto;
import org.nmfw.foodietree.domain.customer.util.LoginUtil;
import org.nmfw.foodietree.domain.product.Util.FileUtil;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreStatsDto;
import org.nmfw.foodietree.domain.store.service.StoreMyPageEditService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreMyPageEditController {

    private final StoreMyPageEditService storeMyPageEditService;

    /**
     *
     * @method   updateStoreInfo
     * @param    dto
     * @return   ResponseEntity<?> type
     * @author   hoho
     * @date     2024 07 25 16:26
     *
     * {
     *     type: price / openAt / closedAt / productCnt / business_number
     *     value: String
     * }
     *
     */
    @PatchMapping("/edit")
    public ResponseEntity<?> updateStoreInfo(@RequestBody UpdateDto dto) {
        String storeId = "thdghtjd115@naver.com";
        boolean flag = storeMyPageEditService.updateStoreInfo(storeId, dto.getType(), dto.getValue());
        if (flag)
            return ResponseEntity.ok().body(true);
        return ResponseEntity.badRequest().body(false);
    }

    /**
     *
     * @method   updateProfileImage
     * @param    storeImg
     * @return   ResponseEntity<?> type
     * @author   hoho
     * @date     2024 07 25 17:14
     *
     */
    @PostMapping("/edit/img")
    public ResponseEntity<?> updateProfileImage(@RequestParam("storeImg") MultipartFile storeImg) {
        String storeId = "thdghtjd115@naver.com";
        boolean flag = storeMyPageEditService.updateProfileImg(storeId, storeImg);
        if (flag)
            return ResponseEntity.ok().body(true);
        return ResponseEntity.badRequest().body(false);
    }
}
