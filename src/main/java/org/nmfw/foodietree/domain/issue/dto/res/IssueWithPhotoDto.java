package org.nmfw.foodietree.domain.issue.dto.res;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueWithPhotoDto {

    private Long issueId;
    private String issueCategory;
    private LocalDateTime issueCompleteAt;
    private String issueText;
    private LocalDateTime cancelIssueAt;
    private String customerId;
    private int reservationId;
    private LocalDateTime makeIssueAt;
    private String status;
    private List<String> issuePhotos;

    public IssueWithPhotoDto(Long issueId, String issueCategory, LocalDateTime issueCompleteAt, String issueText, LocalDateTime cancelIssueAt, String customerId, int reservationId, LocalDateTime makeIssueAt, List<String> issuePhotos) {
        this.issueId = issueId;
        this.issueCategory = issueCategory;
        this.issueCompleteAt = issueCompleteAt;
        this.issueText = issueText;
        this.cancelIssueAt = cancelIssueAt;
        this.customerId = customerId;
        this.reservationId = reservationId;
        this.makeIssueAt = makeIssueAt;
        this.status = determineStatus();
        this.issuePhotos = issuePhotos;
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
