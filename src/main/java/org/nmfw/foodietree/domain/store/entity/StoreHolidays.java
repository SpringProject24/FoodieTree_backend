package org.nmfw.foodietree.domain.store.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString

@EqualsAndHashCode(of="id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="tbl_store_holidays")
public class StoreHolidays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // 가게 휴무일 PK

    @Column(name="store_id", nullable = false)
    private String storeId; // 가게 회원의 이메일

    @Column(name="holidays")
    private LocalDate holidays; // 가게 휴무일
}
