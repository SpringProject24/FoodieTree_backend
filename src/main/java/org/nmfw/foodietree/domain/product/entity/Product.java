package org.nmfw.foodietree.domain.product.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "store_id")
    private String storeId;

    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;

    @Column(name = "product_upload_date")
    private LocalDateTime productUploadDate;

    @Column(name = "canceled_by_store_at")
    private String cancelByStore;

    @Column(name = "idx_store_id")
    private String idxStoreId;

}
