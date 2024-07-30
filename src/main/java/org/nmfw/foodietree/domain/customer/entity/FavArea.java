package org.nmfw.foodietree.domain.customer.entity;

import lombok.*;

import javax.persistence.*;

//@Getter @Setter
//@ToString
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder

@Entity
@Table(name = "tbl_fav_area")
@Setter @Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FavArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "customer_id")
//    private Customer customer;

    @Column(name = "preferred_area")
    private String preferredArea;
}
