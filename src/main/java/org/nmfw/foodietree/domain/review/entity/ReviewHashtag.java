package org.nmfw.foodietree.domain.review.entity;

import lombok.*;

import javax.persistence.*;

/**
 * 해시태그 키워드 + review Id 중간테이블에 키워드 별 review id 저장
 */

@Entity
@Table(name = "tbl_review_hashtag")
@Getter
@ToString(exclude = "review")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 자동 증가 키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = true)
    private Review review; // 리뷰 아이디 저장

    @Enumerated(EnumType.STRING)
    @Column(name = "hashtag", nullable = true)
    private Hashtag hashtag; // 해시태그 저장
}
