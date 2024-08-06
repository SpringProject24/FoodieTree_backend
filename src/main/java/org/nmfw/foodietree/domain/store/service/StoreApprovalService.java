package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.request.StoreApprovalReqDto;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.nmfw.foodietree.domain.store.openapi.LicenseDto;
import org.nmfw.foodietree.domain.store.openapi.LicenseResDto;
import org.nmfw.foodietree.domain.store.openapi.LicenseService;
import org.nmfw.foodietree.domain.store.repository.StoreApprovalRepository;
import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreApprovalService {

    private final StoreApprovalRepository storeApprovalRepository;
    private final StoreRepository storeRepository;
    private final LicenseService licenseService;

    // 등록 요청 내역을 tbl_store_approval에 저장
    public void askStoreApproval(
            StoreApprovalReqDto dto
//            , String storeId
//           , TokenUserInfo userInfo
    ) {
        // userInfo storeId로 Store
//        Store foundStore = storeRepository
//                .findById(userInfo.getUserId())
//                .orElseThrow(throw new NoSuchElementException());

        // 테스트용으로 storeId = 'test@test.com'
        String storeId = "test@test.com";
        Store foundStore = storeRepository
                .findByStoreId(storeId)
                .orElseThrow(() -> new NoSuchElementException("가입한 계정이 아닙니다."));
        log.debug("등록요청: 가게 foundStore: {}", foundStore);

        StoreApproval storeApproval = dto.toEntity();
        storeApproval.setStoreId(storeId);
        StoreApproval saved = storeApprovalRepository.save(storeApproval);
        log.debug("saved storeApproval: {}", saved);
    }

    // 가게 승인 요청 대기 중이면 사업자등록번호 검증
//    @Scheduled(fixedRate = 180000) // 3분마다 스케줄 실행
    public void verifyLicenses() {
        List<StoreApproval> noVerifiedList
                = storeApprovalRepository.findApprovalsByLicenseVerification();

        // API 요구대로 List를 사업자등록번호만 담은 Array로 변환
        String[] array = noVerifiedList.stream()
                .map(StoreApproval::getLicense).collect(Collectors.toList())
                .toArray(new String[noVerifiedList.size()]);

        // API 호출 및 결과 LicenseResDto
        LicenseResDto resDto = licenseService.verifyLicensesByOpenApi(array);

        // API status code OK이면 사업자등록번호 검증 결과 setter로 업데이트
        if("OK".equals(resDto.getStatus_code())) {
            List<LicenseDto> results = resDto.getData();

            for (int i = 0; i < results.size(); i++) {
                StoreApproval storeApproval = noVerifiedList.get(i);
                // 조회 결과 유효한 번호인 경우
                if(!results.get(i).getB_stt().isEmpty()) {
                    storeApproval.setLicenseVerification(ApproveStatus.APPROVED);
                } else { // 조회 결과 유효하지 않은 번호인 경우
                    storeApproval.setLicenseVerification(ApproveStatus.REJECTED);
                }
                log.debug("\n조회 결과 : {}", storeApproval.toString());
            }
            storeApprovalRepository.saveAll(noVerifiedList);
        }

    }

    // 가게 등록 요청이 승인되면 tbl_store에 저장
    public void sendStoreInfo(
            StoreApproval sa
    ) {
        Store foundStore = storeRepository.findByStoreId(sa.getStoreId())
                .orElseThrow(()->new NoSuchElementException("가입한 계정이 아닙니다."));

        Store updatedStore = sa.updateFromStoreApproval(foundStore);

//        Store updatedStore = sa.updateFromStoreApproval();
//        Store saved = storeRepository.save(updatedStore);
        log.info("saved store: {}", updatedStore);
    }
}
