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
@Table(name = "tbl_customer_service")
public class CustomerService {

    @Id
    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "issue_id")
    private Long issueId;
}
