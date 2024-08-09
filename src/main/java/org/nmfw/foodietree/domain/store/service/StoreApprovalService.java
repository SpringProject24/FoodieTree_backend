package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.product.Util.FileUtil;
import org.nmfw.foodietree.domain.store.dto.request.ProductApprovalReqDto;
import org.nmfw.foodietree.domain.store.dto.request.StoreApprovalReqDto;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.nmfw.foodietree.domain.store.openapi.LicenseDto;
import org.nmfw.foodietree.domain.store.openapi.LicenseResDto;
import org.nmfw.foodietree.domain.store.openapi.LicenseService;
import org.nmfw.foodietree.domain.store.repository.StoreApprovalRepository;
import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.nmfw.foodietree.domain.auth.security.TokenProvider.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreApprovalService {

    @Value("${file.upload.root-path}")
    private String rootPath;

    private final StoreApprovalRepository storeApprovalRepository;
    private final StoreRepository storeRepository;
    private final LicenseService licenseService;

    // 등록 요청 내역을 tbl_store_approval에 저장
    public StoreApproval askStoreApproval(
        StoreApprovalReqDto dto
        , TokenUserInfo userInfo
    ) {
        // userInfo storeId로 Store
        if (!userInfo.getRole().equalsIgnoreCase("store")) {
            throw new RuntimeException("스토어 계정이 아닙니다.");
        }
        String storeId = userInfo.getUsername();
        log.debug("등록요청 가게: {}", storeId);

        StoreApproval storeApproval = dto.toEntityForStoreDetail();
        storeApproval.setStoreId(storeId);
        StoreApproval saved = storeApprovalRepository.save(storeApproval);
        log.debug("saved storeApproval - storeDetail: {}", saved);
        return saved;
    }

    // 상품 디테일 tbl_store_approval 업데이트
    public void askProductApproval(
        ProductApprovalReqDto dto
        , TokenUserInfo userInfo
    ) {
        // userInfo에서 storeId 찾기
        if (!userInfo.getRole().equalsIgnoreCase("store")) {
            throw new RuntimeException("스토어 계정이 아닙니다.");
        }
        String storeId = userInfo.getUsername();

        // 이미지 파일 저장 및 경로 문자열로 반환
        MultipartFile file = dto.getProductImage();
//        String productImage = null;
        String productImage = "/test";
        if (file != null && !file.isEmpty()) {
            productImage = FileUtil.uploadFile(rootPath, file);
        }

        // StoreApproval 상품 디테일 업데이트
        StoreApproval entity = storeApprovalRepository.findByStoreId(storeId);
        entity.setPrice(dto.getPrice());
        entity.setProductCnt(dto.getProductCnt());
        entity.setProImage(productImage);

        // repository 저장
        StoreApproval saved = storeApprovalRepository.save(entity);
        log.info("saved StoreApproval - productDetail: {}", saved);
    }


    // 가게 승인 요청 대기 중이면 사업자등록번호 검증
//    @Scheduled(fixedRate = 180000) // 3분마다 스케줄 실행
    public void verifyLicenses() {
        List<StoreApproval> noVerifiedList
            = storeApprovalRepository.findApprovalsByLicenseVerification();
        log.debug("\n승인 대기 리스트 {}", noVerifiedList);

        // API 요구대로 List를 사업자등록번호만 담은 Array로 변환
        String[] array = noVerifiedList.stream()
            .map(StoreApproval::getLicense).collect(Collectors.toList())
            .toArray(new String[noVerifiedList.size()]);

        // API 호출 및 결과 LicenseResDto
        LicenseResDto resDto = licenseService.verifyLicensesByOpenApi(array);

        // API status code OK이면 사업자등록번호 검증 결과 setter로 업데이트
        if ("OK".equals(resDto.getStatus_code())) {
            List<LicenseDto> results = resDto.getData();

            for (int i = 0; i < results.size(); i++) {
                StoreApproval storeApproval = noVerifiedList.get(i);
                // 조회 결과 유효한 번호인 경우
                if (!results.get(i).getB_stt().isEmpty()) {
                    storeApproval.setLicenseVerification(ApproveStatus.APPROVED);
                } else { // 조회 결과 유효하지 않은 번호인 경우
                    storeApproval.setLicenseVerification(ApproveStatus.REJECTED);
                }
                log.debug("\n조회 결과 : {}", storeApproval.toString());
            }
            storeApprovalRepository.saveAll(noVerifiedList);
        }
    }
}