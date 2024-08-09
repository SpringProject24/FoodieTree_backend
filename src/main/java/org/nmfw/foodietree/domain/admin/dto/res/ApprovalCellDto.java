package org.nmfw.foodietree.domain.admin.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.nmfw.foodietree.domain.store.dto.resp.ApprovalInfoDto;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

import java.time.LocalDateTime;

@Getter @ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ApprovalCellDto {

    private Long id; // storeApproval PK
    private String storeId; // 스토어 계정
    private String name; // 상호명
    private String contact; // 가게 연락처
    private String address; // 가게 주소
    private String status; // 승인 요청 상태 - 대기, 승인, 거절
    private String category; // 업종 - FoodType
    private String license; // 사업자등록번호
    private String licenseVerification; // 사업자등록번호 유효성 검증 여부 - 대기, 유효, 무효

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String createdAt; // 요청 생성일시
    private int productCnt; // 상품 갯수
    private int price; // 상품 가격
    private String proImage; // 상품 등록용 이미지

    public ApprovalCellDto(ApprovalInfoDto dto) {
        this.id = dto.getStoreApprovalId();
        this.storeId = dto.getStoreId();
        this.name = dto.getName();
        this.contact = dto.getContact();
        this.address = dto.getAddress();
        this.status = dto.getStatus().getDesc();
        this.category = dto.getCategory().getFoodType();
        this.license = dto.getLicense();
        this.licenseVerification = dto.getLicenseVerification().getValidatedDesc();
        this.createdAt = dto.getCreatedAt().toString();
        this.productCnt = dto.getProductCnt();
        this.price = dto.getPrice();
        this.proImage = dto.getProImage();
    }

}
