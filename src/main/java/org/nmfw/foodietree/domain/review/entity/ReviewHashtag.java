package org.nmfw.foodietree.domain.review.entity;

import lombok.*;

import javax.persistence.*;

/**
 * 해시태그 키워드 + review Id 중간테이블에 키워드 별 review id 저장
 */

@Entity
@Table(name = "review_hashtag")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 자동 증가 키

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review; // 리뷰 아이디 저장

    @Enumerated(EnumType.STRING)
    @Column(name = "hashtag", nullable = false)
    private Hashtag hashtag; // 해시태그 저장
}
