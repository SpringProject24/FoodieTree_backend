package org.nmfw.foodietree.domain.customer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom {

    private final JPAQueryFactory factory; // builder 대신 사용

    // customer 에서 email로 date 찾기
    @Override
    public Date findExpiryDateByEmail(String email) {
        return null;
    }

}
