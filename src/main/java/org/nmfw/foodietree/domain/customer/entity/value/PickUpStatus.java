package org.nmfw.foodietree.domain.customer.entity.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum PickUpStatus {
    RESERVED("픽업대기"),
    PICKEDUP("픽업완료"),
    CANCELED("예약취소"),
    NOSHOW("노쇼");

    private final String status;
}
