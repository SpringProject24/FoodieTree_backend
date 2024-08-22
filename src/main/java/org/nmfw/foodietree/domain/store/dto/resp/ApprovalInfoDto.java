package org.nmfw.foodietree.domain.store.dto.resp;

import lombok.*;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 승인 요청에 대한 가게, 상품 조회 response DTO
public class ApprovalInfoDto {

    private Long storeApprovalId; // storeApproval PK
    private String storeId;
    private String name; // 상호명
    private String contact; // 가게 연락처
    private String address; // 가게 주소
    private ApproveStatus status; // 승인 요청 상태
    private StoreCategory category;
    private String license; // 사업자등록번호
    private ApproveStatus licenseVerification; // 실제 사업자등록번호인지 api 검증 여부
    private LocalDateTime createdAt; // 요청 생성일시
    private int productCnt; // 상품 갯수
    private int price; // 상품 가격
    private String proImage; // 상품 등록용 이미지

}
