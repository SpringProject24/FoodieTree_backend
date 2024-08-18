package org.nmfw.foodietree.domain.issue.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.issue.dto.res.IssueDto;
import org.nmfw.foodietree.domain.issue.repository.IssueRepository;
import org.nmfw.foodietree.domain.issue.service.IssueService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/issue")
public class IssueController {

    private final IssueService issueService;
    private final IssueRepository issueRepository;

    @GetMapping
    public ResponseEntity<?> issue() {
        log.info("get issues");
        List<IssueDto> issues = issueService.getIssues();
        log.info("issues : {}", issues);
        return ResponseEntity.ok().body(issues);
    }

    @PostMapping
    public ResponseEntity<?> issue(@RequestBody Map<String, String> issue) {
        log.info("save issues");
        log.info("issue : {}", issue);
        issueService.saveWithCategory(issue.get("customerId"), issue.get("issueCategor"));
        return ResponseEntity.ok().body("success");
    }

}
