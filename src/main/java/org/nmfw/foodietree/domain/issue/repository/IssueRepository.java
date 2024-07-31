package org.nmfw.foodietree.domain.issue.repository;

import org.nmfw.foodietree.domain.issue.entiry.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {
}
