package org.nmfw.foodietree.domain.store.entity.value;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString
@AllArgsConstructor
public enum ApproveStatus {
    PENDING("승인 대기 중", "대기", "대기"),
    IN_PROGRESS("승인 진행 중", "진행", "진행"),
    APPROVED("승인 완료", "승인", "유효"),
    REJECTED("승인 거부", "거절", "무효"),
    y("승인", "승인", "승인");

    private final String statusDesc; // 상태 설명
    private final String desc; // 상태
    private final String validatedDesc; // 검증 결과
}
