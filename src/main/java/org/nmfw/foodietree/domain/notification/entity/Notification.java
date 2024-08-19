package org.nmfw.foodietree.domain.notification.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode(of = "notificationId")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(name = "notification_type")
    private String type; // 알림 유형
    private String receiverId;
    private String senderId;
    private String targetId; // 예약이면 예약ID, 리뷰면 리뷰ID

    @Column(name = "notification_content")
    private String content; // 알림 내용
    private String isRead; // 알림 수신자의 열람 여부, null 또는 R

    @CreationTimestamp
    private LocalDateTime createdAt; // 알림 발송시간

}
