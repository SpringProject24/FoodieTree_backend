package org.nmfw.foodietree.domain.admin.dto.req;

import lombok.*;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalStatusDto {

    private String actionType; // 승인 APPROVED 또는 거절 REJECTED
    private List<Long> approvalIdList; // 선택된 StoreApproval PK 목록
}
