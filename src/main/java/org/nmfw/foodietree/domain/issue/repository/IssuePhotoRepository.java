package org.nmfw.foodietree.domain.issue.repository;

import org.nmfw.foodietree.domain.issue.entity.IssuePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuePhotoRepository extends JpaRepository<IssuePhoto, Long> {
}
