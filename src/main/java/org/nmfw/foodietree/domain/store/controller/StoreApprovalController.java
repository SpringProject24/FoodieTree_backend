package org.nmfw.foodietree.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.util.LoginUtil;
import org.nmfw.foodietree.domain.store.dto.resp.StoreApprovalDto;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
import org.nmfw.foodietree.domain.store.service.StoreApprovalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/store")
@Slf4j
@RequiredArgsConstructor
public class StoreApprovalController {

    private final StoreApprovalService storeApprovalService;

    @GetMapping("/approval")
    public String storeApprovalForm() {
        return "store/approval-form";
    }

    @PostMapping("/approval")
    public String storeApprovalResult(@RequestParam("storeName") String storeName,
                                      @RequestParam("address") String address,
                                      @RequestParam("category") String category,
                                      @RequestParam("businessNumber") String businessNumber,
                                      @RequestParam("storeLicenseNumber") String storeLicenseNumber,
                                      HttpSession session,
                                      Model model) {

        String storeId = LoginUtil.getLoggedInUser(session);
        StoreCategory storeCategory = null;

        // DTO를 사용하여 데이터 전달
        StoreApprovalDto storeApprovalDto = new StoreApprovalDto(storeId, storeName, address, storeCategory, businessNumber, storeLicenseNumber);
        storeApprovalService.registerStoreInfo(storeApprovalDto);

        // 결과 페이지로 이동
        model.addAttribute("message", "승인 완료");
        return "redirect:/store/product";
    }
}
