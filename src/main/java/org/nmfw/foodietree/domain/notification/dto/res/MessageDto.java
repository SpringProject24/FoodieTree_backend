package org.nmfw.foodietree.domain.notification.dto.res;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {

    private Long id; // 알림 PK
    private String type; // 알림 유형 - 예약 확인, 예약 취소, ...
    private String receiverId; // 알림 받을 Id (customer, store ...)
    private String senderId; // 알림 보낸 Id (store, ...)
    private String targetId; // 예약Id, 게시글Id 등
    private String content; // 알림 내용
    private boolean isRead; // 알림 확인 여부

}
