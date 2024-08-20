package org.nmfw.foodietree.domain.notification.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "notification_target_list")
    @Convert(converter = StringListConverter.class)
    private List<String> targetIdList; // 예약이면 예약ID, 리뷰면 리뷰ID

    private String label; // 알림 내용의 prefix [예약] ...
    @Column(name = "notification_content")
    private String content; // 알림 내용
    @Setter
    private String isRead; // 알림 수신자의 열람 여부, null 또는 R

    @CreationTimestamp
    private LocalDateTime createdAt; // 알림 발송시간

}
