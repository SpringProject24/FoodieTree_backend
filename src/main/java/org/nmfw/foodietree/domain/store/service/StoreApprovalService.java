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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import static org.nmfw.foodietree.domain.auth.security.TokenProvider.*;

@Service
@Slf4j
public class StoreApprovalService {

    @Value("${file.upload.root-path}")
    private String rootPath;

    private final StoreApprovalRepository storeApprovalRepository;
    private final LicenseService licenseService;
    private final TaskScheduler approvalTaskScheduler;

    public StoreApprovalService(@Qualifier("approvalTaskScheduler") TaskScheduler approvalTaskScheduler,
                                StoreApprovalRepository storeApprovalRepository,
                                LicenseService licenseService) {
        this.approvalTaskScheduler = approvalTaskScheduler;
        this.storeApprovalRepository = storeApprovalRepository;
        this.licenseService = licenseService;
    }

    private final List<StoreApproval> pendingApprovals = new CopyOnWriteArrayList<>();
    private ScheduledFuture<?> scheduledTask;

    // 등록 요청 내역을 tbl_store_approval에 저장
    public void askStoreApproval(
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

        pendingApprovals.add(saved);
        // 첫번째 요청이 들어온 경우 타이머를 시작
        if (scheduledTask == null || scheduledTask.isCancelled()) {
            scheduledTask = approvalTaskScheduler.schedule(this::processPendingApprovals,
                    Instant.from(LocalDateTime.now().plusMinutes(3)));
        }
    }

    // 상품 디테일 tbl_store_approval 업데이트
    public void askProductApproval(
        ProductApprovalReqDto dto
        , TokenUserInfo userInfo
    ) {
        if (!userInfo.getRole().equalsIgnoreCase("store")) {
            throw new RuntimeException("스토어 계정이 아닙니다.");
        }
        String storeId = userInfo.getUsername();

        // 이미지 파일 저장 및 경로 문자열로 반환
        MultipartFile file = dto.getProductImage();
        String productImage = null;
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

    public void processPendingApprovals() {
        if (pendingApprovals.isEmpty()) {
            return; // 대기 중인 요청이 없으면 종료
        }
        List<StoreApproval> approvalsToProcess = new ArrayList<>(pendingApprovals);
        pendingApprovals.clear(); // 리스트 초기화
        // 검증 로직 처리
        verifyLicenses(approvalsToProcess);
        // 작업 완료 후 스케줄 초기화
        scheduledTask = null;
    }

    // 가게 승인 요청 대기 중이면 사업자등록번호 검증
//    @Scheduled(fixedRate = 180000) // 3분마다 스케줄 실행
    public void verifyLicenses(List<StoreApproval> approvals) {
        // 위에서 받은 승인 요청 리스트로 검증 로직 수행
        String[] newApprovals = approvals.stream()
                .map(StoreApproval::getLicense)
                .toArray(String[]::new);

//        List<StoreApproval> noVerifiedList
//            = storeApprovalRepository.findApprovalsByLicenseVerification();
//        log.debug("\n승인 대기 리스트 {}", noVerifiedList);

        // API 요구대로 List를 사업자등록번호만 담은 Array로 변환
//        String[] array = noVerifiedList.stream()
//        String[] array = noVerifiedList.stream()
//            .map(StoreApproval::getLicense)
//            .toArray(String[]::new);
//            .toArray(new String[noVerifiedList.size()]);

        // API 호출 및 결과 LicenseResDto
//        LicenseResDto resDto = licenseService.verifyLicensesByOpenApi(array);
        LicenseResDto resDto = licenseService.verifyLicensesByOpenApi(newApprovals);

        // API status code OK면 사업자등록번호 검증 결과 setter로 업데이트
        if ("OK".equals(resDto.getStatus_code())) {
            List<LicenseDto> results = resDto.getData();

            for (int i = 0; i < results.size(); i++) {
                StoreApproval storeApproval = approvals.get(i);
                // 조회 결과 유효한 번호인 경우 유효
                if (!results.get(i).getB_stt().isEmpty()) {
                    storeApproval.setLicenseVerification(ApproveStatus.APPROVED);
                } else { // 조회 결과 유효하지 않은 번호인 경우 무효
                    storeApproval.setLicenseVerification(ApproveStatus.REJECTED);
                }
                log.debug("\n조회 결과 : {}", storeApproval);
            }
            storeApprovalRepository.saveAll(approvals);
        }
    }
}


