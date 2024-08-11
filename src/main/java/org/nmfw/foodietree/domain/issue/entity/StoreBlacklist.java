package org.nmfw.foodietree.domain.issue.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_store_blacklist")
public class StoreBlacklist {

    @Id
    @Column(name = "store_id")
    private String StoreId;

    @Column(name = "black_customer_id")
    private String blackCustomerId;
}
