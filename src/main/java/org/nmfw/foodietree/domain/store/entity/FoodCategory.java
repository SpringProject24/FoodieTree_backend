package org.nmfw.foodietree.domain.store.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of="foodName")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_food_category")
public class FoodCategory {

    @Id
    @Column(name = "food_name", nullable = false)
    private String foodName;

    @Column(name = "food_image", nullable = false)
    private String foodImage;
}
