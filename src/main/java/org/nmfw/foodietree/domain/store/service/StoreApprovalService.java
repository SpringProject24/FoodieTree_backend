package org.nmfw.foodietree.domain.store.service;

import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.notification.service.NotificationService;
import org.nmfw.foodietree.domain.product.Util.FileUtil;
import org.nmfw.foodietree.domain.store.dto.request.ProductApprovalReqDto;
import org.nmfw.foodietree.domain.store.dto.request.StoreApprovalReqDto;
import org.nmfw.foodietree.domain.store.dto.resp.ApprovalInfoDto;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.nmfw.foodietree.domain.store.openapi.LicenseDto;
import org.nmfw.foodietree.domain.store.openapi.LicenseResDto;
import org.nmfw.foodietree.domain.store.openapi.LicenseService;
import org.nmfw.foodietree.domain.store.repository.StoreApprovalRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import static org.nmfw.foodietree.domain.auth.security.TokenProvider.*;

@Service
@Slf4j
public class StoreApprovalService {

    private final NotificationService notificationService;
    @Value("${file.upload.root-path}")
    private String rootPath;

    private final StoreApprovalRepository storeApprovalRepository;
    private final LicenseService licenseService;
    private final TaskScheduler approvalTaskScheduler;

    public StoreApprovalService(@Qualifier("approvalTaskScheduler") TaskScheduler approvalTaskScheduler,
                                StoreApprovalRepository storeApprovalRepository,
                                LicenseService licenseService, NotificationService notificationService) {
        this.approvalTaskScheduler = approvalTaskScheduler;
        this.storeApprovalRepository = storeApprovalRepository;
        this.licenseService = licenseService;
        this.notificationService = notificationService;
    }

    private final List<StoreApproval> pendingApprovals = new CopyOnWriteArrayList<>();
    private ScheduledFuture<?> scheduledTask;

    /**
     * 가게의 승인 요청을 tbl_store_approval에 저장
     *
     * @param dto 승인 요청 정보 DTO
     * @param userInfo 사용자 정보
     * @throws IllegalArgumentException 스토어 계정이 아닌 경우
     */
    public void askStoreApproval(
        StoreApprovalReqDto dto
        , TokenUserInfo userInfo
    ) {
        checkStoreAccount(userInfo);
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
                    notificationService.getInstantTime(LocalDateTime.now().plusMinutes(3)));
        }
    }

    /**
     * 상품 디테일을 tbl_store_approval에 업데이트
     *
     * @param dto 상품 승인 요청 정보 DTO
     * @param userInfo 사용자 정보
     * @throws IllegalArgumentException 스토어 계정이 아닌 경우
     */
    public void askProductApproval(
        ProductApprovalReqDto dto
        , TokenUserInfo userInfo
    ) {
        checkStoreAccount(userInfo);
        String storeId = userInfo.getUsername();

        // 이미지 파일 저장 및 경로 문자열로 반환
        MultipartFile file = dto.getProductImage();
        String productImage = null;
        if (file != null && !file.isEmpty()) {
            productImage = FileUtil.uploadFile(rootPath, file);
        }

        // StoreApproval 상품 디테일 업데이트
        StoreApproval entity = storeApprovalRepository.findRecentOneByStoreId(storeId);
        entity.setPrice(dto.getPrice());
        entity.setProductCnt(dto.getProductCnt());
        entity.setProImage(productImage);

        // repository 저장
        StoreApproval saved = storeApprovalRepository.save(entity);
        log.info("saved StoreApproval - productDetail: {}", saved);
    }
    /**
     * 대기 중인 승인 요청을 처리하는 메서드
     *
     * 대기 중인 요청 리스트를 가져와 검증 로직을 수행한 후, 스케줄을 초기화
     */
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
    /**
     * 승인 요청 리스트의 사업자등록번호를 검증
     *
     * @param approvals 승인 요청 리스트
     */
//    @Scheduled(fixedRate = 180000) // 3분마다 스케줄 실행
    public void verifyLicenses(List<StoreApproval> approvals) {
        // 위에서 받은 승인 요청 리스트로 검증 로직 수행
        String[] newApprovals = approvals.stream()
                .map(StoreApproval::getLicense)
                .toArray(String[]::new);
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
                log.debug("\n사업자번호 조회 결과 : {}", storeApproval);
            }
            storeApprovalRepository.saveAll(approvals);
        }
    }
    /**
     * 주어진 storeId에 대해 현재의 승인 상태 조회
     *
     * @param userInfo 토큰
     * @return 현재 승인 상태를 나타내는 문자열 ("STEP_ONE", "STEP_TWO", "PENDING_APPROVAL", "ALREADY_APPROVED")
     */
    public String findCurrentApprovalState(TokenUserInfo userInfo) {
        checkStoreAccount(userInfo);
        // 요청 리스트를 최신순으로 확인 (최신 요청부터 검사)
        List<ApprovalInfoDto> dtoList = storeApprovalRepository.findDtoListByStoreId(userInfo.getUsername());
        if(dtoList == null || dtoList.isEmpty()) {
            return "STEP_ONE";
        }
        for (ApprovalInfoDto dto : dtoList) {
            String approvalState = evaluateApprovalState(dto);
            log.debug("storeApproval 승인 상태 확인 : {}", approvalState);
            if (!approvalState.equals("STEP_ONE")) {
                return approvalState; // STEP_ONE이 아닌 상태가 있으면 해당 상태 반환
            }
        }
        return "STEP_ONE";
    }
    /**
     * ApprovalInfoDto의 상태를 평가하여 승인 상태를 반환
     *
     * @param dto 승인 요청 정보 DTO
     * @return DTO의 상태에 기반한 승인 상태 문자열 ("STEP_ONE", "STEP_TWO", "PENDING_APPROVAL", "ALREADY_APPROVED")
     */
    public String evaluateApprovalState(ApprovalInfoDto dto) {
        log.info("승인 상태 evaluateApprovalState : {}", dto);
        if(dto == null) return "STEP_ONE";
        if(dto.getStatus() == ApproveStatus.APPROVED) return "ALREADY_APPROVED";
        if(dto.getStatus() == ApproveStatus.REJECTED) return "STEP_ONE";
        if(dto.getProImage() == null) return "STEP_TWO";
        // status PENDING && 상품디테일 등록된 경우 승인 대기 중
        return "PENDING_APPROVAL";
    }
    /**
     * 주어진 사용자 정보가 스토어 계정인지 확인
     *
     * @param userInfo 사용자 정보
     * @throws IllegalArgumentException 스토어 계정이 아닌 경우
     */
    private void checkStoreAccount(TokenUserInfo userInfo) {
        if (!userInfo.getRole().equalsIgnoreCase("store")) {
            throw new IllegalArgumentException("스토어 계정이 아닙니다.");
        }
    }
}


