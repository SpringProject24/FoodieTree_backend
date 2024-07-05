package org.nmfw.foodietree.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateDto;
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

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store/mypage/edit")
public class StoreMyPageEditController {
    String storeId = "sji4205@naver.com";

    @Value("${env.upload.path}")
    private String uploadDir;

    private final StoreMyPageEditService storeMyPageEditService;

    @GetMapping("/main")
    public String main(
            HttpSession session
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
    ) {
        log.info("store my page edit main");

        StoreMyPageDto storeInfo = storeMyPageEditService.getStoreMyPageInfo(storeId);
        StoreStatsDto stats = storeMyPageEditService.getStats(storeId);

        model.addAttribute("storeInfo", storeInfo);
        model.addAttribute("stats", stats);

        return "store/store-mypage-edit-test";
    }

    @PatchMapping("/update/password")
    public ResponseEntity<?> updateCustomerPw(@RequestBody String newPassword) {
        boolean flag = storeMyPageEditService.updateStorePw(storeId, newPassword);
        return flag? ResponseEntity.ok("password reset successful"): ResponseEntity.status(400).body("reset fail");
    }

    @PatchMapping("/update/info")
    public ResponseEntity<?> updateStoreInfo(@RequestBody UpdateDto dto) {
        storeMyPageEditService.updateStoreInfo(storeId, dto.getType(), dto.getValue());
        return ResponseEntity.ok().body("update store info");
    }

    @PostMapping("/update/img")
    public ResponseEntity<?> updateProfileImage(@RequestParam("storeImg") MultipartFile storeImg) {
        try {
            // 예시로 파일 이름과 크기를 출력하는 코드
            String fileName = storeImg.getOriginalFilename();
            long fileSize = storeImg.getSize();
            System.out.println("Received file: " + fileName + ", Size: " + fileSize + " bytes");
            String imagePath = null;
            if (!storeImg.isEmpty()) {
                // 서버에 업로드 후 업로드 경로 반환
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                imagePath = FileUtil.uploadFile(uploadDir, storeImg);
                storeMyPageEditService.updateStoreInfo(storeId, "store_img", imagePath);
                return ResponseEntity.ok().body(true);
            }
//            FileUtil.uploadFile(rootPath, storeImg);

        } catch (Exception e) {
            // 업로드 실패 시 예외 처리
            return ResponseEntity.badRequest().body("Failed to upload file");
        }
        return ResponseEntity.badRequest().body("Failed to upload file");
    }

    @PatchMapping("/update/price")
    public ResponseEntity<?> updatePrice(@RequestBody int price) {
        storeMyPageEditService.updatePrice(storeId, price);
        return ResponseEntity.ok().body("update price");
    }

    @PatchMapping("/update/openAt")
    public ResponseEntity<?> updateOpenAt(@RequestBody String time) {
        storeMyPageEditService.updateOpenAt(storeId, time);
        return ResponseEntity.ok().body("update open at");
    }

    @PatchMapping("/update/closedAt")
    public ResponseEntity<?> updateClosedAt(@RequestBody String time) {
        storeMyPageEditService.updateClosedAt(storeId, time);
        return ResponseEntity.ok().body("update closed at");
    }

    @PatchMapping("/update/productCnt")
    public ResponseEntity<?> updateProductCnt(@RequestBody int cnt) {
        storeMyPageEditService.updateProductCnt(storeId, cnt);
        return ResponseEntity.ok().body("update product cnt");
    }
}
