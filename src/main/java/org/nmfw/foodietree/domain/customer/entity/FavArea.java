package org.nmfw.foodietree.domain.customer.entity;

import lombok.*;

import javax.persistence.*;

import static org.nmfw.foodietree.domain.customer.entity.QFavArea.favArea;

//@Getter @Setter
//@ToString
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_fav_area")
public class FavArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "preferred_area")
    private String preferredArea;
}
