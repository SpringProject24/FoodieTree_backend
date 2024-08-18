package org.nmfw.foodietree.domain.customer.entity.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum IssueStatus {
    INPROGRESS("진행 중"),
    SOLVED("해결 완료"),
    CANCELLED ("취소");

    private final String issueStatusName;
}
