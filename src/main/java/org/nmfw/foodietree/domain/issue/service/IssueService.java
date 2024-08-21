package org.nmfw.foodietree.domain.issue.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.entity.value.IssueCategory;
import org.nmfw.foodietree.domain.issue.dto.res.IssueDto;
import org.nmfw.foodietree.domain.issue.dto.res.IssueWithPhotoDto;
import org.nmfw.foodietree.domain.issue.entity.Issue;
import org.nmfw.foodietree.domain.issue.entity.IssuePhoto;
import org.nmfw.foodietree.domain.issue.repository.IssuePhotoRepository;
import org.nmfw.foodietree.domain.issue.repository.IssueRepository;
import org.nmfw.foodietree.domain.issue.repository.IssueRepositoryCustom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class IssueService {

    private final IssueRepository issueRepository;
    private final IssueRepositoryCustom issueRepositoryCustom;
    private final IssuePhotoRepository issuePhotoRepository;

    public List<IssueDto> getIssues() {
        log.info("getIssues");
        List<Issue> all = issueRepository.findAll();
        return all.stream()
                .map(IssueDto::new) // Convert each Issue entity to IssueDto using the constructor
                .collect(Collectors.toList()); // Collect the results into a List
    }

    public List<IssueWithPhotoDto> getIssueWithPhoto(Long issueId) {
        log.info("getIssueWithPhoto");
        List<Issue> allById = issueRepository.findAllById(Collections.singleton(issueId));
        List<IssuePhoto> issuePhotoById = issuePhotoRepository.findAllById(Collections.singleton(issueId));

        List<IssueWithPhotoDto> dto = allById.stream()
                .map(issue -> {
                    List<String> photos = issuePhotoById.stream()
                            .map(IssuePhoto::getIssuePhoto)
                            .collect(Collectors.toList());
                    return IssueWithPhotoDto.builder()
                            .issueId(issue.getIssueId())
                            .issueCategory(issue.getIssueCategory().toString())
                            .issueCompleteAt(issue.getIssueCompleteAt())
                            .issueText(issue.getIssueText())
                            .cancelIssueAt(issue.getCancelIssueAt())
                            .customerId(issue.getCustomerId())
                            .reservationId(issue.getReservationId())
                            .makeIssueAt(issue.getMakeIssueAt())
//                            .status(issue.getStatus().toString()
                            .issuePhotos(photos)
                            .build();
                })
                .collect(Collectors.toList());

        return dto;
    }

    public void saveWithCategory(String customerId, String issueCategory) {
        log.info("save");
        Issue issue = Issue.builder()
                .customerId(customerId)
                .issueCategory(IssueCategory.fromString(issueCategory).toString())
                .build();
        issueRepository.save(issue);
    }

    public void updateCategory(Long issueId, String issueCategory) {
        log.info("updateCategory");
        log.info("issueId : {}", issueId);
        log.info("issueCategory : {}", issueCategory);
        issueRepositoryCustom.updateCategory(issueId, issueCategory);
    }
}
