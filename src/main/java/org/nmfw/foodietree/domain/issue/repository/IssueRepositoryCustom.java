package org.nmfw.foodietree.domain.issue.repository;

import org.nmfw.foodietree.domain.issue.dto.res.IssueWithPhotoDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepositoryCustom{
    /**
     * 이슈와 사진을 함께 조회
     * @return 이슈와 사진을 함께 조회한 결과 dto
     */
    List<IssueWithPhotoDto> findIssueWithPhoto();

    void updateCategory(Long issueId, String issueCategory);
}
