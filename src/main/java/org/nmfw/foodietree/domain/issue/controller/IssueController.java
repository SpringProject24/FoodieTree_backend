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
import org.nmfw.foodietree.domain.product.Util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Value("${file.upload.root-path}")
    private String rootPath;


    @GetMapping
    public ResponseEntity<?> issue() {
        log.info("get issues");
        List<IssueDto> issues = issueService.getIssues();
        log.info("issues : {}", issues);
        return ResponseEntity.ok().body(issues);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> issueDetail(@RequestParam String issueId) {
        log.info("get issue detail");
        Long issueId1 = Long.valueOf(issueId);
        Issue issue1 = issueRepository.findById(issueId1).orElseThrow(() -> new IllegalArgumentException("해당 이슈가 존재하지 않습니다."));
        IssueDto issueDto = new IssueDto(issue1);
        return ResponseEntity.ok().body(issueDto);
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

    @PostMapping("/saveText")
    public ResponseEntity<?> updateIssueText(@RequestBody Map<String, String> issue) {
        Long issueId = Long.valueOf(issue.get("issueId"));
        String issueText = issue.get("issueText");
        String done = issue.get("done");
        Issue issue1 = issueRepository.findById(issueId).orElseThrow(() -> new IllegalArgumentException("해당 이슈가 존재하지 않습니다."));

        issue1.setIssueText(issueText);

        if(done.equals("cancel")){
            issue1.setCancelIssueAt(LocalDateTime.now());
        }else{
            issue1.setIssueCompleteAt(LocalDateTime.now());
        }

        issueRepository.save(issue1);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/uploadPhoto")
    public ResponseEntity<?> updateIssuePhoto(@RequestParam("files") List<MultipartFile> files, @RequestParam Long issueId) {
        List<String> fileUrls = new ArrayList<>();

        // 파일을 서버에 저장하고 URL을 모은다.
        for (MultipartFile file : files) {
            try {
                String fileUrl = FileUtil.uploadFile(rootPath, file);
                fileUrls.add(fileUrl);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + file.getOriginalFilename());
            }
        }

        // 모든 파일 URL을 DB에 저장한다.
        try {
            List<IssuePhoto> issuePhotos = fileUrls.stream()
                    .map(url -> IssuePhoto.builder()
                            .issueId(issueId)
                            .issuePhoto(url)
                            .build())
                    .collect(Collectors.toList());

            issuePhotoRepository.saveAll(issuePhotos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터베이스 저장 실패");
        }

        return ResponseEntity.ok(fileUrls);
    }

    @GetMapping("/photo/{issueId}")
    public ResponseEntity<?> getIssuePhoto(@PathVariable Long issueId) {
        List<IssuePhoto> issuePhotos = issuePhotoRepository.findAllByIssueId(issueId);
        return ResponseEntity.ok(issuePhotos);
    }
}
