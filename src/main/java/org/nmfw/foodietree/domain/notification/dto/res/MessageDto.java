package org.nmfw.foodietree.domain.notification.dto.res;

import lombok.*;
import org.nmfw.foodietree.domain.notification.entity.Notification;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {

    @Setter
    private Long id; // 알림 PK
    private String type; // 알림 유형 - 예약 확인, 예약 취소, ...
    private String receiverId; // 알림 받을 Id (customer, store ...)
    private String senderId; // 알림 보낸 Id (store, ...)
    private String targetId; // 예약Id, 게시글Id 등
    private String content; // 알림 내용
    private boolean isRead; // 알림 확인 여부

    public Notification toEntity() {
        return Notification.builder()
                .type(this.type)
                .receiverId(this.receiverId)
                .senderId(this.senderId)
                .targetId(this.targetId)
                .content(this.content)
                .build();
    }

}
