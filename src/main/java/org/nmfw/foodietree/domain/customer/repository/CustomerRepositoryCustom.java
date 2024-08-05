package org.nmfw.foodietree.domain.customer.repository;

import java.util.Date;

public interface CustomerRepositoryCustom {
    Date findExpiryDateByEmail(String email);
    // 커스텀 쿼리
}
