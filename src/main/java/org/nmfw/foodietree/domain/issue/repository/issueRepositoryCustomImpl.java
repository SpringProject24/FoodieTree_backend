package org.nmfw.foodietree.domain.issue.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.issue.dto.res.IssueWithPhotoDto;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.nmfw.foodietree.domain.issue.entity.QIssue.issue;

@Repository
@RequiredArgsConstructor
public class issueRepositoryCustomImpl implements IssueRepositoryCustom{

    private final JPAQueryFactory factory;

    @Override
    public List<IssueWithPhotoDto> findIssueWithPhoto() {
//        return factory.select(new issueWithPhotoDto(
//                        issue.issueId,
//                        issue.issueCategory,
//                        issue.issueCompleteAt,
//                        issue.issueText,
//                        issue.cancelIssueAt,
//                        issue.customerId,
//                        issue.reservationId,
//                        issue.makeIssueAt,
////                        issue.status,
//                        Expressions.as(issuePhoto1.issuePhoto).as("issuePhotos")
//                ))
//                .from(issue)
//                .leftJoin(issuePhoto1).on(issue.issueId.eq(issuePhoto1.issueId))
//                .groupBy(issue.issueId)
//                .fetch();
        return null;
    }

    @Override
    public void updateCategory(Long issueId, String issueCategory) {

        factory.update(issue)
                .set(issue.issueCategory, issueCategory)
                .where(issue.issueId.eq(issueId))
                .execute();
    }
}
