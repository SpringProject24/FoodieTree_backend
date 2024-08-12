package org.nmfw.foodietree.domain.issue.entity;

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

@Setter
@Getter
@EqualsAndHashCode(of = "blackCustomerId")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_admin_blacklist")
public class AdminBlacklist {

    @Id
    @Column(name = "black_customer_id")
    private String blackCustomerId;

    @Column(name = "issue_id")
    private String issueId;
}
