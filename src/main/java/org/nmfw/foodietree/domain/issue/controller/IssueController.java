package org.nmfw.foodietree.domain.issue.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.entity.value.IssueCategory;
import org.nmfw.foodietree.domain.issue.dto.res.IssueDto;
import org.nmfw.foodietree.domain.issue.dto.res.IssueWithPhotoDto;
import org.nmfw.foodietree.domain.issue.entity.Issue;
import org.nmfw.foodietree.domain.issue.entity.IssuePhoto;
import org.nmfw.foodietree.domain.issue.repository.IssuePhotoRepository;
import org.nmfw.foodietree.domain.issue.repository.IssueRepository;
import org.nmfw.foodietree.domain.issue.service.IssueService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/issue")
public class IssueController {

    private final IssueService issueService;
    private final IssueRepository issueRepository;
    private final IssuePhotoRepository issuePhotoRepository;

    @GetMapping
    public ResponseEntity<?> issue() {
        log.info("get issues");
        List<IssueDto> issues = issueService.getIssues();
        log.info("issues : {}", issues);
        return ResponseEntity.ok().body(issues);
    }

    @PostMapping
    public ResponseEntity<?> issue(@RequestBody Map<String, String> issue) {
        String customerId = issue.get("customerId");
        String reservationId = issue.get("reservationId");
        log.info("customerId : {}", customerId);
        log.info("reservationId : {}", reservationId);
        Issue newIssue = Issue.builder()
                .customerId(customerId)
                .reservationId(Integer.parseInt(reservationId))
                .cancelIssueAt(null)
                .issueCategory("")
                .issueCompleteAt(null)
                .issueText("")
                .makeIssueAt(null)
                .build();
        Issue save = issueRepository.save(newIssue);
        return ResponseEntity.ok().body(save.getIssueId());
    }

    @PostMapping("/category")
    public ResponseEntity<?> updateIssueCategory(@RequestBody Map<String, String> issue) {
        Long issueId = Long.valueOf(issue.get("issueId"));
        IssueCategory category = IssueCategory.fromString(issue.get("issueCategory"));
        String issueCategory = category.getIssueName();

        Issue issue1 = issueRepository.findById(issueId).orElseThrow(() -> new IllegalArgumentException("해당 이슈가 존재하지 않습니다."));

        issue1.setIssueCategory(issueCategory);

        issueRepository.save(issue1);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completeIssue(@RequestBody Map<String, String> issue) {
        Long issueId = Long.valueOf(issue.get("issueId"));
        LocalDateTime completeAt = issue.get("issueCompleteAt") == null ? LocalDateTime.now() : LocalDateTime.parse(issue.get("issueCompleteAt"));
        Issue issue1 = issueRepository.findById(issueId).orElseThrow(() -> new IllegalArgumentException("해당 이슈가 존재하지 않습니다."));

        issue1.setIssueCompleteAt(completeAt);

        issueRepository.save(issue1);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelIssue(@RequestBody Map<String, String> issue) {
        Long issueId = Long.valueOf(issue.get("issueId"));
        LocalDateTime cancelAt = issue.get("cancelIssueAt") == null ? LocalDateTime.now() : LocalDateTime.parse(issue.get("cancelIssueAt"));
        Issue issue1 = issueRepository.findById(issueId).orElseThrow(() -> new IllegalArgumentException("해당 이슈가 존재하지 않습니다."));

        issue1.setCancelIssueAt(cancelAt);

        issueRepository.save(issue1);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/text")
    public ResponseEntity<?> updateIssueText(@RequestBody Map<String, String> issue) {
        Long issueId = Long.valueOf(issue.get("issueId"));
        String issueText = issue.get("issueText");
        Issue issue1 = issueRepository.findById(issueId).orElseThrow(() -> new IllegalArgumentException("해당 이슈가 존재하지 않습니다."));

        issue1.setIssueText(issueText);

        issueRepository.save(issue1);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/photo")
    public ResponseEntity<?> updateIssuePhoto(@RequestBody IssueWithPhotoDto issue) {
        Long issueId = issue.getIssueId();
        List<String> issuePhoto = issue.getIssuePhotos();

        issuePhotoRepository.saveAll(issuePhoto.stream()
                .map(photo -> IssuePhoto.builder()
                        .issueId(issueId)
                        .issuePhoto(photo)
                        .build())
                .collect(Collectors.toList()));

        return ResponseEntity.ok().body(true);
    }

}
