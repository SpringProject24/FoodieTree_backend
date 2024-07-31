package org.nmfw.foodietree.domain.issue.entiry;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_issue")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long issueId;

    @Column(name = "issue_category")
    private String issueCategory;

    @Column(name = "issue_complete_at")
    private LocalDateTime issueCompleteAt;

    @Column(name = "issue_text")
    private String issueText;

    @Column(name = "cancel_issue_at")
    private LocalDateTime cancelIssueAt;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "reservation_id")
    private int reservationId;

}
