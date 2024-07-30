package org.nmfw.foodietree.domain.auth.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EmailRepositoryCustomImpl implements EmailRepositoryCustom {

    private final JPAQueryFactory factory; //builder 처럼 사용하기

}
