package org.nmfw.foodietree.domain.issue.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@EqualsAndHashCode(of = "issuePhotoId")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="tbl_issue_photo")
public class IssuePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_photo_id")
    private Long issuePhotoId;

    @Column(name = "issue_id")
    private Long issueId; // 이슈 아이디 Integer ?

    @Column(name = "issue_photo")
    private String issuePhoto;



}
