package org.nmfw.foodietree.domain.product.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.nmfw.foodietree.domain.store.entity.Store;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@ToString(exclude = "store")
@EqualsAndHashCode(of = "productId")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="tbl_product")
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

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idx_store_id")
    private Store store;                 // 가게
}
