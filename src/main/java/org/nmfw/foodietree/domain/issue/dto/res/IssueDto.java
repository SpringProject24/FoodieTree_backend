package org.nmfw.foodietree.domain.issue.dto.res;

import lombok.*;
import org.nmfw.foodietree.domain.customer.entity.value.IssueCategory;
import org.nmfw.foodietree.domain.issue.entity.Issue;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueDto {

    private Long issueId;
    private IssueCategory issueCategory;
    private LocalDateTime issueCompleteAt;
    private String issueText;
    private LocalDateTime cancelIssueAt;
    private String customerId;
    private int reservationId;
    private LocalDateTime makeIssueAt;
    private String status;

    public IssueDto(Issue issue){
        this.issueId = issue.getIssueId();
        this.issueCategory = IssueCategory.fromString(issue.getIssueCategory());
        this.issueCompleteAt = issue.getIssueCompleteAt();
        this.issueText = issue.getIssueText();
        this.cancelIssueAt = issue.getCancelIssueAt();
        this.customerId = issue.getCustomerId();
        this.reservationId = issue.getReservationId();
        this.makeIssueAt = issue.getMakeIssueAt();
        this.status = determineStatus();
    }

    private String determineStatus() {
        if (this.issueCompleteAt != null) {
            return "SOLVED";
        } else if (this.cancelIssueAt != null) {
            return "CANCELLED";
        } else {
            return "PENDING";
        }
    }
}
