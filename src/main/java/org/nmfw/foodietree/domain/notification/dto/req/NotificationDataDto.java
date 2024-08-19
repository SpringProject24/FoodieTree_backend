package org.nmfw.foodietree.domain.notification.dto.req;

import lombok.*;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 다른 Service에서 알림 Service로 데이터 전달
public class NotificationDataDto {

    private String customerId;
    private String storeId;
    private String storeName;
    private List<String> targetId; // 예약 ID, 리뷰 ID, ...
}
