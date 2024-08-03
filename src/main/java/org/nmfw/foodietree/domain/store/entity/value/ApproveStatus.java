package org.nmfw.foodietree.domain.store.entity.value;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString
@AllArgsConstructor
public enum ApproveStatus {
    PENDING("승인 대기 중"),
    IN_PROGRESS("승인 진행 중"),
    APPROVED("승인 완료"),
    REJECTED("승인 거부"),
    y("승인");

    private final String statusDesc; // 상태 설명
}
